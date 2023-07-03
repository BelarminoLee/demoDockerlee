package mz.co.attendance.control.dao.entities.employee;

import mz.co.attendance.control.dao.entities.base.AuditBean;
import mz.co.attendance.control.dao.entities.healhCenter.HealthCenter;
import mz.co.attendance.control.enums.Bank;
import mz.co.attendance.control.enums.Category;
import mz.co.attendance.control.enums.Currency;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Transactional
@Table(name = "ru_employee")
public class Employee extends AuditBean {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    private String name;
    @OneToOne(cascade = CascadeType.ALL)
    private CellPhone cellphone;
    private String email;
    private Long nuit;
    @Enumerated(EnumType.STRING)
    private Bank bank;
    private Long account;
    private Long nib;
    private BigDecimal subsidy;
    @JoinColumn(name = "subsidy_currency")
    @Enumerated(EnumType.STRING)
    private Currency subsidyCurrency;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Category category;
    @ManyToOne
    @JoinColumn(name = "health_center_id")
    private HealthCenter healthCenter;
    private boolean enabled;

    public Employee() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CellPhone getCellphone() {
        return cellphone;
    }

    public String getCellphoneNumber() {
        return cellphone != null ? cellphone.getNumber() : null;
    }

    public void setCellphone(CellPhone cellphone) {
        this.cellphone = cellphone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getNuit() {
        return nuit;
    }

    public void setNuit(Long nuit) {
        this.nuit = nuit;
    }

    public Bank getBank() {
        return bank;
    }

    public String getBankName() {
        return bank.getLabel();
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Long getAccount() {
        return account;
    }

    public void setAccount(Long account) {
        this.account = account;
    }

    public Long getNib() {
        return nib;
    }

    public void setNib(Long nib) {
        this.nib = nib;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public HealthCenter getHealthCenter() {
        return healthCenter;
    }

    public void setHealthCenter(HealthCenter healthCenterId) {
        this.healthCenter = healthCenterId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public BigDecimal getSubsidy() {
        return subsidy;
    }

    public void setSubsidy(BigDecimal subsidy) {
        this.subsidy = subsidy;
    }

    public Currency getSubsidyCurrency() {
        return subsidyCurrency;
    }

    public void setSubsidyCurrency(Currency subsidyCurrency) {
        this.subsidyCurrency = subsidyCurrency;
    }
}
