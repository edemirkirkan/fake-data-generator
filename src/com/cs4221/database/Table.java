package com.cs4221.database;

import java.util.ArrayList;

public class Table {
    private ArrayList<String> attributeNames;
    private final ArrayList<String> attributeTypes;
    private ArrayList<ArrayList<String>> data;
    private ArrayList<Boolean> isKey;
    private final String tableName;

    public Table(ArrayList<Attribute> attributes, ArrayList<ArrayList<String>> data, String tableName) {
        attributeNames = new ArrayList<>();
        attributeTypes = new ArrayList<>();
        isKey = new ArrayList<>();
        for (Attribute att : attributes) {
            attributeNames.add(att.getName());
            attributeTypes.add(att.getType());
            isKey.add(att.getKey());
        }
        this.data = data;
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public String toString() {
        if (attributeNames == null) {
            attributeNames = new ArrayList<>();
        }
        if (data == null) {
            data = new ArrayList<>();
        }
        if (isKey == null) {
            isKey = new ArrayList<>();
        }
        if (attributeNames.size() == 0 && data.size() == 0) {
            return "No Data";
        }
        ArrayList<Integer> maxLength;
        maxLength =  new ArrayList<>();

        for (int i = 0, n = attributeNames.size(); i < n; i++) {
            maxLength.add(attributeNames.get(i).length() + attributeTypes.get(i).length() + 3);
        }
        String title = tableName + " (" + getClass().getSimpleName().toUpperCase() + ")";
        calcMaxLengthAll(maxLength);
        if (attributeNames.size() == 1) {
            maxLength.set(0, Math.max(title.length(), maxLength.get(0)));
        }

        StringBuilder sb = new StringBuilder();
        StringBuilder sbRowSep = new StringBuilder();
        StringBuilder padding = new StringBuilder();
        String rowSeperator;


        int TABLE_PADDING = 4;
        padding.append(" ".repeat(TABLE_PADDING));

        //Create the row seperator
        for (Integer integer : maxLength) {
            sbRowSep.append("+");
            for (int j = 0; j < integer + (TABLE_PADDING * 2); j++) {
                char SEPERATOR_CHAR = '-';
                sbRowSep.append(SEPERATOR_CHAR);
            }
        }
        sbRowSep.append("+");
        rowSeperator = sbRowSep.toString();

        sb.append(rowSeperator);
        sb.append("\n");

        sb.append("|");

        int blankSpace = rowSeperator.length() - 2 - title.length();
        int space = blankSpace / 2;
        int remainingSpace = blankSpace - space;
        sb.append(" ".repeat(Math.max(0, remainingSpace)));
        sb.append(title);
        sb.append(" ".repeat(Math.max(0, space)));
        sb.append("|");
        sb.append("\n");
        sb.append(rowSeperator);
        sb.append("\n");
        // Attribute Names
        sb.append("|");
        for(int i = 0; i < attributeNames.size(); i++){
            String header = attributeNames.get(i) + " (" + attributeTypes.get(i) + ")";
            sb.append(padding);
            if (isKey.get(i)) {
                sb.append(String.join("\u0332", header.split("",-1)));
            }
            else {
                sb.append(header);
            }
            //Fill up with empty spaces
            sb.append(" ".repeat(Math.max(0, (maxLength.get(i) - header.length()))));
            sb.append(padding);
            sb.append("|");
        }
        sb.append("\n");
        sb.append(rowSeperator);
        sb.append("\n");

        //BODY
        for (ArrayList<String> tempRow : data) {
            //New row
            sb.append("|");
            for (int j = 0; j < tempRow.size(); j++) {
                sb.append(padding);
                sb.append(tempRow.get(j));
                //Fill up with empty spaces
                sb.append(" ".repeat(Math.max(0, (maxLength.get(j) - tempRow.get(j).length()))));
                sb.append(padding);
                sb.append("|");
            }
            sb.append("\n");
            sb.append(rowSeperator);
            sb.append("\n");
        }
        return sb.toString();
    }

    private void calcMaxLengthAll(ArrayList<Integer> maxLength){
        for (ArrayList<String> column : data) {
            for (int j = 0; j < column.size(); j++) {
                //If the table content was longer then current maxLength - update it
                if (column.get(j).length() > maxLength.get(j)) {
                    maxLength.set(j, column.get(j).length());
                }
            }
        }
    }
}
