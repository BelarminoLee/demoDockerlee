package mz.co.attendance.control.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {

    INITIATED("Initiated"),
    SUBMITTED_TO_SUPERVISOR_APPROVAL("Submitted To Supervisor"),
    APPROVED("Approved"),
    SUBMITTED_TO_COORDINATOR_APPROVAL("Submitted To Coordinator");

    private String label;

    Status(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}
