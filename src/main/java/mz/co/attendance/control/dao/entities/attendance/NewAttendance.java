package mz.co.attendance.control.dao.entities.attendance;


import lombok.Data;
import mz.co.attendance.control.enums.Status;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Data
public class NewAttendance implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date entryDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date exitDate;
    private long employeeId;
    private String cellPhone;
    private Status status;
    private long approver;
    public NewAttendance() {
        this.status = Status.INITIATED;
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

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public long getApprover() {
        return approver;
    }

    public void setApprover(long approver) {
        this.approver = approver;
    }
    public String getCellPhone() {
        return cellPhone;
    }
    public void setCellPhone(String cellPhone) {this.cellPhone = cellPhone;}

}
