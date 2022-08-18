package com.yaroslav.lobur.model.entity.enums;

import java.util.Locale;

public enum Gender {
    MALE, FEMALE, OTHER;
//    MALE("male"), FEMALE("female"), OTHER("other");
//
//    final String name;
//
//    Gender(String gender) {
//        this.name = gender;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    @Override
//    public String toString() {
//        return "Gender{" +
//                "name='" + name + '\'' +
//                '}';
//    }
    @Override
    public String toString() {
        return this.name();
    }
}
