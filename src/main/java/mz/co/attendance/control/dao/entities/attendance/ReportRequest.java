package mz.co.attendance.control.dao.entities.attendance;

import mz.co.attendance.control.dao.entities.district.District;
import mz.co.attendance.control.dao.entities.healhCenter.HealthCenter;
import mz.co.attendance.control.dao.entities.province.Province;
import mz.co.attendance.control.enums.Status;

import java.io.Serializable;
import java.util.List;

public class ReportRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<DateRange> dateRanges;
    private Status status;
    private Province province;
    private District district;
    private HealthCenter healthCenter;
    public ReportRequest() {
    }

    public List<DateRange> getDateRanges() {
        return dateRanges;
    }

    public void setDateRanges(List<DateRange> dateRanges) {
        this.dateRanges = dateRanges;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
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
}
