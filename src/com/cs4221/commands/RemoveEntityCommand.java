package com.cs4221.commands;

import com.cs4221.database.Database;

public class RemoveEntityCommand implements Command{
    private final String entityName;

    public RemoveEntityCommand(String entityName) {
        this.entityName = entityName;
    }

    @Override
    public void execute(Database db) {
        db.removeEntity(entityName);
    }

    @Override
    public boolean isQuit() {
        return false;
    }
}
