package com.github.johanfredin.bindygenerator;

/**
 * Enum representing user input regarding naming standard for the datasource
 * fields in the resulting java source file.
 */
public enum FieldMapping {

    /**
     * Java variable name should be the same as it was in the datasource.
     * Quote chars (if any) will be removed however.
     */
    AS_IS("asIs"),

    /**
     * Java naming convention should be used for the field e.g lowerCamelCase.
     */
    LOWER_CAMEL_CASE("lowerCamelCase");

    private String mapping;

    FieldMapping(String mapping) {
        this.mapping = mapping;
    }

    /**
     * Fetch an entry matching on passed in entry
     * @param mapping the mapping to match with an entry
     * @return the mathins entry or {@link #AS_IS} by default.
     */
    public static FieldMapping getByMapping(String mapping) {
        return mapping.equalsIgnoreCase("lowerCamelCase") ? LOWER_CAMEL_CASE : AS_IS;
    }
}
