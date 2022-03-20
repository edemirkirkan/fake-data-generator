package com.cs4221.database;

import java.util.ArrayList;

public class Database {
    ArrayList<Table> tables;

    public Database() {
        tables = new ArrayList<>();
    }

    public void addTable(Table table) {
        tables.add(table);
        if (table instanceof Entity) {
            System.out.println("Entity is successfully created");
        }
        else {
            System.out.println("Relation is successfully created");
        }
    }

    public void printEntities() {
        for (Table table : tables) {
            if (table instanceof Entity) {
                System.out.println(table);
            }
        }
    }

    public void printRelations() {
        for (Table table : tables) {
            if (table instanceof Relation) {
                System.out.println(table);
            }
        }
    }
}
