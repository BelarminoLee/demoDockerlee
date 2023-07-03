package mz.co.attendance.control.service.client;

import mz.co.attendance.control.dao.entities.attendance.Attendance;
import mz.co.attendance.control.dao.entities.attendance.NewAttendance;
import mz.co.attendance.control.dao.entities.employee.Employee;
import mz.co.attendance.control.dao.entities.security.USSDLogin;
import mz.co.attendance.control.dao.entities.ussd.Menu;
import mz.co.attendance.control.dao.entities.ussd.MenuOption;
import mz.co.attendance.control.dao.entities.ussd.UssdSession;
import mz.co.attendance.control.enums.Language;
import mz.co.attendance.control.enums.MenuOptionAction;
import mz.co.attendance.control.enums.Status;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UssdRoutingService {
    private final MenuService menuService;
    private final SessionService sessionService;
    private final RestClientService restClientService;
    private final String pattern = "dd/MM/yyyy HH:mm:ss";
    private final String USSD_SHORTCODE_REGEX = "(?:\\*\\d+)+#";
    private final DateFormat df = new SimpleDateFormat(pattern);

    public UssdRoutingService(MenuService menuService, SessionService sessionService, RestClientService restClientService) {
        this.menuService = menuService;
        this.sessionService = sessionService;
        this.restClientService = restClientService;
    }

    /**
     * @param sessionId
     * @param provider
     * @param msisdn
     * @param text
     * @return
     * @throws IOException
     */
    public String menuLevelRouter(String sessionId, String provider, String msisdn, String text) throws IOException {
        Language preferedLanguage = restClientService.getPreferedCellPhoneLanguage(msisdn);
        Map<String, Menu> menus = menuService.loadMenus(preferedLanguage);
        UssdSession session = checkAndSetSession(sessionId, provider, msisdn, text, preferedLanguage);
        /**
         * Check if response has some value
         */
        return (text.length() > 0 && !text.matches(USSD_SHORTCODE_REGEX)) ? getNextMenuItem(session, menus) : menus.get(session.getCurrentMenuLevel()).getText();
    }

    /**
     * @param session
     * @param menus
     * @return
     * @throws IOException
     */
    public String getNextMenuItem(UssdSession session, Map<String, Menu> menus) throws IOException {
        String[] levels = session.getText().split("\\*");
        String lastValue = levels[levels.length - 1];
        Menu menuLevel = menus.get(session.getCurrentMenuLevel());

        if (menuLevel.getMaxSelections() == 0) {
            MenuOption menuOption = menuLevel.getMenuOptions().get(0);
            return processMenuOption(session, menuOption);
        } else if (menuLevel.getMaxSelections() > 0 && Integer.parseInt(lastValue) <= menuLevel.getMaxSelections()) {
            MenuOption menuOption = menuLevel.getMenuOptions().get(Integer.parseInt(lastValue) - 1);
            return processMenuOption(session, menuOption);
        }

        session.setCurrentMenuLevel("7");
        menuLevel = menus.get(session.getCurrentMenuLevel());
        MenuOption menuOption = menuLevel.getMenuOptions().get(0);
        return processMenuOption(session, menuOption);
    }

    /**
     * @param menuLevel
     * @return
     * @throws IOException
     */
    public String getMenu(String menuLevel, Language preferedLanguage) throws IOException {
        return menuService.loadMenus(preferedLanguage).get(menuLevel).getText();
    }

    public String getMenuResponse(String menuLevel, int index, Language preferedLanguage) throws IOException {
        return menuService.loadMenus(preferedLanguage).get(menuLevel).getMenuOptions().get(index).getResponse();
    }
    /**
     * @param menuOption
     * @return
     * @throws IOException
     */
    public String processMenuOption(UssdSession session, MenuOption menuOption) throws IOException {
        switch (menuOption.getType()) {
            case "response":
                return processMenuOptionResponses(session, menuOption);
            case "level":
                if (!session.isAuthenticated()) {
                    String levelText = getMenu(session.getCurrentMenuLevel(), session.getPreferredLanguage());
                    Map<String, String> variablesMap = new HashMap<>();
                    variablesMap.put("attempts", Long.toString(3 - session.getRetries()));
                    levelText = replaceVariable(variablesMap, levelText);
                    return levelText;
                }
                session = validateExistingAttendanceForToday(session);
                if (StringUtils.equals(session.getCurrentMenuLevel(), "8")) {
                    menuOption.setNextMenuLevel("8");
                    menuOption.setType("response");
                    menuOption.setResponse(getMenuResponse("8",0,session.getPreferredLanguage()));
                    menuOption.setAction(MenuOptionAction.TERMINATE_SESSION);
                    return processMenuOptionResponses(session, menuOption);
                } else if (StringUtils.equals(session.getCurrentMenuLevel(), "3")) {
                    menuOption.setNextMenuLevel("3");
                }
                updateSessionMenuLevel(session, menuOption.getNextMenuLevel());
                return getMenu(menuOption.getNextMenuLevel(), session.getPreferredLanguage());
            default:
                return "END ";
        }
    }

    /**
     * @param menuOption
     * @return
     */
    public String processMenuOptionResponses(UssdSession session, MenuOption menuOption) {
        String response = menuOption.getResponse();
        Map<String, String> variablesMap = new HashMap<>();

        switch (menuOption.getAction()) {
            case PROCESS_LOGIN:
                break;
            case PROCESS_ENTRY:
                Date entry = Calendar.getInstance().getTime();
                variablesMap.put("entry_date", df.format(entry));
                NewAttendance newAttendance = new NewAttendance();
                newAttendance.setCellPhone(session.getMsisdn());
                newAttendance.setEntryDate(entry);
                newAttendance.setStatus(Status.INITIATED);
                restClientService.registerAttendance(newAttendance);
                sessionService.delete(session.getSessionId());
                break;
            case PROCESS_EXIT:
                Date exit = Calendar.getInstance().getTime();
                variablesMap.put("exit_date", df.format(exit));
                Attendance attendance = restClientService.getCurrentDateAttendanceByCellPhone(session.getMsisdn());
                if (!Objects.nonNull(attendance)) {
                    response = "Sorry, an error occurred while registering your exit.\nPlease, contact your supervisor!";
                    if (session.getPreferredLanguage() == Language.PT) {
                        response = "Desculpe, ocorreu um erro ao registar a saida.\nPor favor, contacte o seu supervisor!";
                    }
                    break;
                }
                NewAttendance updateAttendance = new NewAttendance();
                updateAttendance.setId(attendance.getId());
                updateAttendance.setCellPhone(session.getMsisdn());
                updateAttendance.setEntryDate(attendance.getEntryDate());
                updateAttendance.setExitDate(exit);
                updateAttendance.setStatus(Status.SUBMITTED_TO_SUPERVISOR_APPROVAL);
                restClientService.updateAttendance(attendance.getId(), updateAttendance);
                sessionService.delete(session.getSessionId());
                break;
            case TERMINATE_SESSION:
                if (session.isCompletedRegistry()) {
                    Date entryDate = session.getEntry();
                    Date exitDate = session.getExit();
                    variablesMap.put("entry_date", df.format(entryDate));
                    variablesMap.put("exit_date", df.format(exitDate));
                    break;
                }
                sessionService.delete(session.getSessionId());
                break;
        }
        response = replaceVariable(variablesMap, response);
        return response;
    }

    /**
     * @param variablesMap
     * @param response
     * @return
     */
    public String replaceVariable(Map<String, String> variablesMap, String response) {
        return new StringSubstitutor(variablesMap).replace(response);
    }

    /**
     * @param session
     * @param menuLevel
     * @return
     */
    public UssdSession updateSessionMenuLevel(UssdSession session, String menuLevel) {
        session.setPreviousMenuLevel(session.getCurrentMenuLevel());
        session.setCurrentMenuLevel(menuLevel);
        return sessionService.update(session);
    }

    /**
     * Check, Set or update the existing session with the provided Session Id
     *
     * @param sessionId
     * @param serviceCode
     * @param phoneNumber
     * @param text
     * @return
     */
    public UssdSession checkAndSetSession(String sessionId, String serviceCode, String phoneNumber, String text, Language preferedLanguage) throws IOException {
        UssdSession session = sessionService.get(sessionId);

        if (!hasAccessToService(phoneNumber)) {
            session = new UssdSession();
            session.setCurrentMenuLevel("6");
            session.setPreviousMenuLevel("1");
            session.setSessionId(sessionId);
            session.setMsisdn(phoneNumber);
            session.setProvider(serviceCode);
            session.setText(text);
            session.setRetries(0);
            session.setPreferredLanguage(preferedLanguage);
            return session;
        }

        if (Objects.nonNull(session)) {
            session.setText(text);
            session.setSessionId(sessionId);
            session.setMsisdn(phoneNumber);
            session.setProvider(serviceCode);
            if (!session.isAuthenticated()) {
                session = validateLogin(text, session);
            }
            return sessionService.update(session);
        } else {
            session = new UssdSession();
            session.setCurrentMenuLevel("1");
            session.setPreviousMenuLevel("1");
            session.setSessionId(sessionId);
            session.setMsisdn(phoneNumber);
            session.setProvider(serviceCode);
            session.setText(text);
            session.setPreferredLanguage(preferedLanguage);
            if (text.length() > 0 && !text.matches(USSD_SHORTCODE_REGEX)) {
                session = validateLogin(text, session);
            }
        }

        return sessionService.createUssdSession(session);
    }

    private UssdSession validateExistingAttendanceForToday(UssdSession session) {
        if (session.isAuthenticated()) {
            Attendance attendance = restClientService.getCurrentDateAttendanceByCellPhone(session.getMsisdn());
            if (Objects.nonNull(attendance)) {
                if (attendance.getEntryDate() != null && attendance.getExitDate() != null) {
                    session.setCurrentMenuLevel("8");
                    session.setCompletedRegistry(true);
                    session.setEntry(attendance.getEntryDate());
                    session.setExit(attendance.getExitDate());
                    return session;
                } else if (attendance.getEntryDate() != null && attendance.getExitDate() == null) {
                    session.setCurrentMenuLevel("3");
                }
            }
        }
        return session;
    }

    private UssdSession validateLogin(String text, UssdSession session) {
        if (StringUtils.isNotBlank(text) && (Integer.parseInt(session.getCurrentMenuLevel()) == 1 || Integer.parseInt(session.getCurrentMenuLevel()) == 4)) {
            if (pinIsValid(session.getMsisdn(), text)) {
                session.setAuthenticated(true);
                session.setRetries(0);
                // session = validateExistingAttendanceForToday(session);
            } else {
                session.setAuthenticated(false);
                session.setRetries(session.getRetries() + 1);
                if (session.getRetries() > 0 && session.getRetries() < 3) {
                    session.setCurrentMenuLevel("4");
                    session.setPreviousMenuLevel("1");
                } else {
                    session.setCurrentMenuLevel("5");
                    session.setPreviousMenuLevel("4");
                }
            }
        }
        return session;
    }

    private boolean hasAccessToService(String phoneNumber) {
        Employee employee = restClientService.searchByEmployeeCellPhone(phoneNumber);
        return Objects.nonNull(employee) && employee.isEnabled();
    }

    private boolean pinIsValid(String cellPhone, String pin) {
        USSDLogin ussdLogin = new USSDLogin(cellPhone, pin);
        ussdLogin = restClientService.validateLogin(ussdLogin);
        return Objects.nonNull(ussdLogin) && ussdLogin.isValid();
    }
}
