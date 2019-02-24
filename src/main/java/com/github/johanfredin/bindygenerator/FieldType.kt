package com.github.johanfredin.bindygenerator

/**
 * Used when wanting numeric fields in resulting java class.
 * Holds a objectTypeName and the priority of the objectTypeName. E.g if field in
 * datasource is numeric on one line and a string on the next
 * then String will be used for the variable etc.
 * @property priority the priority of the variable objectTypeName
 * @property objectTypeName the variable objectTypeName
 */
enum class FieldType(val priority: Int, val objectTypeName: String, val primitiveTypeName: String) : Comparable<FieldType> {

    STRING(1, "String", "String"),
    FLOAT(2, "Float", "float"),
    INTEGER(3, "Integer", "int");

//        override fun compareTo(other: FieldType): Int {
//            if (this.priority > other.priority) {
//                return 1
//            } else if (this.priority < other.priority) {
//                return -1
//            }
//            return 0
//        }
}
