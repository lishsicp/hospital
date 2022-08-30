package com.yaroslav.lobur.utils.managers;

import java.util.ResourceBundle;

public class ValidationManager {

    private static final ResourceBundle rb = ResourceBundle.getBundle("validation.validation");

    private ValidationManager() {}

    public static String getProperty(String key) {
        return rb.getString(key);
    }

}
