package com.cs4221.utility;


import com.cs4221.commands.*;
import com.cs4221.database.Attribute;

import java.util.ArrayList;

public class Parser {
    public Command parse(String input) {
        String[] keywords = input.split(" ");
        if (keywords[0].equalsIgnoreCase("EXIT")) {
            return new ExitCommand();
        } else if (keywords[0].equalsIgnoreCase("HELP")) {
            return new HelpCommand();
        } else if (keywords[0].equalsIgnoreCase("SHOW") &&
                keywords[1].equalsIgnoreCase("ENTITIES")) {
            return new ShowEntitiesCommand();
        } else if (keywords[0].equalsIgnoreCase("SHOW") &&
                keywords[1].equalsIgnoreCase("RELATIONS")) {
            return new ShowRelationsCommand();
        } else if (keywords[0].equalsIgnoreCase("SHOW") &&
                keywords[1].equalsIgnoreCase("DIAGRAM")) {
            return new ShowDiagramCommand();
        } else if (keywords[0].equalsIgnoreCase("REMOVE") &&
                keywords[1].equalsIgnoreCase("ENTITY")) {
            return parseRemoveEntity(keywords);
        } else if (keywords[0].equalsIgnoreCase("REMOVE") &&
                keywords[1].equalsIgnoreCase("RELATION")) {
            return parseRemoveRelation(keywords);
        } else if (keywords[0].equalsIgnoreCase("CREATE") &&
                keywords[1].equalsIgnoreCase("ENTITY")) {
            return parseCreateEntity(keywords);
        } else if (keywords[0].equalsIgnoreCase("CREATE") &&
                keywords[1].equalsIgnoreCase("RELATION")) {
            return parseCreateRelation(keywords);
        }
        return new ErrorCommand("Wrong Format\nAll available commands can be seen using 'help' command");
    }

    private Command parseRemoveEntity(String[] keywords) {
        if (keywords.length != 3) {
            return new ErrorCommand("Syntax Error\nEntity name cannot be left blank\n");
        }
        return new RemoveEntityCommand(keywords[2]);
    }

    private Command parseRemoveRelation(String[] keywords) {
        if (keywords.length != 3) {
            return new ErrorCommand("Syntax Error\nRelation name cannot be left blank\n");
        }
        return new RemoveRelationCommand(keywords[2]);
    }

    private Command parseCreateEntity(String[] keywords) {
        if (!keywords[3].equalsIgnoreCase("WITH")) {
            return new ErrorCommand("Syntax Error\n" +
                    "CREATE ENTITY command must be used with 'WITH' keyword." +
                    "\n All available commands can be seen using 'help' command");
        }
        String tableName = keywords[2];
        int index = 4;
        ArrayList<Attribute> attributes = new ArrayList<>();
        while (index <= keywords.length - 1 && !keywords[index].equalsIgnoreCase("PRIMARY")) {
            Attribute attribute = new Attribute(keywords[index], removeLastChar(keywords[index + 1]));
            attributes.add(attribute);
            index += 2;
        }
        if (!keywords[index].equalsIgnoreCase("PRIMARY") || !keywords[index + 1].equalsIgnoreCase("KEY")) {
            return new ErrorCommand("Syntax Error\n" +
                    "CREATE ENTITY command must be used with 'PRIMARY KEY' keyword." +
                    "\n All available commands can be seen using 'help' command");
        }

        index += 2;

        while (index <= keywords.length - 1) {
            String attributeName = removeLastChar(keywords[index]);
            boolean keyFound = false;
            for (Attribute att : attributes) {
                if (attributeName.equals(att.getName())) {
                    keyFound = true;
                    att.setKey();
                    break;
                }
            }
            if (!keyFound) {
                return new ErrorCommand("Primary Key Error\n'" + attributeName +
                        "' doesn't match with any of the attributes of corresponding table.");
            }
            index++;
        }
        return new CreateEntityCommand(tableName, attributes);
    }

    private Command parseCreateRelation(String[] keywords) {
        if (!keywords[3].equalsIgnoreCase("FOR")) {
            return new ErrorCommand("Syntax Error\n" +
                    "CREATE RELATION command must be used with 'FOR' keyword." +
                    "\n All available commands can be seen using 'help' command");
        }
        if (!keywords[5].equalsIgnoreCase("WITH") ||
                !keywords[13].equalsIgnoreCase("WITH") ||
                !keywords[6].equalsIgnoreCase("PARTICIPATION") ||
                !keywords[14].equalsIgnoreCase("PARTICIPATION")) {
            return new ErrorCommand("Syntax Error\n" +
                    "CREATE RELATION command must specify the join constraints " +
                    "with the keywords WITH PARTICIPATION." +
                    "\n All available commands can be seen using 'help' command");

        }
        if (!keywords[7].equalsIgnoreCase("MIN") ||
                !keywords[9].equalsIgnoreCase("MAX") ||
                !keywords[15].equalsIgnoreCase("MIN") ||
                !keywords[17].equalsIgnoreCase("MAX")) {
            return new ErrorCommand("Syntax Error\n" +
                    "CREATE RELATION command must specify the join constraints " +
                    "with the keywords MIN MAX" +
                    "\n All available commands can be seen using 'help' command");
        }
        if (!keywords[11].equalsIgnoreCase("AND")) {
            return new ErrorCommand("Syntax Error\n" +
                    "All available commands can be seen using 'help' command");
        }
        String relationName = keywords[2];
        String leftTableName = keywords[4];
        String leftTableMinConst = keywords[8];
        String leftTableMaxConst = keywords[10];
        String rightTableName = keywords[12];
        String rightTableMinConst = keywords[16];
        String rightTableMaxConst = keywords[18];
        String hasAttribute;
        ArrayList<Attribute> attributes = new ArrayList<>();
        try {
            hasAttribute = keywords[19];
        } catch (IndexOutOfBoundsException ex) {
            return new CreateRelationCommand(relationName, leftTableName, leftTableMinConst, leftTableMaxConst,
                    rightTableName, rightTableMinConst, rightTableMaxConst, attributes);
        }
        if (!hasAttribute.equalsIgnoreCase("ATTRIBUTES")) {
            return new ErrorCommand("Syntax Error\n" +
                    "CREATE RELATION command must specify the extra attributes of relation if any " +
                    "with the keyword ATTRIBUTES" +
                    "\n All available commands can be seen using 'help' command");
        }

        for (int i = 20; i < keywords.length; i += 2) {
            Attribute attribute = new Attribute(keywords[i], removeLastChar(keywords[i + 1]));
            attributes.add(attribute);
        }
        return new CreateRelationCommand(relationName, leftTableName, leftTableMinConst, leftTableMaxConst,
                rightTableName, rightTableMinConst, rightTableMaxConst, attributes);
    }

    private String removeLastChar(String s) {
        return (s == null || s.length() == 0)
                ? null
                : s.charAt(s.length() - 1) == ',' ? s.substring(0, s.length() - 1) : s;
    }
}
