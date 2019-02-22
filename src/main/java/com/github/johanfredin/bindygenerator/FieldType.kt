package com.github.johanfredin.bindygenerator

/**
 * Used when wanting numeric fields in resulting java class.
 * Holds a type and the priority of the type. E.g if field in
 * datasource is numeric on one line and a string on the next
 * then String will be used for the variable etc.
 */
class FieldType
/**
 * Create a new instance
 * @param priority the priority of the variable type
 * @param type the variable type
 */
internal constructor(private val priority: Int,
                     /**
                      * Get the type
                      * @return the type of the variable
                      */
                     internal val type: String) : Comparable<FieldType> {

    override fun compareTo(fieldType: FieldType): Int {
        if (this.priority > fieldType.priority) {
            return 1
        } else if (this.priority < fieldType.priority) {
            return -1
        }
        return 0
    }
}
