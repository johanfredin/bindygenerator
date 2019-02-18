package com.github.johanfredin.bindygenerator;

public class BindyField {

    private int pos;
    private String type = String.class.getSimpleName();
    private String name;

    public BindyField(){}

    public BindyField(String name, String type, int pos) {
        this.name = name;
        this.type = type;
        this.pos = pos;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "BindyField{" +
                "pos=" + pos +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
