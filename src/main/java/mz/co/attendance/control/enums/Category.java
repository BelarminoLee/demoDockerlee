package mz.co.attendance.control.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Category {

    SUPERVISON("Supervisor"),

    VOLUNTEER("Volunteer"),

    COORDINATIOR("Coordinator");

    private String label;

    Category(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}
