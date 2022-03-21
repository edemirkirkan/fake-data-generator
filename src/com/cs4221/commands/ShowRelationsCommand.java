package com.cs4221.commands;

import com.cs4221.database.Database;

public class ShowRelationsCommand implements Command{
    @Override
    public void execute(Database db) {
        db.showRelations();
    }

    @Override
    public boolean isQuit() {
        return false;
    }
}
