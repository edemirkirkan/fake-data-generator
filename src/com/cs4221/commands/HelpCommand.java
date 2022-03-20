package com.cs4221.commands;

import com.cs4221.database.Database;

public class HelpCommand implements Command {
    protected boolean isQuit;
    final protected String separator = "-----------------------------------------------------------------------------------------" +
            "-----------------------------------------------------------------------------------" +
            "---------------------------------------------------------------------------------";

    public HelpCommand() {
        this.isQuit = false;
    }
    @Override
    public void execute(Database db) {
        System.out.println(separator + "\nCREATE ENTITY COMMAND\nFormat\nCREATE ENTITY {entity_name} " +
                "WITH {attribute_name} {attribute_type},{attribute2_name} {attribute2_type}, ... " +
                "PRIMARY KEY {attribute_name}," +
                " {attribute1_name}, ...\nUsage\nCREATE ENTITY student WITH s_id UUID, s_name VARCHAR(20), " +
                "s_age INT PRIMARY KEY s_id\n" + separator + "\nEXIT COMMAND\nFormat\nexit | Exit | EXIT" +
                "\nUsage\nEXIT\n" + separator);
    }

    @Override
    public boolean isQuit() {
        return this.isQuit;
    }
}
