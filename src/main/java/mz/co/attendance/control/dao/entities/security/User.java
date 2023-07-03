package mz.co.attendance.control.dao.entities.security;


import mz.co.attendance.control.dao.entities.base.AuditBean;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Transactional
@Table(name = "ru_user")
public class User extends AuditBean {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private long id;

    @NotEmpty
    @Size(max = 255)
    @Column(unique = true)
    private String username;

    @NotEmpty
    @Size(max = 255)
    @Column(unique = true)
    private String email;
    @NotNull
    @Size(min = 4, max = 255)
    private String passwordHash;
    @NotBlank
    @Size(max = 255)
    private String firstName;
    @NotBlank
    @Size(max = 255)
    private String lastName;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    private boolean locked;

    @PrePersist
    @PreUpdate
    private void prepareData() {
        this.email = email == null ? null : email.toLowerCase();
        this.username = username == null ? null : username.toLowerCase();
    }

    public User() {
        // An empty constructor is needed for all beans
    }

    public User(String username, String email, String passwordHash, String firstName, String lastName, Role role) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        User that = (User) o;
        return locked == that.locked && Objects.equals(email, that.email) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), email, firstName, lastName, role, locked);
    }
}