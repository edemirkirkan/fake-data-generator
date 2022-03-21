package com.cs4221.commands;

import com.cs4221.database.Database;

public class ShowDiagramCommand implements Command{
    @Override
    public void execute(Database db) {
        db.showDiagram();
    }

    @Override
    public boolean isQuit() {
        return false;
    }
}
