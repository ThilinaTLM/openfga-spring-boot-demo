package com.openfgademo.api.models.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public enum FgaUserGroup {
    ADMIN("admin"),
    MANAGER("manager"),
    USER("user");

    private final String value;
}
