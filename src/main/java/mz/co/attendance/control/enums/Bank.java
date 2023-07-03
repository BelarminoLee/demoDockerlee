package mz.co.attendance.control.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Bank {

    STB("Standard Bank"),

    MBIM("Millennium Bim"),

    FNB("First National Bank"),

    ABSA("Absa"),

    UBA("UBA"),

    FCB("First Capital Bank"),

    SOCREMO("Socremo"),

    BCI("Banco Comercial e de Investimentos");

    private String label;

    Bank(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}
