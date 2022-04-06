package com.cs4221.commands;

import com.cs4221.database.Database;
import com.cs4221.database.MultipleColumnDistribution;

import java.io.IOException;

public class MultipleColumnDistributionCommand implements Command {
    private final MultipleColumnDistribution dist;

    public MultipleColumnDistributionCommand(String tableName1, String attributeName1, String tableName2,
                                             String attributeName2, String type, String param1, String param2) {
        dist = new MultipleColumnDistribution(tableName1, attributeName1, tableName2, attributeName2,
                type, param1, param2);
    }

    @Override
    public void execute(Database db) throws IOException, NoSuchMethodException {
        db.addMultipleColumnDistribution(dist);
    }

    @Override
    public boolean isQuit() {
        return false;
    }
}
