package com.cs4221.commands;

import com.cs4221.database.Database;

public class ExitCommand implements Command {
    @Override
    public void execute(Database db) {
        System.out.println("Goodbye...");
    }

    @Override
    public boolean isQuit() {
        return true;
    }
}
