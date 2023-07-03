package mz.co.attendance.control.dao.entities.base;


import org.apache.deltaspike.data.api.audit.CreatedOn;
import org.apache.deltaspike.data.api.audit.ModifiedOn;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public abstract class AuditBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedOn
    private Date createDate;
    @Column(name = "create_user")
    private String createUser;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    @ModifiedOn
    private Date updateDate;
    @Column(name = "update_user")
    private String updateUser;
    private String operation;

    public Date getCreateDate() {
        return createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public String getOperation() { return operation; }


    @PrePersist
    private void setAuditPersistInfo() {
        this.operation = "INSERT";
        this.createDate = new Date();
    }

    @PreRemove
    public void onPreRemove() {
        this.operation = "DELETE";
        this.updateDate = new Date();
    }


    @PreUpdate
    private void setAuditUpdateInfo() {
        this.operation = "UPDATE";
        this.updateDate = new Date();
    }

}
