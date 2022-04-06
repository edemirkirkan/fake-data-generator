package com.cs4221.database;

import java.util.ArrayList;

public class Relation extends Table {
    private final String leftTableName;
    private final String leftTableParticipation;
    private final String rightTableName;
    private final String rightTableParticipation;
    private final String relationshipType;

    public Relation(ArrayList<Attribute> attributes, ArrayList<ArrayList<String>> data, String tableName,
                    String leftTableName, String leftTableMinConst, String leftTableMaxConst,
                    String rightTableName, String rightTableMinConst, String rightTableMaxConst) {
        super(attributes, data, tableName);
        final String partial = "Partial Participation";
        final String total = "Total Participation";
        this.leftTableName = leftTableName;
        this.rightTableName = rightTableName;
        this.leftTableParticipation = leftTableMinConst.equalsIgnoreCase("0") ? partial : total;
        this.rightTableParticipation = rightTableMinConst.equalsIgnoreCase("0") ? partial : total;
        if (leftTableMaxConst.equalsIgnoreCase("1") &&
                rightTableMaxConst.equalsIgnoreCase("*")) {
            relationshipType = "One-Many";
        } else if (leftTableMaxConst.equalsIgnoreCase("*") &&
                rightTableMaxConst.equalsIgnoreCase("1")) {
            relationshipType = "Many-One";
        } else if (leftTableMaxConst.equalsIgnoreCase("*") &&
                rightTableMaxConst.equalsIgnoreCase("*")) {
            relationshipType = "Many-Many";
        }else if (leftTableMaxConst.equalsIgnoreCase("1") &&
                rightTableMaxConst.equalsIgnoreCase("1")) {
            relationshipType = "One-One";
        }
        else {
            relationshipType = null;
        }
    }

    public String getLeftTableName() {
        return leftTableName;
    }

    public String getRightTableName() {
        return rightTableName;
    }

    public String getLeftTableParticipation() {
        return leftTableParticipation;
    }

    public String getRightTableParticipation() {
        return rightTableParticipation;
    }

    public String getRelationshipType() {
        return relationshipType;
    }
}
