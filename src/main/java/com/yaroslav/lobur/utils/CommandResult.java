package com.yaroslav.lobur.utils;

public class CommandResult {
    private final String url;
    private final boolean redirect;

    public CommandResult(String url) {
        this.url = url;
        this.redirect = false;
    }

    public CommandResult(String url, boolean redirect) {
        this.url = url;
        this.redirect = redirect;
    }

    public boolean isRedirect() {
        return redirect;
    }

    public String getUrl() {
        return url;
    }

}







