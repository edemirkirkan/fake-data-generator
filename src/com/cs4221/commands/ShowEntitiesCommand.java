package com.cs4221.commands;

import com.cs4221.database.Database;

public class ShowEntitiesCommand implements Command{
    @Override
    public void execute(Database db) {
        db.showEntities();
    }

    @Override
    public boolean isQuit() {
        return false;
    }
}
