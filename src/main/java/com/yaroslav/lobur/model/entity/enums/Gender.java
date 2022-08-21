package com.yaroslav.lobur.model.entity.enums;



public enum Gender {
    MALE, FEMALE, OTHER;

    @Override
    public String toString() {
        return this.name();
    }
}
