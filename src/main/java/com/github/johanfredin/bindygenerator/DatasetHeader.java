package com.github.johanfredin.bindygenerator;

/**
 * Helper class for the {@link BindyGenerator} that is used when fetching the header record
 * fields. Holds the field as is and a modified java name (if {@link FieldMapping#AS_IS} then
 * the datasource name will be used as java field name)
 */
public class DatasetHeader {

    private final String dataSourceName;
    private final String javaFieldName;

    /**
     * Create a new instance
     * @param dataSourceName the datasource name as is from the header records in the datasource file.
     * @param javaFieldName the altered datasource name to match java naming convention if chosen.
     */
    DatasetHeader(String dataSourceName, String javaFieldName) {
        this.dataSourceName = dataSourceName;
        this.javaFieldName = javaFieldName;
    }

    /**
     * the datasource name as is from the header records in the datasource file.
     * @return the datasource name
     */
    String getDataSourceName() {
        return dataSourceName;
    }

    /**
     * the altered datasource name to match java naming convention if chosen.
     * @return the altered datasource name
     */
    String getJavaFieldName() {
        return javaFieldName;
    }

    @Override
    public String toString() {
        return "DatasetHeader{" +
                "dataSourceName='" + dataSourceName + '\'' +
                ", javaFieldName='" + javaFieldName + '\'' +
                '}';
    }
}
