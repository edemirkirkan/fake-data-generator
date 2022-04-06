package com.cs4221.commands;

import com.cs4221.database.Database;

import java.io.IOException;

public class GenerateDataCommand implements Command {
    private final int rowCount;

    public GenerateDataCommand(int rowCount) {
        this.rowCount = rowCount;
    }

    @Override
    public void execute(Database db) throws IOException, NoSuchMethodException {
        db.generateData(rowCount);
        System.out.println("Your csv files are generated. You can find them on the output folder\nGoodbye...");
    }

    @Override
    public boolean isQuit() {
        return true;
    }
}
