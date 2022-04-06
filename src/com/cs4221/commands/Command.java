package com.cs4221.commands;

import com.cs4221.database.Database;

import java.io.IOException;

public interface Command {
    void execute(Database db) throws IOException, NoSuchMethodException;

    boolean isQuit();

}
