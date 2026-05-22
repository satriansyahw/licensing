package com.belajar.task_api.application.common;

import lombok.Getter;

import java.util.Map;

@Getter
public class Result<T> {
    private final boolean success;
    private final String message;
    private  final T data;

    public Result(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
    public static <T> Result<T> success(T data) {
        return new Result<>(true, "Operation successful", data);
    }

    public static <T> Result<T> success(T data, String message) {
        return new Result<>(true, message, data);
    }

    public static <T> Result<T> failure(String message) {
        return new Result<>(false, message, null);
    }
}
