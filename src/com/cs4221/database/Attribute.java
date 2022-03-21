package com.cs4221.database;

public class Attribute {
    private final String name;
    private final String type;
    private boolean isKey;

    public Attribute(String name, String type) {
        this.name = name;
        this.type = type;
        this.isKey = false;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean getKey() {
        return isKey;
    }

    public void setKey() {
        this.isKey = true;
    }

    public String toString() {
        return isKey ? name + "(KEY) " + type : name + " " + type;
    }
}
