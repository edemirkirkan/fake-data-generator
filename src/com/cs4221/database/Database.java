package com.cs4221.database;

import java.util.ArrayList;

public class Database {
    private final ArrayList<Table> tables;

    public Database() {
        tables = new ArrayList<>();
    }

    public void addTable(Table table) {
        if (!isUnique(table.getTableName())) {
            System.out.println("There is already a entity/relation in the system named '" + table.getTableName() +
                    "'\nConsider changing the name and try again" +
                    "\nAll diagram can be seen using SHOW DIAGRAM command.");
            return;
        }
        tables.add(table);
        String type = table instanceof Entity ? "Entity" : "Relation";
        System.out.println(type + " is successfully created");
    }

    public void removeEntity(String entityName) {
        for (Table t : tables) {
            if (t instanceof  Entity && t.getTableName().equalsIgnoreCase(entityName)) {
                tables.remove(t);
                System.out.println("Entity is successfully removed");
                return;
            }
        }
        System.out.println("There is no entity named '" + entityName + "' in the system");
    }

    public void removeRelation(String relationName) {
        for (Table t : tables) {
            if (t instanceof Relation && t.getTableName().equalsIgnoreCase(relationName)) {
                tables.remove(t);
                System.out.println("Relation is successfully removed");
                return;
            }
        }
        System.out.println("There is no relation named '" + relationName + "' in the system");
    }

    public void showEntities() {
        boolean found = false;
        for (Table table : tables) {
            if (table instanceof Entity) {
                found = true;
                System.out.println(table);
            }
        }
        if (!found) {
            System.out.println("Currently, there is no entity in the system");
        }
    }

    public void showRelations() {
        boolean found = false;
        for (Table table : tables) {
            if (table instanceof Relation) {
                found = true;
                System.out.println(table);
            }
        }
        if (!found) {
            System.out.println("Currently, there is no relation in the system");
        }
    }

    public void showDiagram() {
        for (Table table : tables) {
            System.out.println(table);
        }
    }

    private boolean isUnique(String tableName) {
        for (Table t : tables) {
            if (t.getTableName().equalsIgnoreCase(tableName)) {
                return false;
            }
        }
        return true;
    }
}
