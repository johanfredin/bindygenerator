package com.github.johanfredin.bindygenerator

/**
 * Helper class for the [BindyGenerator] that is used when fetching the header record
 * fields. Holds the field as is and a modified java name (if [FieldMapping.AS_IS] then
 * the datasource name will be used as java field name)
 * @property dataSourceName the datasource name as is from the header records in the datasource file.
 * @property javaFieldName the altered datasource name to match java naming convention if chosen.
 */
data class DatasetHeader(val dataSourceName: String, val javaFieldName: String)
