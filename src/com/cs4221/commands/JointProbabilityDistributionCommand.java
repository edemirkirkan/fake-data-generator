package com.cs4221.commands;

import com.cs4221.database.Database;

import java.io.IOException;

public class JointProbabilityDistributionCommand implements Command{
    String mean,sd,tableName;
    public JointProbabilityDistributionCommand(String mean, String sd, String tableName) {
        this.mean = mean;
        this.sd = sd;
        this.tableName = tableName;

    }

    @Override
    public void execute(Database db) throws IOException, NoSuchMethodException {
        db.addJointProbabilityDistribution(mean,sd,tableName);
    }

    @Override
    public boolean isQuit() {
        return false;
    }
}
