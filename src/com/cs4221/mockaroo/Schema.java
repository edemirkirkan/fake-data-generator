package com.cs4221.mockaroo;

import com.cs4221.database.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Schema {
    private static int rowCount;

    public Schema(int rowCount, ArrayList<Table> tables, Database db) throws IOException {
        Schema.rowCount = rowCount;
        this.generateSchema(tables, db);
    }

    public void generateSchema(ArrayList<Table> tables, Database db) throws IOException {
        ArrayList<Table> finalTable = new ArrayList<>();
        ArrayList<Table> finalTableWORelation = new ArrayList<>();
        List<String> entityAlreadyWithSchema = new ArrayList<>();
        for (Table t : tables) {

            if (t instanceof Relation) {  // check if this is relation
                String leftName = ((Relation) t).getLeftTableName();
                String rightName = ((Relation) t).getRightTableName();
                String leftParticipation = null;
                String rightParticipation = null;
                if (Objects.equals(((Relation) t).getLeftTableParticipation(), "Partial Participation")) {
                    leftParticipation = "Partial Participation";
                }
                if (Objects.equals(((Relation) t).getRightTableParticipation(), "Partial Participation")) {
                    rightParticipation = "Partial Participation";
                }
                if (Objects.equals(((Relation) t).getLeftTableParticipation(), "Total Participation")) {
                    leftParticipation = "Total Participation";
                }
                if (Objects.equals(((Relation) t).getRightTableParticipation(), "Total Participation")) {
                    rightParticipation = "Total Participation";
                }
                if (Objects.equals(((Relation) t).getRelationshipType(), "Many-Many")) {
                    ArrayList<Attribute> attributesArray = new ArrayList<>();
                    String tableName = t.getTableName();
                    for (Table t1 : tables) {
                        ArrayList<Attribute> attributes = t1.getTableAttributes();
                        if ((t1 instanceof Entity && t1.getTableName().equals(leftName)) ||
                                (t1 instanceof Entity && t1.getTableName().equals(rightName))) {
                            if (!entityAlreadyWithSchema.contains(t1.getTableName())) {
                                finalTable.add(t1);
                                entityAlreadyWithSchema.add(t1.getTableName());
                            }
                            for (Attribute att : attributes) {
                                if (att.getKey()) {
                                    Attribute arr;
                                    if (t1.getTableName().equals(leftName)) {
                                        arr = new Attribute(att.getName(), att.getType() + "&!" + leftParticipation);
                                    } else {
                                        arr = new Attribute(att.getName(), att.getType() + "&!" + rightParticipation);
                                    }
                                    attributesArray.add(arr);

                                }
                            }
                        }
                    }
                    // if relation attributes are there
                    ArrayList<Attribute> relationAttributes = t.getTableAttributes();
                    for (Attribute att : relationAttributes) {
                        Attribute arr = new Attribute(att.getName(), att.getType());
                        attributesArray.add(arr);
                    }
                    Table entity = new Entity(attributesArray, null, tableName);
                    finalTable.add(entity);
                    Repository r = new Repository(rowCount, finalTable, "Many-Many", null, db);

                } else if (Objects.equals(((Relation) t).getRelationshipType(), "One-One")) {
                    String tableName = t.getTableName();
                    if (Objects.equals(leftParticipation, "Total Participation") && Objects.equals(rightParticipation, "Total Participation")) {
                        for (Table t1 : tables) {
                            if ((t1 instanceof Entity && t1.getTableName().equals(leftName)) ||
                                    (t1 instanceof Entity && t1.getTableName().equals(rightName))) {
                                if (!entityAlreadyWithSchema.contains(t1.getTableName())) {
                                    finalTable.add(t1);
                                    entityAlreadyWithSchema.add(t1.getTableName());
                                }

                            }
                        }
                    }
                    Repository r = new Repository(rowCount, finalTable, "One-One", tableName, db);
                }

                //Start of May part

                else {
                    boolean isBothPartial = Objects.equals(leftParticipation, "Partial Participation")
                            && Objects.equals(rightParticipation, "Partial Participation");
                    if (Objects.equals(((Relation) t).getRelationshipType(), "One-Many")) {
                        String tableName = t.getTableName();
                        // for total partial relation  (1,1)(0,*)  - have to merge relation and left table
                        if (Objects.equals(leftParticipation, "Total Participation") &&
                                Objects.equals(rightParticipation, "Partial Participation")) {
                            //have to merge left and relation table
                            //start here
                            ArrayList<Attribute> attributesArray = new ArrayList<>();
                            for (Table t1 : tables) {
                                ArrayList<Attribute> attributes = t1.getTableAttributes();
                                if (t1 instanceof Entity && t1.getTableName().equals(leftName)) {
                                    for (Attribute att : attributes) {
                                        if (att.getKey()) {
                                            Attribute arr = new Attribute(att.getName(), att.getType());
                                            arr.setKey(); // make left table primary key as primary key for this table
                                            attributesArray.add(arr);
                                        } else {
                                            attributesArray.add(att); // add all the attributes of left table
                                        }

                                    }
                                }
                                if (t1 instanceof Entity && t1.getTableName().equals(rightName)) {
                                    //adding right table as original
                                    if (!entityAlreadyWithSchema.contains(t1.getTableName())) {
                                        finalTable.add(t1);
                                        entityAlreadyWithSchema.add(t1.getTableName());
                                    }

                                    for (Attribute att : attributes) {// add right table primary key as foreign key to merge table
                                        if (att.getKey()) {
                                            Attribute arr = new Attribute(att.getName(), att.getType());
                                            attributesArray.add(arr);
                                        }
                                    }

                                }


                            }
                            //adding attributes of relations
                            ArrayList<Attribute> relationAttributes = t.getTableAttributes();
                            if (relationAttributes.size() > 0) {
                                attributesArray.addAll(relationAttributes);
                            }
                            Table entity = new Entity(attributesArray, null, leftName); //adding merge table
                            finalTable.add(entity);
                            entityAlreadyWithSchema.add(leftName);//no need to add left table again because left and relation table merge


                        }


                        // for partial partial relation (0,1)(0,*)
                        if (isBothPartial) {
                            // same as many-many except primary is left table primary key
                            //start here
                            ArrayList<Attribute> attributesArray = new ArrayList<>();
                            for (Table t1 : tables) {
                                ArrayList<Attribute> attributes = t1.getTableAttributes();
                                if ((t1 instanceof Entity && t1.getTableName().equals(leftName)) ||
                                        (t1 instanceof Entity && t1.getTableName().equals(rightName))) {
                                    if (!entityAlreadyWithSchema.contains(t1.getTableName())) {
                                        finalTable.add(t1);
                                        entityAlreadyWithSchema.add(t1.getTableName());
                                    }
                                    for (Attribute att : attributes) {
                                        if (att.getKey()) {
                                            if (t1.getTableName().equals(leftName)) {
                                                Attribute arr = new Attribute(att.getName(), att.getType());
                                                arr.setKey();//making left table primary key as primary key
                                                attributesArray.add(arr);
                                            } else if (t1.getTableName().equals(rightName)) {
                                                Attribute arr = new Attribute(att.getName(), att.getType());
                                                attributesArray.add(arr);
                                            }

                                        }
                                    }
                                }
                            }

                            // if relation attributes are there
                            ArrayList<Attribute> relationAttributes = t.getTableAttributes();
                            for (Attribute att : relationAttributes) {
                                Attribute arr = new Attribute(att.getName(), att.getType());
                                attributesArray.add(arr);
                            }
                            Table entity = new Entity(attributesArray, null, tableName);
                            finalTable.add(entity);
                        }
                        Repository r = new Repository(rowCount, finalTable, "One-Many", null, db);

                    } else if (Objects.equals(((Relation) t).getRelationshipType(), "Many-One")) {
                        String tableName = t.getTableName();
                        // for partial total relation (0,*)(1,1) - have to merge relation and right table
                        if (Objects.equals(leftParticipation, "Partial Participation")
                                && Objects.equals(rightParticipation, "Total Participation")) {
                            //have to merge right and relation table
                            ArrayList<Attribute> attributesArray = new ArrayList<>();
                            for (Table t1 : tables) {
                                ArrayList<Attribute> attributes = t1.getTableAttributes();
                                if (t1 instanceof Entity && t1.getTableName().equals(rightName)) {
                                    for (Attribute att : attributes) {
                                        if (att.getKey()) {
                                            Attribute arr = new Attribute(att.getName(), att.getType());
                                            arr.setKey(); // make right table primary key as primary key for this table
                                            attributesArray.add(arr);
                                        } else {
                                            attributesArray.add(att); // add all the attributes of right table
                                        }

                                    }
                                }
                                if (t1 instanceof Entity && t1.getTableName().equals(leftName)) {
                                    //adding left table as original
                                    if (!entityAlreadyWithSchema.contains(t1.getTableName())) {
                                        finalTable.add(t1);
                                        entityAlreadyWithSchema.add(t1.getTableName());
                                    }

                                    for (Attribute att : attributes) {
                                        // add left table primary key as foreign key to merged table
                                        if (att.getKey()) {
                                            Attribute arr = new Attribute(att.getName(), att.getType());
                                            attributesArray.add(arr);
                                        }
                                    }

                                }


                            }
                            //adding attributes of relations
                            ArrayList<Attribute> relationAttributes = t.getTableAttributes();
                            if (relationAttributes.size() > 0) {
                                attributesArray.addAll(relationAttributes);
                            }

                            Table entity = new Entity(attributesArray, null, rightName); //adding merge table
                            finalTable.add(entity);
                            entityAlreadyWithSchema.add(rightName);// No need to add right table again because right table and relation table merged
                        }


                        // for partial partial relation (0,*) (0,1)
                        if (isBothPartial) {
                            // same as many-many except primary is right table primary key
                            //start here
                            ArrayList<Attribute> attributesArray = new ArrayList<>();
                            for (Table t1 : tables) {
                                ArrayList<Attribute> attributes = t1.getTableAttributes();
                                if ((t1 instanceof Entity && t1.getTableName().equals(leftName)) ||
                                        (t1 instanceof Entity && t1.getTableName().equals(rightName))) {
                                    if (!entityAlreadyWithSchema.contains(t1.getTableName())) {
                                        finalTable.add(t1);
                                        entityAlreadyWithSchema.add(t1.getTableName());
                                    }
                                    for (Attribute att : attributes) {
                                        if (att.getKey()) {
                                            if (t1.getTableName().equals(leftName)) {
                                                Attribute arr = new Attribute(att.getName(), att.getType());
                                                attributesArray.add(arr);
                                            } else if (t1.getTableName().equals(rightName)) {
                                                Attribute arr = new Attribute(att.getName(), att.getType());
                                                arr.setKey();//making right table primary key as primary key
                                                attributesArray.add(arr);
                                            }

                                        }
                                    }
                                }
                            }

                            // if relation attributes are there
                            ArrayList<Attribute> relationAttributes = t.getTableAttributes();
                            for (Attribute att : relationAttributes) {
                                Attribute arr = new Attribute(att.getName(), att.getType());
                                attributesArray.add(arr);
                            }
                            Table entity = new Entity(attributesArray, null, tableName);
                            finalTable.add(entity);
                        }

                        Repository r = new Repository(rowCount, finalTable, "Many-One", null, db);

                    }
                }
            }
        }

        for (Table t : tables) { // for tables that is not in relation
            if (t instanceof Entity) {  // check this is entity
                if (!entityAlreadyWithSchema.contains(t.getTableName())) { // check if the table schema already generated inside relation loop
                    finalTableWORelation.add(t);
                    entityAlreadyWithSchema.add(t.getTableName());
                }
            }
        }
        if (finalTableWORelation.size() > 0) {
            Repository r = new Repository(rowCount, finalTableWORelation, "Independent", null, db);
        }


    }


}