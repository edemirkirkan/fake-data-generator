package com.cs4221.mockaroo;

import com.cs4221.database.Attribute;
import com.cs4221.database.Database;
import com.cs4221.database.JointProbabilityDistribution;
import com.cs4221.database.Table;
import org.apache.commons.io.IOUtils;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Repository {
    private static JSONArray finalJSONArray = new JSONArray();
    private static JSONArray finalSQLArray = new JSONArray();
    private static int rowCount;
    private static String schemaType;
    private static String tblName = null;

    public Repository(int rowCount, ArrayList<Table> tables, String schemaType,
                      String tableName, Database db) throws IOException {
        Repository.rowCount = rowCount;
        Repository.schemaType = schemaType;
        Repository.tblName = tableName;
        // finalJSONArray = this.generateData(tables); // TODO generate csv files at the end, return final json array
        // finalJSONArray = db.applySingleColumnDistributions(finalJSONArray, rowCount);
        // generate csv here, smt like generateCSV(finalJSONArray);
        // generate at the end, instead of step by step
        this.generateData(tables);
        JSONArray distJsonArray = db.applySingleColumnDistributions(finalJSONArray, rowCount);
        if (distJsonArray.isEmpty()) {
            throw new IOException();
        }

        distJsonArray = db.applyMultipleColumnDistributions(finalJSONArray);
        if (distJsonArray.isEmpty()) {
            throw new IOException();
        }

        for (int i = 0; i < distJsonArray.length(); i++) {
            JSONObject tableObject = distJsonArray.getJSONObject(i);
            JSONArray tableArray = tableObject.getJSONArray("data");
            String tbName = tableObject.optString("tableName");
            generateCSV(tableArray, tbName);
        }
    }

    public void generateData(ArrayList<Table> tables) throws IOException {
        if (Objects.equals(schemaType, "Many-Many")) {

            int index = 1;
            for (Table t : tables) {
                JSONArray fields = new JSONArray();
                if (index == tables.size()) { // for relation table in Many-Many (3 rd table)
                    JSONArray relationAttributes = new JSONArray();
                    JSONArray relationAttributeData = new JSONArray();
                    ArrayList<Attribute> attributes = t.getTableAttributes();
                    ArrayList<String> leftTablePrimaryIds = new ArrayList<>();
                    ArrayList<String> rightTablePrimaryIds = new ArrayList<>();

                    int tableCount = 0;

                    String leftParticipation = null;
                    String leftPrimaryIdColumnName = null;
                    String rightParticipation = null;
                    String rightPrimaryIdColumnName = null;
                    for (Attribute att : attributes) {  // get Primary id's of each table

                        if (tableCount == 0) { // check if it's left table to get their respective participation
                            for (int i = 0; i < rowCount; i++) { //changes after merge
                                JSONObject leftTableObject = finalJSONArray.getJSONObject(tableCount);
                                JSONArray leftTableArray = leftTableObject.getJSONArray("data");
                                JSONObject jsonObject1 = leftTableArray.getJSONObject(i);
                                String value1 = jsonObject1.optString(att.getName());
                                leftTablePrimaryIds.add(value1);
                            }
                            if (att.getType().contains("Partial Participation")) {
                                leftParticipation = "Partial Participation";
                            } else if (att.getType().contains("Total Participation")) {
                                leftParticipation = "Total Participation";
                            }
                            leftPrimaryIdColumnName = att.getName();
                            tableCount++;
                        } else if (tableCount == 1) { // check if it's right table to get their respective participation
                            for (int i = 0; i < rowCount; i++) { //changes after merge
                                JSONObject rightTableObject = finalJSONArray.getJSONObject(tableCount);
                                JSONArray rightTableArray = rightTableObject.getJSONArray("data");
                                JSONObject jsonObject1 = rightTableArray.getJSONObject(i);
                                String value1 = jsonObject1.optString(att.getName());
                                rightTablePrimaryIds.add(value1);
                            }
                            if (att.getType().contains("Partial Participation")) {
                                rightParticipation = "Partial Participation";
                            } else if (att.getType().contains("Total Participation")) {
                                rightParticipation = "Total Participation";
                            }
                            rightPrimaryIdColumnName = att.getName();
                            tableCount++;
                        } else { //generate data for relation attributes
                            JSONObject jsonobj = orderJsonObject();
                            jsonobj.put("name", att.getName());
                            jsonobj.put("type", att.getType().replace("_", " "));
                            relationAttributes.put(jsonobj);
                        }
                    }
                    if (relationAttributes.length() > 0) {
                        relationAttributeData = fetchData(relationAttributes, null);
                    }
                    List<String> leftPartialList = leftTablePrimaryIds.subList(0, (int) Math.floor(rowCount / 2) != 0 ? (int) Math.floor(rowCount / 2) : rowCount);
                    List<String> rightPartialList = rightTablePrimaryIds.subList(0, (int) Math.floor(rowCount / 2) != 0 ? (int) Math.floor(rowCount / 2) : rowCount);
                    int MedianId = (int) Math.floor(rowCount / 2) != 0 ? (int) Math.floor(rowCount / 2) : rowCount;
                    if ((Objects.equals(leftParticipation, "Partial Participation"))
                            && (Objects.equals(rightParticipation, "Partial Participation"))) { // check both tables are partial, if it is get only partial id's for table construction (rowcount/2)
                        List<String> leftPartialIds = leftTablePrimaryIds.subList(0, (int) Math.floor(rowCount / 2) != 0 ? (int) Math.floor(rowCount / 2) : rowCount);
                        List<String> rightPartialIds = rightTablePrimaryIds.subList(0, (int) Math.floor(rowCount / 2) != 0 ? (int) Math.floor(rowCount / 2) : rowCount);
                        for (int i = 0; i < leftPartialIds.size() && fields.length() < rowCount; i++) {
                            for (int j = 0; j < rightPartialIds.size(); j++) {

                                if (fields.length() < rowCount) {
                                    JSONObject jsonobj = orderJsonObject();
                                    jsonobj.put(leftPrimaryIdColumnName, leftPartialIds.get(i));
                                    jsonobj.put(rightPrimaryIdColumnName, rightPartialIds.get(j));
                                    // only if relation has attributes (this will happen for many-many)
                                    for (int m = 0; m < relationAttributes.length(); m++) {
                                        ArrayList<String> relationAttributeRandomData = new ArrayList<>();
                                        JSONObject jsonObject1 = relationAttributes.getJSONObject(m);
                                        String relationAttrName = jsonObject1.getString("name");
                                        for (int m1 = 0; m1 < relationAttributeData.length(); m1++) {
                                            JSONObject jsonObject2 = relationAttributeData.getJSONObject(m1);
                                            String value1 = jsonObject2.optString(relationAttrName);
                                            relationAttributeRandomData.add(value1);
                                        }
                                        Random r = new Random();
                                        int randomNumber = r.nextInt(relationAttributeRandomData.size());
                                        jsonobj.put(relationAttrName, relationAttributeRandomData.get(randomNumber));
                                    }
                                    fields.put(jsonobj);
                                } else {
                                    break;
                                }
                            }
                        }
                    } else if ((Objects.equals(leftParticipation, "Partial Participation")) &&
                            (Objects.equals(rightParticipation, "Total Participation"))) {
                        for (int i = 0; i < leftTablePrimaryIds.size() && fields.length() < rowCount;
                             i++) { // as left table is partial, it is in main loop
                            for (int j = 0; j < rowCount;
                                 j++) { // as right table has full participation , it is in sub loop , so it covers all sub-loop and if teh total row count is still within limit then it goes to main loop
                                JSONObject jsonobj = orderJsonObject();
                                //jsonobj.put(leftPrimaryIdColumnName, leftTablePrimaryIds.get(i));
                                jsonobj.put(leftPrimaryIdColumnName, leftPartialList.get(new Random().nextInt(leftPartialList.size())));
                                jsonobj.put(rightPrimaryIdColumnName, rightTablePrimaryIds.get(j));
                                fields.put(jsonobj);
                            }
                        }
                        if (MedianId != 1) {
                            List<String> leftPartialListFRromMidToEnd = leftTablePrimaryIds.subList(MedianId, rowCount - 1);
                            List<String> rightPartialListFRromMidToEnd = rightTablePrimaryIds.subList(MedianId, rowCount - 1);
                            for (int i = 0; i < leftTablePrimaryIds.size() && fields.length() < rowCount + rowCount;
                                 i++) { // as left table is partial, it is in main loop
                                for (int j = 0; j < rowCount;
                                     j++) { // as right table has full participation , it is in sub loop , so it covers all sub-loop and if teh total row count is still within limit then it goes to main loop
                                    JSONObject jsonobj = orderJsonObject();
                                    //jsonobj.put(leftPrimaryIdColumnName, leftTablePrimaryIds.get(i));
                                    jsonobj.put(leftPrimaryIdColumnName, leftPartialListFRromMidToEnd.get(new Random().nextInt(leftPartialListFRromMidToEnd.size())));
                                    jsonobj.put(rightPrimaryIdColumnName, rightTablePrimaryIds.get(j));
                                    fields.put(jsonobj);
                                }
                            }
                        }
                    } else if ((Objects.equals(leftParticipation, "Total Participation")) && (Objects.equals(rightParticipation, "Partial Participation"))) { // same as above but vice versa
                        for (int i = 0; i < rightTablePrimaryIds.size() && fields.length() < rowCount; i++) {
                            for (int j = 0; j < rowCount; j++) {
                                JSONObject jsonobj = orderJsonObject();
                                jsonobj.put(leftPrimaryIdColumnName, leftTablePrimaryIds.get(j));
                                //  jsonobj.put(rightPrimaryIdColumnName, rightTablePrimaryIds.get(i));
                                jsonobj.put(rightPrimaryIdColumnName, rightPartialList.get(new Random().nextInt(rightPartialList.size())));
                                fields.put(jsonobj);
                            }
                        }
                        if (MedianId != 1) {
                            List<String> rightPartialListFromMidToEnd = rightTablePrimaryIds.subList(MedianId, rowCount);
                            for (int i = 0; i < rightTablePrimaryIds.size() && fields.length() < rowCount + rowCount;
                                 i++) {
                                for (int j = 0; j < rowCount; j++) {
                                    JSONObject jsonobj = orderJsonObject();
                                    jsonobj.put(leftPrimaryIdColumnName, leftTablePrimaryIds.get(j));
                                    //  jsonobj.put(rightPrimaryIdColumnName, rightTablePrimaryIds.get(i));
                                    jsonobj.put(rightPrimaryIdColumnName, rightPartialListFromMidToEnd.get(new Random().nextInt(rightPartialListFromMidToEnd.size() - 1)));
                                    fields.put(jsonobj);
                                }
                            }
                        }
                    } else { //Total - Total Participation
                        for (int i = 0; i < rightTablePrimaryIds.size();
                             i++) { //as both tables are fully involved, every table mapped with other
                            for (int j = 0; j < rightTablePrimaryIds.size(); j++) {
                                JSONObject jsonobj = orderJsonObject();
                                jsonobj.put(leftPrimaryIdColumnName, leftTablePrimaryIds.get(i));
                                jsonobj.put(rightPrimaryIdColumnName, rightTablePrimaryIds.get(j));
                                fields.put(jsonobj);
                            }
                        }
                    }
                    //generateCSV(fields, t.getTableName());
                    schemaParsing(fields, t.getTableName()); //change by May
                } else { // for entity involved in relation (main 2 tables)
                    fields = new JSONArray();
                    ArrayList<Attribute> attributes = t.getTableAttributes();
                    for (Attribute att : attributes) {
                        JSONObject jsonobj = orderJsonObject();
                        jsonobj.put("name", att.getName());
                        if (att.getKey()) {
                            jsonobj.put("type", att.getType().replace("_", " "));
                        } else {
                            jsonobj.put("type", att.getType().replace("_", " "));
                        }
                        fields.put(jsonobj);

                    }
                    schemaParsing(fetchData(fields, t.getTableName()), t.getTableName());
                    index++;
                }

            }
        } else if (Objects.equals(schemaType, "One-One")) {
            JSONArray fields = new JSONArray();
            for (Table t : tables) {
                ArrayList<Attribute> attributes = t.getTableAttributes();
                for (Attribute att : attributes) {
                    JSONObject jsonobj = orderJsonObject();
                    jsonobj.put("name", att.getName());
                    if (att.getKey()) {
                        jsonobj.put("type", att.getType().replace("_", " "));
                    } else {
                        jsonobj.put("type", att.getType().replace("_", " "));
                    }
                    fields.put(jsonobj);

                }
            }
            schemaParsing(fetchData(fields, tblName), tblName);

        } else if (Objects.equals(schemaType, "One-Many") || Objects.equals(schemaType, "Many-One")) {

            double mean = JointProbabilityDistribution.mean;
            double sd = JointProbabilityDistribution.sd;

            boolean hasJPD = !Double.valueOf(mean).equals(0.0) && !Double.valueOf(sd).equals(0.0);

            if (tables.size() == 2) {
                //Total partial relationship
                //first table is primary table
                Table firstTable = tables.get(0);

                JSONArray fields = new JSONArray();
                String foreignKeyName = null;
                ArrayList<Attribute> attributes = firstTable.getTableAttributes();
                for (Attribute att : attributes) {
                    JSONObject jsonobj = orderJsonObject();
                    jsonobj.put("name", att.getName());
                    if (att.getKey()) {
                        foreignKeyName = att.getName(); //to check foreign key name in another table
                        jsonobj.put("type", "Row Number");
                    } else {
                        jsonobj.put("type", att.getType().replace("_", " "));
                    }
                    fields.put(jsonobj);

                }
                schemaParsing(fetchData(fields, firstTable.getTableName()), firstTable.getTableName());


                ArrayList<String> firstTablePrimaryIds = new ArrayList<>();
                for (Attribute att : attributes) {
                    if (att.getKey()) {
                        //getting primary key of primary table
                        for (int i = 0; i < rowCount; i++) {
                            JSONObject primaryTableObject = finalJSONArray.getJSONObject(0);
                            JSONArray primaryTableArray = primaryTableObject.getJSONArray("data");
                            JSONObject jsonObject1 = primaryTableArray.getJSONObject(i);
                            String value1 = jsonObject1.optString(att.getName());
                            firstTablePrimaryIds.add(value1);
                        }
                    }
                }

                ArrayList<String> distForeignkeyList = new ArrayList<>();

                //for two table probability distribution


                if (hasJPD && Objects.equals(foreignKeyName, JointProbabilityDistribution.tableName)) {
                    distForeignkeyList.addAll(createForeignKeyDistList(rowCount, mean, sd, firstTablePrimaryIds));
                } else {
                    for (int i = 0; i < firstTablePrimaryIds.size(); i++) {
                        distForeignkeyList.add(firstTablePrimaryIds.get(new Random().nextInt(firstTablePrimaryIds.size())));
                    }
                }
                JSONArray foreignkeyValues = new JSONArray();
                for (int i = 0; i < rowCount; i++) {
                    JSONObject row = new JSONObject();
                    row.put(foreignKeyName, distForeignkeyList.get(i));
                    foreignkeyValues.put(row);
                }

                fields.clear();
                attributes.clear(); // clear both list to use again

                //second table is merging of (relation+entity table)
                Table secondTable = tables.get(1);
                attributes = secondTable.getTableAttributes();
                for (Attribute att : attributes) {

                    if (!att.getName().equalsIgnoreCase(foreignKeyName)) {//foreignkey values will be insert from above table
                        JSONObject jsonobj = orderJsonObject();
                        jsonobj.put("name", att.getName());
                        if (att.getKey()) {

                            jsonobj.put("type", "Row Number");
                        } else {

                            jsonobj.put("type", att.getType().replace("_", " "));
                        }

                        fields.put(jsonobj);

                    }
                }

                JSONArray attributesValues = fetchData(fields, null);


                JSONArray finalValues = new JSONArray();

                for (int i = 0; i < attributesValues.length(); i++) {

                    JSONObject jso = mergeJSONObjects((JSONObject) attributesValues.get(i), (JSONObject) foreignkeyValues.get(i));
                    finalValues.put(jso);

                }
                schemaParsing(finalValues, secondTable.getTableName());

            } //for partial partial relation (0,1) (0,n)
            else if (tables.size() == 3) {

                ArrayList<List<String>> entityTablesPrimaryIds = new ArrayList<>();
                ArrayList<String> foreignKeyName = new ArrayList<String>();
                JSONArray finalValues = new JSONArray();
                for (int i = 0; i < tables.size(); i++) {

                    if (i == tables.size() - 1) { //for the last table - relation table

                        ArrayList<Attribute> attributes = tables.get(i).getTableAttributes();
                        String tableName = tables.get(i).getTableName();
                        String relationPriKey = "";
                        JSONArray fields = new JSONArray();
                        for (Attribute att : attributes) {
                            if (att.getKey()) {
                                relationPriKey = att.getName();
                            }
                            if (!att.getName().equalsIgnoreCase(foreignKeyName.get(0)) && !att.getName().equalsIgnoreCase(foreignKeyName.get(1))) {//foreignkey values will be inserted from above table and filtering only relation attributes
                                JSONObject jsonobj = orderJsonObject();
                                jsonobj.put("name", att.getName());
                                jsonobj.put("type", att.getType().replace("_", " "));

                                fields.put(jsonobj);

                            }
                        }
                        JSONArray relationAttributeValues = fetchData(fields, null);


                        //determine which of two foreign key is primary and select the values of primary randomly and uniquely
                        int primaryKeyIndex = Objects.equals(relationPriKey, foreignKeyName.get(0)) ? 0 : 1;
                        int foreignKeyIndex = !Objects.equals(relationPriKey, foreignKeyName.get(0)) ? 0 : 1;
                        Collections.shuffle(entityTablesPrimaryIds.get(primaryKeyIndex));

                        ArrayList<String> distForeignkeyList = new ArrayList<>();

                        if (hasJPD && Objects.equals(relationPriKey, JointProbabilityDistribution.tableName)) {
                            distForeignkeyList.addAll(createForeignKeyDistList(rowCount, mean, sd, (ArrayList<String>) entityTablesPrimaryIds.get(foreignKeyIndex)));
                        } else {
                            for (int j = 0; j < entityTablesPrimaryIds.get(foreignKeyIndex).size(); j++) {
                                distForeignkeyList.add(entityTablesPrimaryIds.get(foreignKeyIndex).get(new Random().nextInt(entityTablesPrimaryIds.get(foreignKeyIndex).size())));
                            }
                        }

                        //apply probability distribution to foreign key occurrance

                        // ArrayList<String> distForeignkeyList = createForeignKeyDistList(rowCount, mean, sd, (ArrayList<String>) entityTablesPrimaryIds.get(foreignKeyIndex));

                        //choose another foreign key value randomly

                        JSONArray foreignkeyValues = new JSONArray();
                        for (int j = 0; j < entityTablesPrimaryIds.get(foreignKeyIndex).size(); j++) {
                            JSONObject row = orderJsonObject();

                            //insert both foreign keys - one as primary key and other foreign key
                            row.put(foreignKeyName.get(primaryKeyIndex), entityTablesPrimaryIds.get(primaryKeyIndex).get(j));
                            row.put(foreignKeyName.get(foreignKeyIndex), distForeignkeyList.get(j));
                            foreignkeyValues.put(row);
                        }

                        // joining the values
                        for (int k = 0; k < relationAttributeValues.length(); k++) {

                            JSONObject jso = mergeJSONObjects((JSONObject) foreignkeyValues.get(k), (JSONObject) relationAttributeValues.get(k));
                            finalValues.put(jso);

                        }

                        schemaParsing(finalValues, tableName);

                    } else {
                        //for first two tables that are primary tables(entity table)
                        Table entityTable = tables.get(i);
                        JSONArray fields = new JSONArray();
                        ArrayList<Attribute> attributes = entityTable.getTableAttributes();
                        for (Attribute att : attributes) {
                            JSONObject jsonobj = orderJsonObject();
                            jsonobj.put("name", att.getName());
                            if (att.getKey()) {
                                foreignKeyName.add(att.getName()); //to check foreign key name in another table
                                jsonobj.put("type", "Row Number");

                            } else {
                                jsonobj.put("type", att.getType().replace("_", " "));
                            }
                            fields.put(jsonobj);

                        }


                        schemaParsing(fetchData(fields, entityTable.getTableName()), entityTable.getTableName());


                        //getting primary tables ids

                        ArrayList<String> entityTablePrimaryIds = new ArrayList<>();
                        for (Attribute att : attributes) {
                            if (att.getKey()) {//getting primary key of primary table
                                for (int j = 0; j < rowCount; j++) {
                                    JSONObject primaryTableObject = finalJSONArray.getJSONObject(i == 0 ? 0 : 1);
                                    JSONArray primaryTableArray = primaryTableObject.getJSONArray("data");
                                    JSONObject jsonObject1 = primaryTableArray.getJSONObject(j);
                                    String value1 = jsonObject1.optString(att.getName());
                                    entityTablePrimaryIds.add(value1);
                                }
                            }
                        }
                        entityTablesPrimaryIds.add(entityTablePrimaryIds);


                    }

                }
            }

        } else if (Objects.equals(schemaType, "Independent")) {
            for (Table t : tables) {
                JSONArray fields = new JSONArray();
                ArrayList<Attribute> attributes = t.getTableAttributes();
                for (Attribute att : attributes) {
                    JSONObject jsonobj = new JSONObject();
                    jsonobj.put("name", att.getName());
                    if (att.getKey()) {
                        jsonobj.put("type", "Row Number");
                    } else {
                        jsonobj.put("type", att.getType().replace("_", " "));
                    }
                    fields.put(jsonobj);

                }
                schemaParsing(fetchData(fields, t.getTableName()), t.getTableName());
            }

        } // for independent entity without any relation
    }

    public void generateSQLFile(JSONArray fields, String tableName) throws IOException { // separate code for generate sql file
        String fieldsStr = URLEncoder.encode(fields.toString(), StandardCharsets.UTF_8);
        String rowCountStr = URLEncoder.encode(rowCount + "", StandardCharsets.UTF_8);
        URL url = new URL("https://api.mockaroo.com/api/generate.sql?key=dc055d30&" +
                "array=true&count=" + rowCountStr + "&fields=" + fieldsStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/sql");
        OutputStream os = conn.getOutputStream();
        os.write(fields.toString().getBytes());
        os.flush();
        FileWriter out = new FileWriter("output/"
                + tableName + ".sql", false);

        String s = IOUtils.toString(conn.getInputStream(), String.valueOf(StandardCharsets.UTF_8));
        out.write(s);
        out.close();
    }

    public JSONArray fetchData(JSONArray fields, String tableName) throws IOException {
        String fieldsStr = URLEncoder.encode(fields.toString(), StandardCharsets.UTF_8);
        String rowCountStr = URLEncoder.encode(rowCount + "", StandardCharsets.UTF_8);
        URL url = new URL("https://api.mockaroo.com/api/generate.json?key=dc055d30&" +
                "array=true&count=" + rowCountStr + "&fields=" + fieldsStr);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");
        OutputStream os = conn.getOutputStream();
        os.write(fields.toString().getBytes());
        os.flush();
        if (tableName != null) { // not for relation id generation logic
            generateSQLFile(fields, tableName);
        }
        return new JSONArray(IOUtils.toString(conn.getInputStream(), String.valueOf(StandardCharsets.UTF_8)));
    }

    //putting data into finalJSONArray
    public void schemaParsing(JSONArray data, String tableName) {
        JSONObject rootJSONObject = new JSONObject();
        rootJSONObject.put("tableName", tableName);
        rootJSONObject.put("data", data);
        finalJSONArray.put(rootJSONObject);
    }


    public void generateCSV(JSONArray fields, String tableName) {
        try (FileWriter out = new FileWriter("output/"
                + tableName + ".csv", false)) {
            JSONObject rootJSONObject = new JSONObject();
            rootJSONObject.put("data", fields);
            String csv = CDL.toString(fields);
            out.write(csv);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONObject mergeJSONObjects(JSONObject json1, JSONObject json2) {
        JSONObject mergedJSON = orderJsonObject();


        for (String key : JSONObject.getNames(json1)) {
            mergedJSON.put(key, json1.get(key));
        }

        for (String key : JSONObject.getNames(json2)) {
            mergedJSON.put(key, json2.get(key));
        }
        return mergedJSON;
    }

    public JSONObject orderJsonObject() {

        JSONObject jso = new JSONObject();

        try {
            Field changeMap = jso.getClass().getDeclaredField("map");
            changeMap.setAccessible(true);
            changeMap.set(jso, new LinkedHashMap<>());
            changeMap.setAccessible(false);

        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return jso;
    }

    //apply gaussian distribution to foreign key
    public ArrayList<String> createForeignKeyDistList(int noOfRow, double mean, double sd, ArrayList<String> foreignKeyList) {
        ArrayList<String> result = new ArrayList<>();
        int count = 0;
        int index = 0;
        Collections.shuffle(foreignKeyList);
        while (count < noOfRow) {
            //use gaussian probability distribution for occurrence of each foreign key
            int gd = (int) Math.round(new Random().nextGaussian() * sd + mean);
            String foreignkeyId = foreignKeyList.get(index++);
            for (int i = 0; i < gd; i++) {
                result.add(foreignkeyId);
                count++;
                if (count == noOfRow) {
                    break;
                }
            }
        }
        Collections.shuffle(result);
        return result;
    }


}