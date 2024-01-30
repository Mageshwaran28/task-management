package com.zerp.taskmanagement.myenum;

import com.zerp.taskmanagement.customexception.InvalidInputException;

public enum Status {
    PENDING("1"),
    PROCESSING("2"),
    COMPLETED("3");

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

