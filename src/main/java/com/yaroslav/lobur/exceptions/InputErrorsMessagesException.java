package com.yaroslav.lobur.exceptions;

import java.util.Map;

public class InputErrorsMessagesException extends RuntimeException {

    private final Map<String, String> errors;

    public InputErrorsMessagesException(Map<String, String> errors) {
        this.errors = errors;
    }

    public Map<String, String> getErrorMessageMap() {
        return errors;
    }
}
