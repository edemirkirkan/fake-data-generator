package com.cs4221.database;

import com.cs4221.utility.UI;

import java.util.ArrayList;
import java.util.RandomAccess;

public class Table {
    private final String tableName;
    private final ArrayList<Attribute> attributes;
    private final ArrayList<ArrayList<String>> data;

    public Table(ArrayList<Attribute> attributes, ArrayList<ArrayList<String>> data, String tableName) {
        this.attributes = attributes;
        this.data = data;
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public ArrayList<Attribute> getTableAttributes() {
        return attributes;
    }

    public String toString() {
        UI ui = new UI();
        ArrayList<String> attributeNames = new ArrayList<>();
        ArrayList<String> attributeTypes = new ArrayList<>();
        ArrayList<Boolean> isKey = new ArrayList<>();
        if (attributes != null) {
            for (Attribute att : attributes) {
                attributeNames.add(att.getName());
                attributeTypes.add(att.getType());
                isKey.add(att.getKey());
            }
        }
        String tableType = getClass().getSimpleName().toUpperCase();
        if (this instanceof Entity) {
            return ui.displayEntity(attributeNames, data, attributeTypes, isKey, tableName, tableType);
        } else {
            Relation relation = (Relation) this;
            return ui.displayRelation(attributeNames, attributeTypes, tableName, relation.getLeftTableName(), relation.getRightTableName(), relation.getLeftTableParticipation(), relation.getRightTableParticipation(), relation.getRelationshipType());
        }

    }

}
