package mz.co.attendance.control.dao.entities.attendance;

import mz.co.attendance.control.dao.entities.district.District;
import mz.co.attendance.control.dao.entities.healhCenter.HealthCenter;
import mz.co.attendance.control.enums.Bank;
import mz.co.attendance.control.enums.Category;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;

public class AttendanceReportRaw implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private String name;
    private District district;
    private HealthCenter healthCenter;
    private Category category;
    private Bank bank;
    private String account;
    private String nib;
    private BigDecimal subsidy;
    private BigDecimal effectiveness;
    private BigDecimal totalToPay;
    private HashMap<Long, String> attendances;

    public AttendanceReportRaw() {
        this.attendances = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public HealthCenter getHealthCenter() {
        return healthCenter;
    }

    public void setHealthCenter(HealthCenter healthCenter) {
        this.healthCenter = healthCenter;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNib() {
        return nib;
    }

    public void setNib(String nib) {
        this.nib = nib;
    }

    public BigDecimal getSubsidy() {
        return subsidy;
    }

    public void setSubsidy(BigDecimal subsidy) {
        this.subsidy = subsidy;
    }

    public BigDecimal getEffectiveness() {
        return effectiveness;
    }

    public void setEffectiveness(BigDecimal effectiveness) {
        this.effectiveness = effectiveness;
    }

    public BigDecimal getTotalToPay() {
        return totalToPay;
    }

    public void setTotalToPay(BigDecimal totalToPay) {
        this.totalToPay = totalToPay;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public HashMap<Long, String> getAttendances() {
        return attendances;
    }

    public void setAttendances(HashMap<Long, String> attendances) {
        this.attendances = attendances;
    }
}
