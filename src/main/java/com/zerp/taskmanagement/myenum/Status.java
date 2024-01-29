package com.zerp.taskmanagement.myenum;


public enum Status {
    PENDING("1"),
    PROCESSING("2"),
    COMPLETED("3");
    // PENDING,
    // PROCESSING,
    // COMPLETED;

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
        return null;
    }
}

