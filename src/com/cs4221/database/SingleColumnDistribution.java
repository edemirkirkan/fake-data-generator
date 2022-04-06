package com.cs4221.database;

public class SingleColumnDistribution {
    private String tableName;
    private String attributeName;
    private String type;
    private String param1;
    private String param2;

    public SingleColumnDistribution(String tableName, String attributeName,
                                    String type, String param1, String param2) {
        this.type = type;
        this.param1 = param1;
        this.param2 = param2;
        this.tableName = tableName;
        this.attributeName = attributeName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getType() {
        return type;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public String getParam1() {
        return param1;
    }

    public String getParam2() {
        return param2;
    }
}
