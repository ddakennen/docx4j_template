package com.kennen.template4j.enums;

public enum FileTypes {
    DOCX(".docx"), XLSX(".xlsx"), PDF(".pdf");


    private final String value;
    FileTypes(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
