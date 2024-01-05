package com.zerp.taskmanagement.myenum;


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
        throw new IllegalArgumentException("No enum constant with string value: " + stringValue);
    }
}

