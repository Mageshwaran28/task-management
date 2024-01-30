package com.zerp.taskmanagement.myenum;

import com.zerp.taskmanagement.customexception.InvalidInputException;

public enum Priority {
    HIGH("1"),
    MEDIUM("2"),
    LOW("3");

    private final String stringValue;

    Priority(String stringValue) {
        this.stringValue = stringValue;

    }

    public String getStringValue() {
        return stringValue;
    }

    public static Priority fromString(String stringValue) {
        for (Priority priority : Priority.values()) {
            if (priority.stringValue.equals(stringValue)) {
                return priority;
            }
        }
        throw new InvalidInputException("Invalid priority id : " + stringValue);
    }

}
