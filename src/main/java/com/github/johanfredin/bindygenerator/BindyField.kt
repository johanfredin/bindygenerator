package com.github.johanfredin.bindygenerator

/**
 * Class representing a bindy field and its annotation.
 * Holds all the data to generate the following in a java file:<br></br><br></br>
 * &#64;DataField(pos=1, columnName=COLUMN_Name)
 * @property pos the pos argument representing the column index for the field in the datasource file.
 * @property type The objectTypeName of the field. Can be one of {String, Float, Integer}
 * @property dataSourceName The name of the field in the datasource
 * @property javaFieldName Set the name of the java variable based on the datasource field name.
 */
data class BindyField(val pos: Int,
                      val dataSourceName: String,
                      val javaFieldName: String,
                      var type: FieldType)