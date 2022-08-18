package com.yaroslav.lobur.exceptions;

import java.util.List;

public class DBExceptionMessages extends  RuntimeException {

    private final List<String> errorMessages;

    public DBExceptionMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }
}
