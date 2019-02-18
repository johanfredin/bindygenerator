package com.github.johanfredin.bindygenerator;

public class GeneratorConfig {

    private String delimiter = ",";
    private boolean isHeader = true;
    private boolean useNumericFieldTypes;

    public GeneratorConfig() {
    }

    public GeneratorConfig(String delimiter, boolean isHeader, boolean useNumericFieldTypes) {
        this.delimiter = delimiter;
        this.isHeader = isHeader;
        this.useNumericFieldTypes = useNumericFieldTypes;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public boolean isUseNumericFieldTypes() {
        return useNumericFieldTypes;
    }

    public void setUseNumericFieldTypes(boolean useNumericFieldTypes) {
        this.useNumericFieldTypes = useNumericFieldTypes;
    }
}
