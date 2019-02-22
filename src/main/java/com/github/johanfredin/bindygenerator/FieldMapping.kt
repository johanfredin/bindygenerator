package com.github.johanfredin.bindygenerator

/**
 * Enum representing user input regarding naming standard for the datasource
 * fields in the resulting java source file.
 */
enum class FieldMapping(private val mapping: String) {

    /**
     * Java variable name should be the same as it was in the datasource.
     * Quote chars (if any) will be removed however.
     */
    AS_IS("asIs"),

    /**
     * Java naming convention should be used for the field e.g lowerCamelCase.
     */
    LOWER_CAMEL_CASE("lowerCamelCase");


    companion object {

        /**
         * Fetch an entry matching on passed in entry
         * @param mapping the mapping to match with an entry
         * @return the mathins entry or [.AS_IS] by default.
         */
        fun getByMapping(mapping: String): FieldMapping {
            return if (mapping.equals("lowerCamelCase", ignoreCase = true)) LOWER_CAMEL_CASE else AS_IS
        }
    }
}
