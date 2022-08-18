package com.yaroslav.lobur.utils;

import java.util.ResourceBundle;

public class RegexpManager {

    private static final ResourceBundle rb = ResourceBundle.getBundle("validation.user-regex");

    private RegexpManager() {}

    public static String getProperty(String key) {
        return rb.getString(key);
    }
}
