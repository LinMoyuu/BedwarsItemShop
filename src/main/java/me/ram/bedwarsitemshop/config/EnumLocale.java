package me.ram.bedwarsitemshop.config;

import lombok.Getter;

@Getter
public enum EnumLocale {
    ZH_CN("zh_CN");

    private final String name;

    EnumLocale(String name) {
        this.name = name;
    }

    public static EnumLocale getByName(String n) {
        for (EnumLocale type : values()) {
            if (type.getName().equals(n)) {
                return type;
            }
        }
        return null;
    }
}
