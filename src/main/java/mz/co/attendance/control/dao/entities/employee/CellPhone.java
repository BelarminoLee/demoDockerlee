package mz.co.attendance.control.dao.entities.employee;

import com.fasterxml.jackson.annotation.JsonProperty;
import mz.co.attendance.control.dao.entities.base.AuditBean;
import mz.co.attendance.control.enums.Language;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Transactional
@Table(name = "ru_employee_cellphone")
public class CellPhone extends AuditBean {
    private static final long serialVersionUID = 1L;
    @Id
    private String number;

    @Size(min = 4, max = 255)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String pin;
    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "employee_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Employee employee;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Language preferredLanguage;

    public CellPhone() {
        this.preferredLanguage = Language.PT;
    }

    public CellPhone(String pin, String number, Employee employee, Language preferredLanguage) {
        this.pin = pin;
        this.number = number;
        this.employee = employee;
        this.preferredLanguage = preferredLanguage;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Language getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(Language preferedLanguage) {
        this.preferredLanguage = preferedLanguage;
    }
}
