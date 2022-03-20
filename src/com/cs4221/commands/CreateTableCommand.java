package com.cs4221.commands;

import com.cs4221.database.Attribute;
import com.cs4221.database.Database;
import com.cs4221.database.Entity;
import com.cs4221.database.Table;

import java.util.ArrayList;

public class CreateTableCommand implements Command {
    protected ArrayList<Attribute> attributes;
    protected String tableName;
    protected boolean isQuit;

    public CreateTableCommand(String tableName, ArrayList<Attribute> attributes) {
        this.attributes = attributes;
        this.tableName = tableName;
        this.isQuit = false;
    }

    @Override
    public void execute(Database db) {
        Table entity = new Entity(attributes, null);
        db.addTable(entity);
    }



    @Override
    public boolean isQuit() {
        return this.isQuit;
    }


}
