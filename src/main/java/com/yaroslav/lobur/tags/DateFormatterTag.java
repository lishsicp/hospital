package com.yaroslav.lobur.tags;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

import java.io.IOException;
import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateFormatterTag extends SimpleTagSupport {

    private Date date;
    private String locale;

    public void setDate(Date date) {
        this.date = date;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public void doTag() throws JspException, IOException {
        DateTimeFormatter dateTimeFormatter;
        if (date == null) return;
        if ("en-US".equals(locale)) {
            dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        } else {
            dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        }
        getJspContext().getOut().write(dateTimeFormatter.format(date.toLocalDate()));
    }
}
