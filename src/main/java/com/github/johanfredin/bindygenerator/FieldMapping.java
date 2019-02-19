package com.github.johanfredin.bindygenerator;

public enum FieldMapping {

    AS_IS("asIs"),
    LOWER_CAMEL_CASE("lowerCamelCase");

    private String mapping;

    FieldMapping(String mapping) {
        this.mapping = mapping;
    }

    public static FieldMapping getByMapping(String mapping) {
        return mapping.equalsIgnoreCase("lowerCamelCase") ? LOWER_CAMEL_CASE : AS_IS;
    }
}
