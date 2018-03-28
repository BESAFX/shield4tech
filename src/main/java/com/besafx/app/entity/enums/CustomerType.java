package com.besafx.app.entity.enums;

public enum CustomerType {
    Individual("افراد"),
    Business("شركات");
    private String name;

    CustomerType(String name) {
        this.name = name;
    }

    public static CustomerType findByName(String name) {
        for (CustomerType v : values()) {
            if (v.getName().equals(name)) {
                return v;
            }
        }
        return null;
    }

    public static CustomerType findByValue(String value) {
        for (CustomerType v : values()) {
            if (v.name().equals(value)) {
                return v;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }
}
