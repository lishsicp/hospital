package com.yaroslav.lobur.model.entity.enums;

public enum OrderBy {
    NAME("lastname"), DATE("date_of_birth"), CATEGORY("category"), NUMBER_OF_PATIENTS("numberOfPatients DESC");

    private final String field;

    OrderBy(String filed) {
        this.field = filed;
    }

    public String getField() {
        return field;
    }
}
