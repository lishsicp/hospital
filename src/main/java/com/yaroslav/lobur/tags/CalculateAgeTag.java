package com.yaroslav.lobur.tags;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;

public class CalculateAgeTag extends SimpleTagSupport {

    private Date date;
    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public void doTag() throws JspException, IOException {
        if (date == null) return;
        int age = Period.between(date.toLocalDate(), LocalDate.now()).getYears();
        getJspContext().getOut().write(String.valueOf(age));
    }
}
