package com.cs4221.database;

import java.util.ArrayList;

public class Entity extends Table {
    public Entity(ArrayList<Attribute> attributes, ArrayList<ArrayList<String>> data) {
        super(attributes, data);
    }
}