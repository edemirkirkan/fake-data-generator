package com.cs4221.commands;

import com.cs4221.database.Database;

public class ErrorCommand implements Command {
    protected String msg;
    protected boolean isQuit;

    public ErrorCommand(String msg) {
        this.msg = msg;
        this.isQuit = false;
    }
    @Override
    public void execute(Database db) {
        System.out.println(msg);
    }

    @Override
    public boolean isQuit() {
        return this.isQuit;
    }
}
