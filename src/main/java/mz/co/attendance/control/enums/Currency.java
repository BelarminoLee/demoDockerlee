package mz.co.attendance.control.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Currency {

    MZN("MZN"),

    ZAR("ZAR"),

    USD("USD"),

    EUR("EUR");

    private String label;

    Currency(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}
