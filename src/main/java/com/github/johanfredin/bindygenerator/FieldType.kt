package com.github.johanfredin.bindygenerator

/**
 * Used when wanting numeric fields in resulting java class.
 * Holds a objectTypeName and the priority of the objectTypeName. E.g if field in
 * datasource is numeric on one line and a string on the next
 * then String will be used for the variable etc.
 * @property priority the priority of the variable objectTypeName
 */
enum class FieldType(val priority: Int) : Comparable<FieldType> {

    STRING(1),
    DECIMAL(2),
    INTEGER(3);

}
