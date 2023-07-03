package mz.co.attendance.control.dao.entities.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class USSDLogin {

    private String hash;
    @JsonProperty("is_valid")
    private boolean isValid;
    private String cellPhone;

    public USSDLogin() {
    }

    public USSDLogin(String cellPhone, String hash) {
        this.hash = hash;
        this.cellPhone = cellPhone;
        this.isValid = false;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }
}
