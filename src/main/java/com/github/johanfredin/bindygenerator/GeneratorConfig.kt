package com.github.johanfredin.bindygenerator

/**
 * The configuration of the generator
 * @property delimiter the field delimiter used in the datasource file
 * @property javaClassName the name of the java class that will represent the datasource file
 * @property fieldMapping what mapping convention to use for the field names when turning them into java fields
 * @property isHeader whether or not a header record is present (typically the first row in the file)
 * @property isUseNumericFieldTypes whether or not to try and map numeric and decimal field types to Float and/or Integer
 * @property isUsePrimitiveTypesWherePossible whether or not to use primitive types (int) instead of Objects (Integer)
 * @property isIncludeColumnName whether or not to include the columnName argument into the bindy &#64;DataField annotation
 *
 */
data class GeneratorConfig(val delimiter: String, val javaClassName: String,
                           val fieldMapping: FieldMapping, val isHeader: Boolean,
                           val isUseNumericFieldTypes: Boolean,
                           val isUsePrimitiveTypesWherePossible: Boolean,
                           val isIncludeColumnName: Boolean)
