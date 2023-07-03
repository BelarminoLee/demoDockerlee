package mz.co.attendance.control.dao.entities.district;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mz.co.attendance.control.dao.entities.base.AuditBean;
import mz.co.attendance.control.dao.entities.healhCenter.HealthCenter;
import mz.co.attendance.control.dao.entities.province.Province;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Entity
@Transactional
@Table(name = "ru_district")
public class District extends AuditBean {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    private String name;
    @ManyToOne
    @NotNull
    @JoinColumn(name = "province_id")
    private Province province;
/*    @OneToMany(mappedBy = "district")
    @JsonIgnoreProperties("district")
    private Collection<HealthCenter> healthCenters;*/

    public District(String name, Province province) {
        this.name = name;
        this.province = province;
    }

    public District() {

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
    public Province getProvince() {
        return province;
    }
    public void setProvince(Province provinceId) {
        this.province = provinceId;
    }
   /* public Collection<HealthCenter> getHealthCenters() {
        return healthCenters;
    }
    public void setHealthCenters(Collection<HealthCenter> healthCenters) {
        this.healthCenters = healthCenters;
    }*/
}
