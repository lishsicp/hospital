package com.yaroslav.lobur.utils;

import java.util.ResourceBundle;

public class PagePathManager {
    private static final ResourceBundle rb = ResourceBundle.getBundle("page-path");

    private PagePathManager() {}

    public static String getProperty(String key) {
        return rb.getString(key);
    }
}
