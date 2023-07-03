package mz.co.attendance.control.dao.entities.ussd;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Menu {
    private static final long serialVersionUID = 1L;
    @JsonProperty("id")
    private String id;
    @JsonProperty("menu_level")
    private String menuLevel;
    @JsonProperty("text")
    private String text;
    @JsonProperty("menu_options")
    private List<MenuOption> menuOptions;
    @JsonProperty("action")
    private String action;
    @JsonProperty("max_selections")
    private Integer maxSelections;

    public Menu() {
    }

    public Menu(String id, String menuLevel, String text, List<MenuOption> menuOptions, String action, Integer maxSelections) {
        this.id = id;
        this.menuLevel = menuLevel;
        this.text = text;
        this.menuOptions = menuOptions;
        this.action = action;
        this.maxSelections = maxSelections;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenuLevel() {
        return menuLevel;
    }

    public void setMenuLevel(String menuLevel) {
        this.menuLevel = menuLevel;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<MenuOption> getMenuOptions() {
        return menuOptions;
    }

    public void setMenuOptions(List<MenuOption> menuOptions) {
        this.menuOptions = menuOptions;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getMaxSelections() {
        return maxSelections;
    }

    public void setMaxSelections(Integer maxSelections) {
        this.maxSelections = maxSelections;
    }
}
