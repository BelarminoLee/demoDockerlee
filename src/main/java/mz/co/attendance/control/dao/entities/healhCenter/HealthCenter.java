package mz.co.attendance.control.dao.entities.healhCenter;

import mz.co.attendance.control.dao.entities.base.AuditBean;
import mz.co.attendance.control.dao.entities.district.District;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Transactional
@Table(name = "ru_health_center")
public class HealthCenter extends AuditBean {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    private String name;
    @ManyToOne
    @NotNull
    @JoinColumn(name = "district_id")
    private District district;

    private String latitude;

    private String longitude;

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

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District districtId) {
        this.district = districtId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
