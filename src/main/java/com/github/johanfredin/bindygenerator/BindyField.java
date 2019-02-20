package com.github.johanfredin.bindygenerator;

/**
 * Class representing a bindy field and its annotation.
 * Holds all the data to generate the following in a java file:<br><br>
 * &#64;DataField(pos=1, columnName=COLUMN_Name)
 * private String columnName;
 *
 */
public class BindyField {

    private int pos;
    private String type;
    private String dataSourceName;
    private String javaFieldName;

    /**
     * The name of the field in the datasource
     * @return the name of the field in the datasource
     */
    String getDataSourceName() {
        return dataSourceName;
    }

    /**
     * Set the name of the field in the datasource
     * @param dataSourceName the name of the field in the datasource
     */
    void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    /**
     * The type of the field. Can be one of {String, Float, Integer}
     * @return the type of the field
     */
    String getType() {
        return type;
    }

    /**
     * Set the type of the field
     * @param type the type of the field
     */
    void setType(String type) {
        this.type = type;
    }

    /**
     * Get the pos argument representing the column index for the field in the datasource file.
     * @return the position in the datasource file this field is from.
     */
    int getPos() {
        return pos;
    }

    /**
     * Set the pos argument representing the column index for the field in the datasource file.
     * @param pos the pos argument.
     */
    void setPos(int pos) {
        this.pos = pos;
    }

    /**
     * The name of the java variable (e.g private String <b>variableName</b>)
     * the datasource field should have.
     * @return the variable name as is or by java naming convention.
     */
    String getJavaFieldName() {
        return javaFieldName;
    }

    /**
     * Set the name of the java variable based on the datasource field name.
     * @param javaFieldName the variable name as is or by java naming convention.
     */
    void setJavaFieldName(String javaFieldName) {
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
