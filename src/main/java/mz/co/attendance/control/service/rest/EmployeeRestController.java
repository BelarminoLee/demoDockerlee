package mz.co.attendance.control.service.rest;

import mz.co.attendance.control.dao.entities.employee.Employee;
import mz.co.attendance.control.dao.repositories.EmployeeRepository;
import mz.co.attendance.control.dao.repositories.HealthCenterRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(value = "/employee")
public class EmployeeRestController {
    private final EmployeeRepository employeeRepository;
    private final HealthCenterRepository emHealthCenterRepository;

    public EmployeeRestController(EmployeeRepository employeeRepository, HealthCenterRepository emHealthCenterRepository) {
        this.employeeRepository = employeeRepository;
        this.emHealthCenterRepository = emHealthCenterRepository;
    }

    /**
     * REST API for getting employee list
     * <p>
     * GET http://localhost:8082/api/employee/list
     */
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Employee>> getAll() {
        return new ResponseEntity(employeeRepository.findAll(), HttpStatus.OK);
    }

    /**
     * REST API for getting employee by CellPhone
     * <p>
     * GET http://localhost:8082/api/employee/search?cellPhone={cellPhone}
     */
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Employee> searchByCellPhone(@RequestParam("cellPhone") String cellPhone) {
        return new ResponseEntity(employeeRepository.findEmployeeByCellphone_Number(cellPhone).isEmpty() ? null : employeeRepository.findEmployeeByCellphone_Number(cellPhone).get(), HttpStatus.OK);
    }


    /**
     * REST API for getting employee list
     * <p>
     * GET http://localhost:8082/api/employee/listPaginated
     */
    @GetMapping(value = "/listPaginated", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Page<Employee>> getAllPaginated(@RequestParam("page") int page, @RequestParam("maxResults") int maxResults) {
        Pageable pageable = PageRequest.of(page, maxResults);
        return new ResponseEntity(employeeRepository.findAll(pageable), HttpStatus.OK);
    }

    /**
     * REST API for creating new employee entry
     * <p>
     * POST http://localhost:8082/api/employee/create
     *
     * @param newEmployee the new employee info
     */
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Employee> createEmployee(@RequestBody Employee newEmployee) {
        try {
            if (Objects.isNull(newEmployee)) {
                throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Employee info is empty");
            }
            Employee employee = employeeRepository.save(newEmployee);
            return new ResponseEntity<>(employee, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException exc) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Error while persisting Employee", exc);
        }
    }

    /**
     * REST API for updating new employee entry
     * <p>
     * PUT http://localhost:8082/api/employee/{id}/update
     *
     * @param newEmployee the new employee info
     */
    @PostMapping(value = "/{id}/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Employee> updateEmployee(@RequestParam Long id, @RequestBody Employee newEmployee) {
        try {
            Optional<Employee> foundedEmployee = employeeRepository.findById(id);
            if (foundedEmployee.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Specified employee doesn't exist");
            }
            if (Objects.isNull(newEmployee)) {
                throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Employee info is empty");
            }
            Employee employee = employeeRepository.save(newEmployee);
            return new ResponseEntity<>(employee, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException exc) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Error while persisting Employee", exc);
        }
    }

}
