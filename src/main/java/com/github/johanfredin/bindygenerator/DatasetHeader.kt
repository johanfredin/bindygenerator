package com.github.johanfredin.bindygenerator

/**
 * Helper class for the [BindyGenerator] that is used when fetching the header record
 * fields. Holds the field as is and a modified java name (if [FieldMapping.AS_IS] then
 * the datasource name will be used as java field name)
 */
class DatasetHeader
/**
 * Create a new instance
 * @param dataSourceName the datasource name as is from the header records in the datasource file.
 * @param javaFieldName the altered datasource name to match java naming convention if chosen.
 */
internal constructor(
        /**
         * the datasource name as is from the header records in the datasource file.
         * @return the datasource name
         */
        internal val dataSourceName: String,
        /**
         * the altered datasource name to match java naming convention if chosen.
         * @return the altered datasource name
         */
        internal val javaFieldName: String) {

    override fun toString(): String {
        return "DatasetHeader{" +
                "dataSourceName='" + dataSourceName + '\''.toString() +
                ", javaFieldName='" + javaFieldName + '\''.toString() +
                '}'.toString()
    }
}
