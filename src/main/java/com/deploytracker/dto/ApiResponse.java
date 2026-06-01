package com.deploytracker.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Generic envelope for all API responses.
 * Ensures a consistent shape whether the call succeeds or fails.
 * <pre>
 * Success: { "data": {...}, "status": 200 }
 * Error:   { "error": "...", "status": 404 }
 * </pre>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final T data;
    private final String error;
    private final int status;

    private ApiResponse(T data, String error, int status) {
        this.data   = data;
        this.error  = error;
        this.status = status;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data, null, 200);
    }

    public static <T> ApiResponse<T> error(String message, int status) {
        return new ApiResponse<>(null, message, status);
    }

    public T getData()      { return data; }
    public String getError(){ return error; }
    public int getStatus()  { return status; }
}
