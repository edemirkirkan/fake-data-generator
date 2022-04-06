package com.cs4221.database;

public class MultipleColumnDistribution {
    private String tableName1;
    private String attributeName1;
    private String tableName2;
    private String attributeName2;
    private String type;
    private String param1;
    private String param2;

    public MultipleColumnDistribution(String tableName1, String attributeName1, String tableName2,
                                      String attributeName2, String type, String param1, String param2) {
        this.type = type;
        this.param1 = param1;
        this.param2 = param2;
        this.tableName1 = tableName1;
        this.attributeName1 = attributeName1;
        this.tableName2 = tableName2;
        this.attributeName2 = attributeName2;
    }

    public String getTableName1() {
        return tableName1;
    }

    public String getType() {
        return type;
    }

    public String getAttributeName1() {
        return attributeName1;
    }

    public String getTableName2() {
        return tableName2;
    }

    public String getAttributeName2() {
        return attributeName2;
    }

    public String getParam1() {
        return param1;
    }

    public String getParam2() {
        return param2;
    }
}
