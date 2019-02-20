package com.github.johanfredin.bindygenerator;

/**
 * The configuration of the generator
 */
public class GeneratorConfig {

    private final String delimiter;
    private final String javaClassName;
    private final FieldMapping fieldMapping;
    private final boolean isHeader;
    private final boolean useNumericFieldTypes;
    private final boolean usePrimitiveTypesWherePossible;
    private final boolean includeColumnName;

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
    GeneratorConfig(String delimiter, String javaClassName,
                           FieldMapping fieldMapping, boolean isHeader,
                           boolean useNumericFieldTypes, boolean usePrimitiveTypesWherePossible,
                           boolean includeColumnName) {
        this.delimiter = delimiter;
        this.javaClassName = javaClassName;
        this.fieldMapping = fieldMapping;
        this.isHeader = isHeader;
        this.useNumericFieldTypes = useNumericFieldTypes;
        this.usePrimitiveTypesWherePossible = usePrimitiveTypesWherePossible;
        this.includeColumnName = includeColumnName;
    }

    /**
     * @return the field delimiter used in the datasource file
     */
    String getDelimiter() {
        return delimiter;
    }

    /**
     * @return whether or not a header record is present (typically the first row in the file)
     */
    boolean isHeader() {
        return isHeader;
    }

    /**
     * @return whether or not to try and map numeric and decimal field types to Float and/or Integer
     */
    boolean isUseNumericFieldTypes() {
        return useNumericFieldTypes;
    }

    /**
     * @return the name of the java class that will represent the datasource file
     */
    String getJavaClassName() {
        return javaClassName;
    }

    /**
     * @return what mapping convention to use for the field names when turning them into java fields
     */
    FieldMapping getFieldMapping() {
        return fieldMapping;
    }

    /**
     * @return whether or not to use primitive types (int) instead of Objects (Integer)
     */
    boolean isUsePrimitiveTypesWherePossible() {
        return usePrimitiveTypesWherePossible;
    }

    /**
     * @return whether or not to include the columnName argument into the bindy &#64;DataField annotation
     */
    boolean isIncludeColumnName() {
        return includeColumnName;
    }

    @Override
    public String toString() {
        return "GeneratorConfig{" +
                "delimiter='" + delimiter + '\'' +
                ", javaClassName='" + javaClassName + '\'' +
                ", fieldMapping=" + fieldMapping +
                ", isHeader=" + isHeader +
                ", useNumericFieldTypes=" + useNumericFieldTypes +
                ", usePrimitiveTypesWherePossible=" + usePrimitiveTypesWherePossible +
                ", includeColumnName=" + includeColumnName +
                '}';
    }
}
