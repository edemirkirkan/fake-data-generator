package com.cs4221.commands;

import com.cs4221.database.Attribute;
import com.cs4221.database.Database;
import com.cs4221.database.Relation;
import com.cs4221.database.Table;

import java.util.ArrayList;

public class CreateRelationCommand implements Command {
    private final String relationName;
    private final String leftTableName;
    private final String leftTableMinConst;
    private final String leftTableMaxConst;
    private final String rightTableName;
    private final String rightTableMinConst;
    private final String rightTableMaxConst;
    private final ArrayList<Attribute> attributes;

    public CreateRelationCommand(String relationName, String leftTableName, String leftTableMinConst,
                                 String leftTableMaxConst, String rightTableName,
                                 String rightTableMinConst, String rightTableMaxConst, ArrayList<Attribute> attributes) {
        this.relationName = relationName;
        this.leftTableName = leftTableName;
        this.leftTableMinConst = leftTableMinConst;
        this.leftTableMaxConst = leftTableMaxConst;
        this.rightTableName = rightTableName;
        this.rightTableMinConst = rightTableMinConst;
        this.rightTableMaxConst = rightTableMaxConst;
        this.attributes = attributes;
    }

    @Override
    public void execute(Database db) {
        Table relation = new Relation(attributes, null, relationName, leftTableName, leftTableMinConst,
                leftTableMaxConst, rightTableName, rightTableMinConst, rightTableMaxConst);
        db.addTable(relation);
    }

    @Override
    public boolean isQuit() {
        return false;
    }
}
