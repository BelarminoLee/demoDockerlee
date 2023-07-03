package mz.co.attendance.control.dao.entities.configuration;

import mz.co.attendance.control.dao.entities.base.AuditBean;
import org.hibernate.envers.AuditTable;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.Objects;


@Entity
@Transactional
@Table(name = "ru_configuration")
@AuditTable("hi_configuration")
public class Configuration extends AuditBean {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "property", nullable = false)
    private String property;
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ConfigurationType type;
    @Column(name = "cfg_value")
    private String value;
    @Column(name = "description", nullable = false)
    private String description;
    private String tag;

    public Configuration() {
    }

    public Configuration(String property, ConfigurationType type, String value, String description, String tag) {
        this.property = property;
        this.type = type;
        this.value = value;
        this.description = description;
        this.tag = tag;
    }

    public ConfigurationType getType() {
        return this.type;
    }

    public void setType(ConfigurationType type) {
        this.type = type;
    }

    public String getProperty() {
        return this.property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Configuration that = (Configuration) o;
        return Objects.equals(property, that.property) && type == that.type && Objects.equals(value, that.value) && Objects.equals(description, that.description) && Objects.equals(tag, that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(property, type, value, description, tag);
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "id=" + id +
                ", property='" + property + '\'' +
                ", type=" + type +
                ", value='" + value + '\'' +
                ", description='" + description + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}
