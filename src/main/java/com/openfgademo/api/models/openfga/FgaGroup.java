package com.openfgademo.api.models.openfga;

public enum FgaGroup {
    ADMIN("admin"),
    EDITOR("editor"),
    VIEWER("viewer");

    private final String value;

    FgaGroup(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
