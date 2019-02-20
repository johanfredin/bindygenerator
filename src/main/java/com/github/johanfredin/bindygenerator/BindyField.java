package com.github.johanfredin.bindygenerator;

public class BindyField {

    private int pos;
    private String type;
    private String dataSourceName;
    private String javaFieldName;

    public BindyField(){}

    public BindyField(String dataSourceName, String type, int pos, String javaFieldName) {
        this.dataSourceName = dataSourceName;
        this.type = type;
        this.pos = pos;
        this.javaFieldName = javaFieldName;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public String getJavaFieldName() {
        return javaFieldName;
    }

    public void setJavaFieldName(String javaFieldName) {
        this.javaFieldName = javaFieldName;
    }

    @Override
    public String toString() {
        return "BindyField{" +
                "pos=" + pos +
                ", type='" + type + '\'' +
                ", dataSourceName='" + dataSourceName + '\'' +
                ", javaFieldName='" + javaFieldName + '\'' +
                '}';
    }
}
