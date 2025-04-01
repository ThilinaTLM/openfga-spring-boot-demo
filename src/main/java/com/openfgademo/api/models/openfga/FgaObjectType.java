package com.openfgademo.api.models.openfga;

public enum FgaObjectType {
    USER("user"),
    GROUP("group"),
    DOCUMENT("document");

    private final String value;

    FgaObjectType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}