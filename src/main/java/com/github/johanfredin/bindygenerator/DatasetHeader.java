package com.github.johanfredin.bindygenerator;

public class DatasetHeader {

    private final String dataSourceName;
    private final String javaFieldName;

    public DatasetHeader(String dataSourceName, String javaFieldName) {
        this.dataSourceName = dataSourceName;
        this.javaFieldName = javaFieldName;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public String getJavaFieldName() {
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
