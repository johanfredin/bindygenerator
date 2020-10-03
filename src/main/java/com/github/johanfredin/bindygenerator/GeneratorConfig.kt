package com.github.johanfredin.bindygenerator

/**
 * The configuration of the generator
 * @property delimiter the field delimiter used in the datasource file
 * @property javaClassName the name of the java class that will represent the datasource file
 * @property fieldMapping what mapping convention to use for the field names when turning them into java fields
 * @property isHeader whether or not a header record is present (typically the first row in the file)
 * @property integerType the field type to use for integers (defaults to string)
 * @property decimalType the field type to use for decimals (defaults to string)
 * @property stringType the field type to use for strings (defaults to string)
 * @property isIncludeColumnName whether or not to include the columnName argument into the bindy &#64;DataField annotation
 *
 */
data class GeneratorConfig(val delimiter: String,
                           val javaClassName: String,
                           val fieldMapping: FieldMapping,
                           val isHeader: Boolean,
                           val isIncludeColumnName: Boolean,
                           val integerType: IntegerType,
                           val decimalType: DecimalType,
                           val stringType: `StringType.java`)

