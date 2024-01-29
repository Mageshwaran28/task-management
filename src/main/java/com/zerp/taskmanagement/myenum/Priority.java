package com.zerp.taskmanagement.myenum;


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
        return null;
    }
}


