package com.github.johanfredin.bindygenerator

/**
 * Used when wanting numeric fields in resulting java class.
 * Holds a type and the priority of the type. E.g if field in
 * datasource is numeric on one line and a string on the next
 * then String will be used for the variable etc.
 * @property priority the priority of the variable type
 * @property type the variable type
 */
data class FieldType(val priority: Int, val type: String) : Comparable<FieldType> {
    override fun compareTo(other: FieldType): Int {
        if (this.priority > other.priority) {
            return 1
        } else if (this.priority < other.priority) {
            return -1
        }
        return 0
    }
}
