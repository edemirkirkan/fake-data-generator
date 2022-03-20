package com.cs4221.database;

import com.cs4221.database.Attribute;

import java.util.ArrayList;

public class Table {
    ArrayList<String> attributeNames;
    ArrayList<ArrayList<String>> data;
    ArrayList<Boolean> isKey;

    public Table(ArrayList<Attribute> attributes, ArrayList<ArrayList<String>> data) {
        attributeNames = new ArrayList<>();
        isKey = new ArrayList<>();
        for (Attribute att : attributes) {
            attributeNames.add(att.getName());
            isKey.add(att.getKey());
        }
        this.data = data;
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

        for (String header : attributeNames) {
            maxLength.add(header.length());
        }
        calcMaxLengthAll(data, maxLength);
        StringBuilder sb = new StringBuilder();
        StringBuilder sbRowSep = new StringBuilder();
        StringBuilder padder = new StringBuilder();
        String rowSeperator;


        int TABLE_PADDING = 4;
        for(int i = 0; i < TABLE_PADDING; i++){
            padder.append(" ");
        }

        //Create the rowSeperator
        for (Integer integer : maxLength) {
            sbRowSep.append("|");
            for (int j = 0; j < integer + (TABLE_PADDING * 2); j++) {
                char SEPERATOR_CHAR = '-';
                sbRowSep.append(SEPERATOR_CHAR);
            }
        }
        sbRowSep.append("|");
        rowSeperator = sbRowSep.toString();

        sb.append(rowSeperator);
        sb.append("\n");
        // Attribute Names
        sb.append("|");
        for(int i = 0; i < attributeNames.size(); i++){
            sb.append(padder);
            if (isKey.get(i)) {
                sb.append(String.join("\u0332", attributeNames.get(i).split("",-1)));
            }
            else {
                sb.append(attributeNames.get(i));
            }
            //Fill up with empty spaces
            for(int k = 0; k < (maxLength.get(i)-attributeNames.get(i).length()); k++){
                sb.append(" ");
            }
            sb.append(padder);
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
                sb.append(padder);
                sb.append(tempRow.get(j));
                //Fill up with empty spaces
                for (int k = 0; k < (maxLength.get(j) - tempRow.get(j).length()); k++) {
                    sb.append(" ");
                }
                sb.append(padder);
                sb.append("|");
            }
            sb.append("\n");
            sb.append(rowSeperator);
            sb.append("\n");
        }
        return sb.toString();
    }

    private void calcMaxLengthAll(ArrayList<ArrayList<String>> table, ArrayList<Integer> maxLength){
        for (ArrayList<String> temp : table) {
            for (int j = 0; j < temp.size(); j++) {
                //If the table content was longer then current maxLength - update it
                if (temp.get(j).length() > maxLength.get(j)) {
                    maxLength.set(j, temp.get(j).length());
                }
            }
        }
    }
}
