package com.yaroslav.lobur.utils;

import com.yaroslav.lobur.model.entity.enums.Role;

import java.util.*;

public class SecurityMapInitializer {

    private SecurityMapInitializer() {}

    private static Set<String> setRoleActions(Properties properties, Role role) {
        String[] values = properties.getProperty(role.name())
                .replace(System.lineSeparator(), "\\s").split("\\s");
        return new HashSet<>(Arrays.asList(values));
    }

    public static Map<Role, Set<String>> initialize(Properties properties) {
        Map<Role, Set<String>> map = new EnumMap<>(Role.class);
        map.put(Role.ADMIN, setRoleActions(properties, Role.ADMIN));
        map.put(Role.DOCTOR, setRoleActions(properties, Role.DOCTOR));
        map.put(Role.NURSE, setRoleActions(properties, Role.NURSE));
        return map;
    }

}
