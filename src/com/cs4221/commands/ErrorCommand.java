package com.cs4221.commands;

import com.cs4221.database.Database;

public class ErrorCommand implements Command {
    private final String msg;

    public ErrorCommand(String msg) {
        this.msg = msg;
    }
    @Override
    public void execute(Database db) {
        System.out.println(msg);
    }

    @Override
    public boolean isQuit() {
        return false;
    }
}
