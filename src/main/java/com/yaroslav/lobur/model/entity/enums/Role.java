package com.yaroslav.lobur.model.entity.enums;

import java.util.HashMap;
import java.util.Map;

public enum Role {

    ADMIN(1), DOCTOR(2), NURSE(3);

    final Integer roleId;

    Role(int roleId) {
        this.roleId = roleId;
    }

    private static final Map<Integer, Role> byId = new HashMap<>();

    static {
        for (Role e : Role.values()) {
            if (byId.put(e.getRoleId(), e) != null) {
                throw new IllegalArgumentException("duplicate id: " + e.getRoleId());
            }
        }
    }

    public static Role getById(int id) {
        return byId.get(id);
    }

    public int getRoleId() {
        return roleId;
    }
}
