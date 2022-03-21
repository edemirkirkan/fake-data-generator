package com.cs4221.commands;

import com.cs4221.database.Attribute;
import com.cs4221.database.Database;
import com.cs4221.database.Entity;
import com.cs4221.database.Table;

import java.util.ArrayList;

public class CreateEntityCommand implements Command {
    private final ArrayList<Attribute> attributes;
    private final String tableName;

    public CreateEntityCommand(String tableName, ArrayList<Attribute> attributes) {
        this.attributes = attributes;
        this.tableName = tableName;
    }

    @Override
    public void execute(Database db) {
        Table entity = new Entity(attributes, null, tableName);
        db.addTable(entity);
    }

    @Override
    public boolean isQuit() {
        return false;
    }


}
