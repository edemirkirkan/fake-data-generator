package com.cs4221.database;

public class Attribute {
    protected String name;
    protected String type;
    protected boolean isKey;

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

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
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
