package com.openfgademo.api.models.common;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class AppErrorResponse {
    private final LocalDateTime timestamp;
    private final String message;
    private final List<String> errorList;
    private final Map<String, String> errorMap;
    private final String internalCode;
}
