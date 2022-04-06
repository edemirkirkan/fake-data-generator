package com.cs4221.commands;

import com.cs4221.database.Database;
import com.cs4221.database.SingleColumnDistribution;

import java.io.IOException;

public class SingleColumnDistributionCommand implements Command {
    private final SingleColumnDistribution dist;

    public SingleColumnDistributionCommand(String tableName, String attributeName,
                                           String type, String param1, String param2) {
        dist = new SingleColumnDistribution(tableName, attributeName,
                type, param1, param2);
    }

    @Override
    public void execute(Database db) throws IOException, NoSuchMethodException {
        db.addSingleColumnDistribution(dist);
    }

    @Override
    public boolean isQuit() {
        return false;
    }
}
