package com.kennen.template4j.enums;

public enum LiveStyles {
    TITLE("Title"), CAPTION("Caption")
    ;

    private final String value;

    LiveStyles(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
