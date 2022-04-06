package com.cs4221.utility;

import java.util.ArrayList;

public class UI {
    public String displayEntity(ArrayList<String> attributeNames, ArrayList<ArrayList<String>> data,
                                ArrayList<String> attributeTypes, ArrayList<Boolean> isKey, String tableName, String tableType) {
        if (attributeNames == null) {
            attributeNames = new ArrayList<>();
        }
        if (data == null) {
            data = new ArrayList<>();
        }
        if (isKey == null) {
            isKey = new ArrayList<>();
        }

        ArrayList<Integer> maxLength = new ArrayList<>();

        for (int i = 0, n = attributeNames.size(); i < n; i++) {
            final int extraChars = isKey.get(i) ? 8 : 3;
            maxLength.add(attributeNames.get(i).length() + attributeTypes.get(i).length() + extraChars);
        }
        String title = tableName + " (" + tableType + ")";
        // Calculate max length
        for (ArrayList<String> column : data) {
            for (int j = 0, n = column.size(); j < n; j++) {
                if (column.get(j).length() > maxLength.get(j)) {
                    maxLength.set(j, column.get(j).length());
                }
            }
        }
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
        for (int i = 0; i < attributeNames.size(); i++) {
            String key = isKey.get(i) ? " (PK)" : "";
            String header = attributeNames.get(i) + " (" + attributeTypes.get(i) + ")" + key;
            sb.append(padding);
            sb.append(header);
            sb.append(" ".repeat(Math.max(0, (maxLength.get(i) - header.length()))));
            sb.append(padding);
            sb.append("|");
        }
        sb.append("\n");
        sb.append(rowSeperator);
        sb.append("\n");

        //BODY
        for (ArrayList<String> tempRow : data) {
            sb.append("|");
            for (int j = 0; j < tempRow.size(); j++) {
                sb.append(padding);
                sb.append(tempRow.get(j));
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

    public String getTypeText() {
        final String SEPARATOR = "------------------------------------------------------------------" +
                "------------";

        return SEPARATOR + "\nAnimal_Common_Name, App_Bundle_ID, Bitcoin_Address, Boolean, Car_Model, City\n" +
                "Color, Country, Currency, Datetime, Email_Address, First_Name, Full_Name\nGender, IBAN," +
                "IP_Address_V4, IP_Address_V6, Last_Name, MAC_Address, Number\nPassword, Phone, Row_Number, " +
                "URL, Username, ISBN, SSN, Race, Time_Zone\n" + SEPARATOR;
    }

    public String getHelpText() {
        final String SEPARATOR = "------------------------------------------------------------------" +
                "----------------------------------------------------------------------------";
        return SEPARATOR + "\nKeep in mind that all of the commands and keywords are " +
                "case insensitive i.e help, HELP both can be used\n" + SEPARATOR + "\nCREATE ENTITY " +
                "COMMAND\nFormat\nCREATE ENTITY {entity_name} " + "WITH {attribute_name} " +
                "{attribute_type}" + ",{attribute2_name} {attribute2_type}, ... " +
                "PRIMARY KEY {attribute_name}, " + "...\nUsage\nCREATE ENTITY student " +
                "WITH s_id Row_Number, s_name First_Name, s_lname Last_Name PRIMARY KEY s_id\nOR\n" +
                "CREATE ENTITY teacher WITH t_id Row_Number, t_name First_Name, t_lname " +
                "Last_Name PRIMARY KEY t_id\n" + SEPARATOR + "\nCREATE " +
                "RELATION COMMAND\nFormat\nCREATE RELATION {relation_name} FOR {left_table_name} WITH " +
                "PARTICIPATION MIN {left_table_min} MAX\n{left_table_max} AND {right_table_name} WITH " +
                "PARTICIPATION MIN {right_table_min} MAX {right_table_max}\n('optional') ATTRIBUTES {attribute_name}" +
                " {attribute_type}, {attribute2_name} {attribute2_type}, ...\n" + "Usage\nCREATE RELATION student-teacher FOR student WITH PARTICIPATION MIN 0 MAX * AND teacher WITH PARTICIPATION MIN 1 " +
                "MAX *\nOR\nCREATE RELATION student-teacher FOR student WITH PARTICIPATION MIN 0 MAX * " +
                "AND teacher WITH PARTICIPATION MIN 1 MAX 1 ATTRIBUTES date Datetime\n" + SEPARATOR +
                "\nREMOVE ENTITY COMMAND\nFormat\nREMOVE ENTITY {entity_name}\nUsage\nREMOVE ENTITY " +
                "student\n" + SEPARATOR + "\nREMOVE RELATION COMMAND\nFormat\nREMOVE RELATION " +
                "{relation_name}" + "\nUsage\nREMOVE RELATION " + "student-teacher\n" + SEPARATOR +
                "\nSHOW ENTITIES COMMAND\nFormat/Usage\nSHOW ENTITIES\n" + SEPARATOR + "\nSHOW " +
                "RELATIONS COMMAND\nFormat/Usage\nSHOW RELATIONS\n" + SEPARATOR + "\nSHOW DIAGRAM " +
                "COMMAND\nFormat/Usage\nSHOW DIAGRAM\n" + SEPARATOR + "\nCREATE DISTRIBUTION COMMAND\n" +
                "Format/Usage:\nCREATE UNIFORM_INTEGER DISTRIBUTION WITH PARAM1 20 PARAM2 50 FOR " +
                "ATTRIBUTE s_id IN TABLE student\nOR\nCREATE GEOMETRIC DISTRIBUTION WITH PARAM1 1 PARAM2 " +
                "NaN FOR ATTRIBUTE s_id IN TABLE student\n" + SEPARATOR + "\nCREATE JOINT DISTRIBUTION COMMAND\n" +
                "Format/Usage:\nCREATE UNIFORM_INTEGER JOINT DISTRIBUTION WITH PARAM1 20 PARAM2 50 FOR " +
                "ATTRIBUTE s_id IN TABLE student\nOR\nCREATE GEOMETRIC JOINT DISTRIBUTION WITH PARAM1 1 PARAM2 NaN " +
                "FOR ATTRIBUTE s_id IN TABLE student\n" + SEPARATOR + "\nSHOW TYPES\nFormat/Usage\nTYPE\n" +
                SEPARATOR + "\nGENERATE DATA COMMAND\nFormat/Usage\n" +
                "GENERATE DATA {row_number}\n" + SEPARATOR + "\nEXIT COMMAND\nFormat/Usage\nEXIT\n" + SEPARATOR;
    }

    public String displayRelation(ArrayList<String> attributeNames, ArrayList<String> attributeTypes, String tableName,
                                  String leftTableName, String rightTableName, String leftTableParticipation,
                                  String rightTableParticipation, String relationshipType) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sep = new StringBuilder();
        final int PADDING = 4;
        int fl = Math.max(4 * PADDING + leftTableName.length() + 1, 2 * PADDING + leftTableParticipation.length());
        int sl = Math.max(5 * PADDING + tableName.length() - 1, relationshipType.length());
        int tl = Math.max(4 * PADDING + rightTableName.length() + 1, 2 * PADDING + rightTableParticipation.length());
        sep.append("+");
        sep.append("-".repeat(Math.max(0, fl)));
        sep.append("+");
        sep.append("-".repeat(Math.max(0, sl)));
        sep.append("+");
        sep.append("-".repeat(Math.max(0, tl)));
        sep.append("+");
        sb.append(sep);
        sb.append("\n");
        sb.append("|");
        String leftTitle = leftTableName + " (ENTITY)";
        int space = fl - leftTitle.length();
        sb.append(" ".repeat(space / 2));
        sb.append(leftTitle);
        sb.append(" ".repeat(space - space / 2));
        sb.append("|");
        String relationName = tableName + " (RELATION)";
        space = sl - relationName.length();
        sb.append(" ".repeat(space / 2));
        sb.append(relationName);
        sb.append(" ".repeat(space - space / 2));
        sb.append("|");
        String rightTitle = rightTableName + " (ENTITY)";
        space = tl - rightTitle.length();
        sb.append(" ".repeat(space / 2));
        sb.append(rightTitle);
        sb.append(" ".repeat(space - space / 2));
        sb.append("|");
        sb.append("\n");
        sb.append(sep);
        sb.append("\n");
        sb.append("|");
        space = fl - leftTableParticipation.length();
        sb.append(" ".repeat(space / 2));
        sb.append(leftTableParticipation);
        sb.append(" ".repeat(space - space / 2));
        sb.append("|");
        space = sl - relationshipType.length();
        sb.append(" ".repeat(space / 2));
        sb.append(relationshipType);
        sb.append(" ".repeat(space - space / 2));
        sb.append("|");
        space = tl - rightTableParticipation.length();
        sb.append(" ".repeat(space / 2));
        sb.append(rightTableParticipation);
        sb.append(" ".repeat(space - space / 2));
        sb.append("|");
        sb.append("\n");
        sb.append(sep);
        sb.append("\n");
        if (attributeNames.size() > 0) {
            StringBuilder extraAtt = new StringBuilder();
            for (int i = 0, n = attributeNames.size(); i < n; i++) {
                extraAtt.append(" ".repeat(PADDING));
                extraAtt.append(attributeNames.get(i)).append(" (").append(attributeTypes.get(i)).append(")");
                extraAtt.append(" ".repeat(PADDING));
            }
            int sp = fl + sl + tl + 2 - extraAtt.toString().length();
            if (sp > 0) {
                sb.append("|");
                sb.append(" ".repeat(sp / 2));
                sb.append(extraAtt);
                sb.append(" ".repeat(sp - sp / 2));
                sb.append("|");
                sb.append("\n");
                sb.append(sep);
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
