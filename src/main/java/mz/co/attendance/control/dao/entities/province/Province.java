package mz.co.attendance.control.dao.entities.province;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mz.co.attendance.control.dao.entities.base.AuditBean;
import mz.co.attendance.control.dao.entities.country.Country;
import mz.co.attendance.control.dao.entities.district.District;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Entity
@Transactional
@Table(name = "ru_province")
public class Province extends AuditBean {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    private String name;
    @ManyToOne
    @NotNull
    @JoinColumn(name = "country_id")
    private Country country;
/*    @OneToMany(mappedBy = "province")
    @JsonIgnoreProperties("province")
    private Collection<District> districts;*/

    public Province(String name, Country country) {
        this.name = name;
        this.country = country;
    }

    public Province() {
    }

    public Country getCountry() {
        return country;
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

    public void setCountry(Country country) {
        this.country = country;
    }

/*    public Collection<District> getDistricts() {
        return districts;
    }

    public void setDistricts(Collection<District> districts) {
        this.districts = districts;
    }*/
}
