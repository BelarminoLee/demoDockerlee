package mz.co.attendance.control.dao.entities.ussd;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import mz.co.attendance.control.enums.MenuOptionAction;

@Data
public class MenuOption {
    private static final long serialVersionUID = 1L;

    private String type;
    private String response;
    @JsonProperty("next_menu_level")
    private String nextMenuLevel;
    private MenuOptionAction action;

    public MenuOption() {
    }

    public MenuOption(String type, String response, String nextMenuLevel, MenuOptionAction action) {
        this.type = type;
        this.response = response;
        this.nextMenuLevel = nextMenuLevel;
        this.action = action;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getNextMenuLevel() {
        return nextMenuLevel;
    }

    public void setNextMenuLevel(String nextMenuLevel) {
        this.nextMenuLevel = nextMenuLevel;
    }

    public MenuOptionAction getAction() {
        return action;
    }

    public void setAction(MenuOptionAction action) {
        this.action = action;
    }
}
