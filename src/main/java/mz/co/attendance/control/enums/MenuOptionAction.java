package mz.co.attendance.control.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MenuOptionAction {

    PROCESS_LOGIN("PROCESS_LOGIN"),

    PROCESS_ENTRY("PROCESS_ENTRY"),

    PROCESS_EXIT("PROCESS_EXIT"),

    PROCESS_REGISTER_PIN_PHASE_1("PROCESS_REGISTER_PIN_PHASE_1"),

    PROCESS_REGISTER_PIN_PHASE_2("PROCESS_REGISTER_PIN_PHASE_2"),

    TERMINATE_SESSION("TERMINATE_SESSION");


    private String action;

    MenuOptionAction(String action) {
        this.action = action;
    }

    @JsonValue
    private String getAction() {
        return action;
    }
}
