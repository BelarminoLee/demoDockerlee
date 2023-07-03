package mz.co.attendance.control.service.rest;


import mz.co.attendance.control.dao.entities.attendance.Attendance;
import mz.co.attendance.control.dao.entities.employee.CellPhone;
import mz.co.attendance.control.dao.entities.security.USSDLogin;
import mz.co.attendance.control.dao.entities.ussd.Menu;
import mz.co.attendance.control.dao.entities.ussd.UssdSession;
import mz.co.attendance.control.dao.repositories.CellPhoneRepository;
import mz.co.attendance.control.dao.repositories.UssdSessionRepository;
import mz.co.attendance.control.enums.Language;
import mz.co.attendance.control.service.client.MenuService;
import mz.co.attendance.control.service.client.UssdRoutingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(value = "/ussdGateway")
public class USSDRestController {


    private final MenuService menuService;

    private final UssdRoutingService ussdRoutingService;

    private final CellPhoneRepository cellPhoneRepository;

    private final UssdSessionRepository ussdSessionRepository;
    public USSDRestController(MenuService menuService, UssdRoutingService ussdRoutingService, CellPhoneRepository cellPhoneRepository, UssdSessionRepository ussdSessionRepository) {
        this.menuService = menuService;
        this.ussdRoutingService = ussdRoutingService;
        this.cellPhoneRepository = cellPhoneRepository;
        this.ussdSessionRepository = ussdSessionRepository;
    }

    /**
     * @return
     * @throws IOException
     */
    @GetMapping(path = "menus")
    public Map<String, Menu> menusLoad(@RequestParam("language") Language language) throws IOException {
        return menuService.loadMenus(language);
    }

    @PostMapping(value = "/validate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<USSDLogin> validateLogin(@RequestBody USSDLogin ussdLogin) {
        if (Objects.isNull(ussdLogin)) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Provided USSDLogin payload is invalid");
        }
        CellPhone cellPhone = cellPhoneRepository.getByNumber(ussdLogin.getCellPhone());
        if (Objects.nonNull(cellPhone) && Objects.nonNull(cellPhone.getEmployee()) && cellPhone.getEmployee().isEnabled() && pinMatches(ussdLogin.getHash(), cellPhone.getPin())) {
            ussdLogin.setValid(true);
        }
        return new ResponseEntity<>(ussdLogin, HttpStatus.CREATED);
    }

    @GetMapping(value = "/paginated", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<UssdSession>> getAll(@RequestParam("page") int page, @RequestParam("maxResults") int maxResults){
        Pageable pageable = PageRequest.of(page, maxResults);
        return new ResponseEntity(ussdSessionRepository.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "menus/Language/{msisdn}")
    public Language getCellPhonePreferedLanguage(@PathVariable("msisdn") String msisdn) throws IOException {
        CellPhone cellPhone = cellPhoneRepository.getByNumber(msisdn);
        if (Objects.isNull(cellPhone)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("CellPhone %s not found", msisdn));
        }
        return cellPhone.getPreferredLanguage();
    }

    /**
     * @return
     */
    @GetMapping(path = "")
    public String index() {
        return "USSD Service is Up!";
    }

    /**
     * @param sessionId
     * @param provider
     * @param msisdn
     * @param text
     * @return
     */
    @PostMapping(path = "")
    public String ussdIngress(@RequestParam @NotEmpty String sessionId, @RequestParam @NotEmpty String provider, @RequestParam @NotEmpty String msisdn, @RequestParam String text) {
        try {
            if (StringUtils.isBlank(sessionId) || StringUtils.isBlank(provider) || StringUtils.isBlank(msisdn)) {
                throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "One ore more mandatory fields not specified");
            }
            return ussdRoutingService.menuLevelRouter(sessionId, provider, msisdn, text);
        } catch (IOException e) {
            return "END " + e.getMessage();
        }
    }

    private boolean pinMatches(String provided, String stored) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(provided, stored);
    }
}
