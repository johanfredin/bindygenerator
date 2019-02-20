package com.github.johanfredin.bindygenerator;

public class FieldType implements Comparable<FieldType>{

    private int priority;
    private String type;

    public FieldType(int priority, String type) {
        this.priority = priority;
        this.type = type;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
