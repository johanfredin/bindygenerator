package com.github.johanfredin.bindygenerator

/**
 * The configuration of the generator
 */
class GeneratorConfig
/**
 * Create a new instance
 *
 * @param delimiter                      the field delimiter used in the datasource file
 * @param javaClassName                  the name of the java class that will represent the datasource file
 * @param fieldMapping                   what mapping convention to use for the field names when turning them into java fields
 * @param isHeader                       whether or not a header record is present (typically the first row in the file)
 * @param useNumericFieldTypes           whether or not to try and map numeric and decimal field types to Float and/or Integer
 * @param usePrimitiveTypesWherePossible whether or not to use primitive types (int) instead of Objects (Integer)
 * @param includeColumnName              whether or not to include the columnName argument into the bindy &#64;DataField annotation
 */
internal constructor(
        /**
         * @return the field delimiter used in the datasource file
         */
        internal val delimiter: String,
        /**
         * @return the name of the java class that will represent the datasource file
         */
        internal val javaClassName: String,
        /**
         * @return what mapping convention to use for the field names when turning them into java fields
         */
        internal val fieldMapping: FieldMapping,
        /**
         * @return whether or not a header record is present (typically the first row in the file)
         */
        internal val isHeader: Boolean,
        /**
         * @return whether or not to try and map numeric and decimal field types to Float and/or Integer
         */
        internal val isUseNumericFieldTypes: Boolean,
        /**
         * @return whether or not to use primitive types (int) instead of Objects (Integer)
         */
        internal val isUsePrimitiveTypesWherePossible: Boolean,
        /**
         * @return whether or not to include the columnName argument into the bindy &#64;DataField annotation
         */
        internal val isIncludeColumnName: Boolean) {

    override fun toString(): String {
        return "GeneratorConfig{" +
                "delimiter='" + delimiter + '\''.toString() +
                ", javaClassName='" + javaClassName + '\''.toString() +
                ", fieldMapping=" + fieldMapping +
                ", isHeader=" + isHeader +
                ", useNumericFieldTypes=" + isUseNumericFieldTypes +
                ", usePrimitiveTypesWherePossible=" + isUsePrimitiveTypesWherePossible +
                ", includeColumnName=" + isIncludeColumnName +
                '}'.toString()
    }
}
