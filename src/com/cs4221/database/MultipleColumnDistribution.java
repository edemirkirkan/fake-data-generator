package com.cs4221.database;

import java.util.ArrayList;
import java.util.HashMap;

public class MultipleColumnDistribution {
    private String tableName;
    private String attributeName1;
    private String attributeName2;
    private String type;
    private HashMap<Object, ArrayList<Number>> mapToDistributionParams;

    public MultipleColumnDistribution(String tableName, String attributeName1,
                                      String attributeName2, String type,
                                      HashMap<Object, ArrayList<Number>> mapToDistributionParams) {
        this.tableName = tableName;
        this.attributeName1 = attributeName1;
        this.attributeName2 = attributeName2;
        this.type = type;
        this.mapToDistributionParams = mapToDistributionParams;
    }

    public String getTableName() {
        return tableName;
    }

    public String getType() {
        return type;
    }

    public String getAttributeName1() {
        return attributeName1;
    }

    public String getAttributeName2() {
        return attributeName2;
    }

    public HashMap<Object, ArrayList<Number>> getMapToDistributionParams() {
        return mapToDistributionParams;
    }
}
