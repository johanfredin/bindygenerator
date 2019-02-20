package com.github.johanfredin.bindygenerator;

public class GeneratorConfig {

    private final String delimiter;
    private final String javaClassName;
    private final FieldMapping fieldMapping;
    private final boolean isHeader;
    private final boolean useNumericFieldTypes;
    private final boolean usePrimitiveTypesWherePossible;
    private final boolean includeColumnName;


    public GeneratorConfig(String delimiter, String javaClassName,
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

    public String getDelimiter() {
        return delimiter;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public boolean isUseNumericFieldTypes() {
        return useNumericFieldTypes;
    }

    public String getJavaClassName() {
        return javaClassName;
    }

    public FieldMapping getFieldMapping() {
        return fieldMapping;
    }

    public boolean isUsePrimitiveTypesWherePossible() {
        return usePrimitiveTypesWherePossible;
    }

    public boolean isIncludeColumnName() {
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
