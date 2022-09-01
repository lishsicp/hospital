package com.yaroslav.lobur.exceptions;

public class UnknownSqlException extends RuntimeException {

    public UnknownSqlException(String message) {
        super(message);
    }

    public UnknownSqlException(String message, Throwable cause) {
        super(message, cause);
    }
}