package mz.co.attendance.control.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Language {

    PT("PORTUGUESE"), EN("ENGLISH");

    private String label;

    Language(String label) {
        this.label = label;
    }

    @JsonValue
    private String getLabel() {
        return label;
    }
}
