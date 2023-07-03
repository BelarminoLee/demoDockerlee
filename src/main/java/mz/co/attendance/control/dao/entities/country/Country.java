package mz.co.attendance.control.dao.entities.country;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import mz.co.attendance.control.dao.entities.base.AuditBean;
import mz.co.attendance.control.dao.entities.province.Province;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import java.util.Collection;

@Entity
@Transactional
@Table(name = "ru_country")
public class Country extends AuditBean {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    private String name;
/*    @OneToMany(mappedBy = "country")
    @JsonIgnoreProperties("country")
    private Collection<Province> provinces;*/
    public Country() {
    }
    public Country(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
/*    public Collection<Province> getProvinces() {
        return provinces;
    }
    public void setProvinces(Collection<Province> provinces) {
        this.provinces = provinces;
    }*/
}
