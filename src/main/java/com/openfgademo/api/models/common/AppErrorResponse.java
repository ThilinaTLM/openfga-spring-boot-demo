package com.openfgademo.api.models.common;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppErrorResponse {
    private final LocalDateTime timestamp;
    private final String message;
    private final List<String> errorList;
    private final Map<String, String> errorMap;
    private final String internalCode;
}
