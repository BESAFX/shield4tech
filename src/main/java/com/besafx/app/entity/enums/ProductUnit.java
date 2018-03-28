package com.besafx.app.entity.enums;

public enum ProductUnit {
    Piece("حبة"),
    Meter("متر"),
    Suit("طقم"),
    Coil("لفة");
    private String name;

    ProductUnit(String name) {
        this.name = name;
    }

    public static ProductUnit findByName(String name) {
        for (ProductUnit v : values()) {
            if (v.getName().equals(name)) {
                return v;
            }
        }
        return null;
    }

    public static ProductUnit findByValue(String value) {
        for (ProductUnit v : values()) {
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
