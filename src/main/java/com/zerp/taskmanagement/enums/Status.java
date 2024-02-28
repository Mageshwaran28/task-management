package com.zerp.taskmanagement.enums;

import com.zerp.taskmanagement.exceptions.InvalidInputException;

public enum Status {
    PENDING("1"),
    PROCESSING("2"),
    HOLD("3"),
    COMPLETED("4");

    private final String stringValue;

    Status(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public static Status fromString(String stringValue) {
        for (Status status : Status.values()) {
            if (status.stringValue.equals(stringValue)) {
                return status;
            }
        }
        throw new InvalidInputException("Invalid status id : " + stringValue);
    }
}

