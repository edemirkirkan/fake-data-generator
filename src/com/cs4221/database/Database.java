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
                    "\nAll tables/entities can be seen using SHOW DIAGRAM command.");
            return;
        }
        if (table instanceof Entity) {
            tables.add(table);
            System.out.println("Entity is successfully created");
        } else {
            Relation relation = (Relation) table;
            if (!validRelation(relation.getLeftTableName(), relation.getRightTableName())) {
                System.out.println("Relation cannot be created as related entities do not exist in the system" +
                        "\nAll entities can be seen using SHOW ENTITIES command.");
                return;
            }
            tables.add(table);
            System.out.println("Relation is successfully created");
        }
    }

    private boolean validRelation(String leftTableName, String rightTableName) {
        boolean leftFound = false;
        boolean rightFound = false;
        for (Table t : tables) {
            if (t instanceof Entity && t.getTableName().equalsIgnoreCase(leftTableName)) {
                leftFound = true;
            }
            if (t instanceof Entity && t.getTableName().equalsIgnoreCase(rightTableName)) {
                rightFound = true;
            }
        }
        return leftFound && rightFound;
    }

    public void removeEntity(String entityName) {
        for (Table t : tables) {
            if (t instanceof Entity && t.getTableName().equalsIgnoreCase(entityName)) {
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
