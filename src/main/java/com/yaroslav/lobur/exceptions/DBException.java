package com.yaroslav.lobur.exceptions;

public class DBException extends RuntimeException {
    public DBException(String message) {
        super(message);
    }

    public DBException(Throwable cause) {
        super(cause);
    }

    public DBException(String message, Throwable cause) {
        super(cause);
    }
}
