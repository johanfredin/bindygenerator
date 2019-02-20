package com.github.johanfredin.bindygenerator;

/**
 * Used when wanting numeric fields in resulting java class.
 * Holds a type and the priority of the type. E.g if field in
 * datasource is numeric on one line and a string on the next
 * then String will be used for the variable etc.
 */
public class FieldType implements Comparable<FieldType>{

    private int priority;
    private String type;

    /**
     * Create a new instance
     * @param priority the priority of the variable type
     * @param type the variable type
     */
    FieldType(int priority, String type) {
        this.priority = priority;
        this.type = type;
    }

    /**
     * Get the type
     * @return the type of the variable
     */
    String getType() {
        return type;
    }

    @Override
    public int compareTo(FieldType fieldType) {
        if(this.priority > fieldType.priority) {
            return 1;
        } else if(this.priority < fieldType.priority) {
            return -1;
        }
        return 0;
    }
}
