package com.cs4221.commands;

import com.cs4221.database.Database;

public interface Command {
    void execute(Database db);
    boolean isQuit();
}
