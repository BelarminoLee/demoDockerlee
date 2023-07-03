package mz.co.attendance.control.service.rest;

import mz.co.attendance.control.dao.entities.attendance.*;
import mz.co.attendance.control.dao.entities.employee.Employee;
import mz.co.attendance.control.dao.repositories.AttendanceControlRepository;
import mz.co.attendance.control.dao.repositories.EmployeeRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/attendance")
public class AttendanceRestController {

    private final AttendanceControlRepository attendanceControlRepository;

    private final EmployeeRepository employeeRepository;

    public AttendanceRestController(EmployeeRepository employeeRepository, AttendanceControlRepository attendanceControlRepository) {
        this.attendanceControlRepository = attendanceControlRepository;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Attendance>> getAll(@RequestParam("page") int page, @RequestParam("maxResults") int maxResults) {
        Pageable pageable = PageRequest.of(page, maxResults);
        return new ResponseEntity(attendanceControlRepository.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/byCurrentDateAndCellPhone", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Attendance> byCurrentDateAndCellPhone(@RequestParam("cellPhone") String cellPhone) {
        Optional<Employee> employee = employeeRepository.findEmployeeByCellphone_Number(cellPhone);
        if (employee.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Employee is registered with the provided cellphone number!");
        }
        Attendance attendance = attendanceControlRepository.queryByCurrentDate(employee.get().getId());
        return new ResponseEntity(attendance, HttpStatus.OK);
    }

    @PostMapping(value = "/report", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AttendanceReport> getAttendanceReport(@RequestBody ReportRequest request) {
        if (Objects.isNull(request)) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Report Request info is empty");
        }
        AttendanceReport attendanceReport = new AttendanceReport();
        attendanceReport.setCountableDays(calculateDays(request));
        return new ResponseEntity(attendanceReport, HttpStatus.OK);
    }

    private static long calculateDays(ReportRequest request) {
        long countableDays = 0;
        for (DateRange dateRange : request.getDateRanges()) {
            long dateBeforeInMs = getDate(dateRange.getStartDate()).getTime();
            long dateAfterInMs = getDate(dateRange.getEndDate()).getTime();
            long timeDiff = Math.abs(dateAfterInMs - dateBeforeInMs);
            long daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
            countableDays += daysDiff;
        }
        return countableDays;
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Attendance> createAttendanceControl(@RequestBody NewAttendance newAttendance) {
        try {
            if (Objects.isNull(newAttendance)) {
                throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Attendance info is empty");
            }
            Attendance attendance = new Attendance(newAttendance, employeeRepository);
            attendanceControlRepository.save(attendance);
            return new ResponseEntity(attendance, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException exc) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Attendance for '" + newAttendance.getCellPhone() + "' is already registered. ", exc);
        }
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Attendance> updateAttendanceControl(@PathVariable long id, @RequestBody NewAttendance newAttendance) {
        try {
            if (Objects.isNull(newAttendance)) {
                throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Attendance Control info is empty");
            }
            if (!attendanceControlRepository.existsById(id)) {
                throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Specified Attendance doesn't exist ");
            }

            Attendance attendance = attendanceControlRepository.findById(id).get();
            attendance.updateAttendance(newAttendance, employeeRepository);
            attendanceControlRepository.save(attendance);
            return new ResponseEntity(attendance, HttpStatus.OK);
        } catch (DataIntegrityViolationException exc) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Attendance is already registered. ", exc);
        }
    }

    private static Date getDate(LocalDate picker) {
        return Date.from(picker.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
}
