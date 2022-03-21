package com.cs4221.commands;

import com.cs4221.database.Database;

public class HelpCommand implements Command {
    @Override
    public void execute(Database db) {
        final String SEPARATOR = "-----------------------------------------------------" +
                "---------------------------------------------------";
        System.out.println(SEPARATOR + "\nKeep in mind that all of the commands and keywords are " +
                "case insensitive i.e help, HELP both can be used\n" + SEPARATOR + "\nCREATE ENTITY " +
                "COMMAND\nFormat\nCREATE ENTITY {entity_name} " + "WITH {attribute_name} " +
                "{attribute_type}" + ",{attribute2_name} {attribute2_type},\n... " +
                "PRIMARY KEY {attribute_name}, " + "{attribute1_name}, ...\nUsage\nCREATE ENTITY student " +
                "WITH s_id UUID, s_name VARCHAR(20), s_age INT PRIMARY KEY s_id\n" + SEPARATOR +
                "\nREMOVE ENTITY COMMAND\nFormat\nREMOVE ENTITY {entity_name}\nUsage\nREMOVE ENTITY " +
                "student\n" + SEPARATOR + "\nREMOVE RELATION COMMAND\nFormat\nREMOVE RELATION " +
                "{relation_name}" + "\nUsage\nREMOVE RELATION " + "student-teacher\n" + SEPARATOR +
                "\nSHOW ENTITIES COMMAND\nFormat/Usage\nSHOW ENTITIES\n" + SEPARATOR + "\nSHOW " +
                "RELATIONS COMMAND\nFormat/Usage\nSHOW RELATIONS\n" + SEPARATOR + "\nSHOW DIAGRAM " +
                "COMMAND\nFormat/Usage\nSHOW DIAGRAM\n" + SEPARATOR + "\nEXIT COMMAND\nFormat/Usage" +
                "\nEXIT\n" + SEPARATOR);
    }

    @Override
    public boolean isQuit() {
        return false;
    }
}
