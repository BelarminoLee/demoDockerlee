package mz.co.attendance.control.dao.entities.ussd;


import mz.co.attendance.control.enums.Language;
import org.apache.deltaspike.data.api.audit.CreatedOn;
import org.apache.deltaspike.data.api.audit.ModifiedOn;
import org.hibernate.envers.AuditTable;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Date;


@Entity
@Transactional
@Table(name = "ru_ussd_session")
@AuditTable("hi_ussd_session")
public class UssdSession implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private long id;
    private String sessionId;
    private String provider;
    private String msisdn;
    private String text;
    private String previousMenuLevel;
    private String currentMenuLevel;
    private boolean isAuthenticated;
    private boolean completedRegistry;
    private Date entry;
    private Date exit;
    private long retries;
    private Language preferredLanguage;
    private String status;

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedOn
    private Date createDate;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    @ModifiedOn
    private Date updateDate;

    public UssdSession() {
    }
    public String getSessionId() {
        return sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    public String getProvider() {
        return provider;
    }
    public void setProvider(String serviceCode) {
        this.provider = serviceCode;
    }
    public String getMsisdn() {
        return msisdn;
    }
    public void setMsisdn(String phoneNumber) {
        this.msisdn = phoneNumber;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getPreviousMenuLevel() {
        return previousMenuLevel;
    }
    public void setPreviousMenuLevel(String previousMenuLevel) {
        this.previousMenuLevel = previousMenuLevel;
    }
    public String getCurrentMenuLevel() {
        return currentMenuLevel;
    }
    public void setCurrentMenuLevel(String currentMenuLevel) {
        this.currentMenuLevel = currentMenuLevel;
    }
    public long getRetries() {
        return retries;
    }
    public void setRetries(long retries) {
        this.retries = retries;
    }
    public boolean isAuthenticated() {
        return isAuthenticated;
    }
    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }
    public Language getPreferredLanguage() { return preferredLanguage; }
    public void setPreferredLanguage(Language preferedLanguage) { this.preferredLanguage = preferedLanguage; }
    public boolean isCompletedRegistry() { return completedRegistry; }
    public void setCompletedRegistry(boolean completedRegistry) { this.completedRegistry = completedRegistry; }
    public Date getEntry() { return entry; }
    public void setEntry(Date entry) { this.entry = entry; }
    public Date getExit() { return exit; }
    public void setExit(Date exit) { this.exit = exit; }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String operation) {
        this.status = operation;
    }

    @PrePersist
    public void onPrePersist() {
        audit("ACTIVE");
        this.createDate = new Date();
    }

    @PreUpdate
    public void onPreUpdate() {
        audit("ACTIVE");
        this.updateDate = new Date();
    }

    @PreRemove
    public void onPreRemove() {
        audit("EXPIRED");
        this.updateDate = new Date();
    }

    private void audit(String operation) {
        setStatus(operation);
    }
}
