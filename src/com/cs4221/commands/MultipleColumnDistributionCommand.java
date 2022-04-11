package com.cs4221.commands;

import com.cs4221.database.Database;
import com.cs4221.database.MultipleColumnDistribution;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MultipleColumnDistributionCommand implements Command {
    private final MultipleColumnDistribution dist;

    public MultipleColumnDistributionCommand(String tableName, String attributeName1,
                                             String attributeName2, String type,
                                             HashMap<Object, ArrayList<Number>> mapToDistributionParams) {
        dist = new MultipleColumnDistribution(tableName, attributeName1, attributeName2, type, mapToDistributionParams);
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
