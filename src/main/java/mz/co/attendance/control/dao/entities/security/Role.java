package mz.co.attendance.control.dao.entities.security;

import mz.co.attendance.control.dao.entities.base.AuditBean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Transactional
@Table(name = "ru_role")
public class Role extends AuditBean {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "role_id")
    private String id;
    @NotBlank
    @Size(max = 255)
    private String label;
    private String description;

    public Role() {
        // Static methods and fields only
    }

    public Role(String id, String label, String description) {
        this.id = id;
        this.label = label;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Role{" + "id=" + id + ", label='" + label + '\'' + ", description='" + description + '\'' + '}';
    }
}