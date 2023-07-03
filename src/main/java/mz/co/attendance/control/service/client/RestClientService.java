package mz.co.attendance.control.service.client;

import mz.co.attendance.control.dao.entities.attendance.Attendance;
import mz.co.attendance.control.dao.entities.attendance.AttendanceReport;
import mz.co.attendance.control.dao.entities.attendance.ReportRequest;
import mz.co.attendance.control.dao.entities.attendance.NewAttendance;
import mz.co.attendance.control.dao.entities.configuration.Configuration;
import mz.co.attendance.control.dao.entities.district.District;
import mz.co.attendance.control.dao.entities.employee.Employee;
import mz.co.attendance.control.dao.entities.healhCenter.HealthCenter;
import mz.co.attendance.control.dao.entities.province.Province;
import mz.co.attendance.control.dao.entities.security.USSDLogin;
import mz.co.attendance.control.dao.entities.security.User;
import mz.co.attendance.control.dao.entities.ussd.UssdSession;
import mz.co.attendance.control.enums.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.time.Duration;
import java.util.List;

@SuppressWarnings("serial")
@Service
public class RestClientService {

    @Autowired
    WebClient webClient;

    /**
     * The port changes depending on where we deploy the app
     */
    @Value("${server.port}")
    private String serverPort;
    @Value("${local.server.address}")
    private String serverAddress;
    @Value("${caching.spring.defaultTTL}")
    private String cacheTimeout;


    // Configuration Services
    public List<Configuration> getAllConfiguration() {
        final String url = baseURI() + "configuration/list";
        final List<Configuration> configurations = webClient.get().uri(url).retrieve().toEntityList(Configuration.class).block().getBody();
        return configurations;
    }

    public Configuration getConfigByProperty(String property) {
        final String url = baseURI() + "configuration/byProperty/" + property;
        final Configuration configuration = webClient.get().uri(url).retrieve().toEntity(Configuration.class).block().getBody();
        return configuration;
    }

    public Configuration updateConfiguration(long id, Configuration configuration) {
        final String url = baseURI() + "configuration/update/" + id;
        final Mono<Configuration> configurationMono = webClient.put().uri(url).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).body(Mono.just(configuration), Configuration.class).retrieve().bodyToMono(Configuration.class);
        return configurationMono.block();
    }



    public Attendance getCurrentDateAttendanceByCellPhone(String cellPhone) {
        final String url = baseURI() + "attendance/byCurrentDateAndCellPhone?cellPhone=" + cellPhone;
        final Attendance attendance = webClient.get().uri(url).retrieve().toEntity(Attendance.class).block().getBody();
        return attendance;
    }

    public Employee searchByEmployeeCellPhone(String cellPhone) {
        final String url = baseURI() + "employee/search?cellPhone=" + cellPhone;
        final Employee employee = webClient.get().uri(url).retrieve().toEntity(Employee.class).block().getBody();
        return employee;
    }

    public Language getPreferedCellPhoneLanguage(String msisdn) {
        final String url = baseURI() + "ussdGateway/menus/Language/" + msisdn;
        final Mono<Language> preferedLanguage = webClient.get().uri(url).retrieve().bodyToMono(Language.class).cache(Duration.ofSeconds(Long.parseLong(cacheTimeout)));
        return preferedLanguage.block();
    }

    public RestResponsePage<Employee> getAllEmployees(int page, int maxResults) {
        final String url = baseURI() + "employee/listPaginated?page=" + page + "&maxResults=" + maxResults;
        final RestResponsePage<Employee> employees = webClient.get().uri(url).retrieve().bodyToMono(new ParameterizedTypeReference<RestResponsePage<Employee>>() {
        }).block();
        return employees;
    }

    public List<Province> getAllProvinces() {
        final String url = baseURI() + "province/all";
        final List<Province> provinces = webClient.get().uri(url).retrieve().toEntityList(Province.class).block().getBody();
        return provinces;
    }
    public List<District> getAllDistrictsByProvince(long province) {
        final String url = baseURI() + "district/allBy?province=" + province;
        final List<District> districts = webClient.get().uri(url).retrieve().toEntityList(District.class).block().getBody();
        return districts;
    }
    public List<HealthCenter> getAllHealthCentersByDistrict(long district) {
        final String url = baseURI() + "healthCenter/allBy?district=" + district;
        final List<HealthCenter> healthCenters = webClient.get().uri(url).retrieve().toEntityList(HealthCenter.class).block().getBody();
        return healthCenters;
    }

    public List<HealthCenter> getAllHealthCenters() {
        final String url = baseURI() + "healthCenter/all";
        final List<HealthCenter> healthCenters = webClient.get().uri(url).retrieve().toEntityList(HealthCenter.class).block().getBody();
        return healthCenters;
    }

    public RestResponsePage<User> getAllUsers(int page, int maxResults) {
        final String url = baseURI() + "user/paginated?page=" + page + "&maxResults=" + maxResults;
        ParameterizedTypeReference<RestResponsePage<User>> parameterizedTypeReference = new ParameterizedTypeReference<>() {};
        final RestResponsePage<User> users = webClient.get().uri(url).retrieve().toEntity(parameterizedTypeReference).block().getBody();
        return users;
    }

    public RestResponsePage<UssdSession> getAllUSSDSessions(int page, int maxResults) {
        final String url = baseURI() + "ussdGateway/paginated?page=" + page + "&maxResults=" + maxResults;
        ParameterizedTypeReference<RestResponsePage<UssdSession>> parameterizedTypeReference = new ParameterizedTypeReference<>() {};
        final RestResponsePage<UssdSession> ussdSessions = webClient.get().uri(url).retrieve().toEntity(parameterizedTypeReference).block().getBody();
        return ussdSessions;
    }

    public RestResponsePage<HealthCenter> getAllHealthCenters(int page, int maxResults) {
        final String url = baseURI() + "healthCenter/paginated?page=" + page + "&maxResults=" + maxResults;
        ParameterizedTypeReference<RestResponsePage<HealthCenter>> parameterizedTypeReference = new ParameterizedTypeReference<>() {};
        final RestResponsePage<HealthCenter> healthCenters = webClient.get().uri(url).retrieve().toEntity(parameterizedTypeReference).block().getBody();
        return healthCenters;
    }

    public AttendanceReport getAttendanceReport(ReportRequest request) {
        final String url = baseURI() + "attendance/report";
        final Mono<AttendanceReport> attendanceReportMono = webClient.post().uri(url).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).body(Mono.just(request), ReportRequest.class).retrieve().bodyToMono(AttendanceReport.class);
        return attendanceReportMono.block();
    }

    public RestResponsePage<Attendance> getAllAttendance(int page, int maxResults) {
        final String url = baseURI() + "attendance/all?page=" + page + "&maxResults=" + maxResults;
        ParameterizedTypeReference<RestResponsePage<Attendance>> parameterizedTypeReference = new ParameterizedTypeReference<>() {
        };
        final RestResponsePage<Attendance> attendanceControls = webClient.get().uri(url).retrieve().toEntity(parameterizedTypeReference).block().getBody();

        return attendanceControls;
    }

    public Attendance registerAttendance(NewAttendance newAttendance) {
        final String url = baseURI() + "attendance/register";
        final Mono<Attendance> attendance = webClient.post().uri(url).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).body(Mono.just(newAttendance), NewAttendance.class).retrieve().bodyToMono(Attendance.class);
        return attendance.block();
    }

    public Attendance updateAttendance(long id, NewAttendance newAttendance) {
        final String url = baseURI() + "attendance/update/" + id;
        final Mono<Attendance> attendance = webClient.put().uri(url).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).body(Mono.just(newAttendance), NewAttendance.class).retrieve().bodyToMono(Attendance.class);
        return attendance.block();
    }

    public USSDLogin validateLogin(USSDLogin ussdLoginAttempt) {
        final String url = baseURI() + "ussdGateway/validate";
        final Mono<USSDLogin> ussdLogin = webClient.post().uri(url).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).body(Mono.just(ussdLoginAttempt), USSDLogin.class).retrieve().bodyToMono(USSDLogin.class);
        return ussdLogin.block();
    }

    private String baseURI() {
        return serverAddress + ":" + serverPort + "/api/";
    }


}
