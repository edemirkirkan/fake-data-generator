package com.cs4221.database;

import java.util.ArrayList;

public class Relation extends Table {
    public Relation(ArrayList<Attribute> attributes, ArrayList<ArrayList<String>> data, String tableName) {
        super(attributes, data, tableName);
    }
}
