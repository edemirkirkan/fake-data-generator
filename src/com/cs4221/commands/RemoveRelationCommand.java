package com.cs4221.commands;

import com.cs4221.database.Database;

public class RemoveRelationCommand implements Command{
    private final String relationName;

    public RemoveRelationCommand(String relationName) {
        this.relationName = relationName;
    }

    @Override
    public void execute(Database db) {
        db.removeRelation(relationName);
    }

    @Override
    public boolean isQuit() {
        return false;
    }
}
