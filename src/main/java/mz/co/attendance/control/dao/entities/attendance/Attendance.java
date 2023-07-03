package mz.co.attendance.control.dao.entities.attendance;

import mz.co.attendance.control.dao.entities.base.AuditBean;
import mz.co.attendance.control.dao.entities.employee.Employee;
import mz.co.attendance.control.dao.repositories.EmployeeRepository;
import mz.co.attendance.control.enums.Status;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Entity
@Transactional
@Table(name = "ru_attendance")
public class Attendance extends AuditBean {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date entryDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date exitDate;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;
    private String observations;
    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "approver_id")
    private Employee approver;

    @Temporal(TemporalType.TIMESTAMP)
    private Date approvalDate;


    public Attendance(NewAttendance newAttendance, EmployeeRepository employeeRepository) {
        if (Objects.nonNull(newAttendance)) {
            Optional<Employee> employee1 = employeeRepository.findEmployeeByCellphone_Number(newAttendance.getCellPhone());
            employee1.ifPresent(employee2 -> this.employee = employee2);
            this.id = newAttendance.getId();
            this.entryDate = newAttendance.getEntryDate();
            this.exitDate = newAttendance.getExitDate();
            this.status = newAttendance.getStatus();
        }
    }

    public void updateAttendance(NewAttendance newAttendance, EmployeeRepository employeeRepository) {
        if (Objects.nonNull(newAttendance)) {
            if (Objects.isNull(this.employee)) {
                Optional<Employee> employee1 = employeeRepository.findEmployeeByCellphone_Number(newAttendance.getCellPhone());
                employee1.ifPresent(employee2 -> this.employee = employee2);
            }
            this.id = newAttendance.getId();
            this.entryDate = newAttendance.getEntryDate();
            this.exitDate = newAttendance.getExitDate();
            this.status = newAttendance.getStatus();
        }
    }

    public Attendance() {
    }

    public Attendance(Date entryDate, Date exitDate, Employee employee) {
        this.entryDate = entryDate;
        this.exitDate = exitDate;
        this.employee = employee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public Date getExitDate() {
        return exitDate;
    }

    public void setExitDate(Date exitDate) {
        this.exitDate = exitDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Employee getApprover() {
        return approver;
    }

    public void setApprover(Employee approver) {
        this.approver = approver;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }
}
