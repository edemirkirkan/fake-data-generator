package com.cs4221.database;

import com.cs4221.distributions.IntegerDistribution;
import com.cs4221.distributions.MultipleColumnIntegerDistribution;
import com.cs4221.distributions.MultipleColumnRealDistribution;
import com.cs4221.distributions.RealDistribution;
import org.apache.commons.math3.distribution.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Database {
    private final ArrayList<Table> tables;
    private final ArrayList<SingleColumnDistribution> singleColumnDistributions;
    private final ArrayList<MultipleColumnDistribution> multipleColumnDistributions;

    public Database() {
        tables = new ArrayList<>();
        singleColumnDistributions = new ArrayList<>();
        multipleColumnDistributions = new ArrayList<>();
    }

    public void addTable(Table table) {
        if (!isUnique(table.getTableName())) {
            System.out.println("There is already a entity/relation in the system named '" + table.getTableName() +
                    "'\nConsider changing the name and try again" +
                    "\nAll tables/entities can be seen using SHOW DIAGRAM command.");
            return;
        }
        if (table instanceof Entity) {
            tables.add(table);
            System.out.println("Entity is successfully created");
        } else {
            Relation relation = (Relation) table;
            if (!validRelation(relation.getLeftTableName(), relation.getRightTableName())) {
                System.out.println("Relation cannot be created as related entities do not exist in the system" +
                        "\nAll entities can be seen using SHOW ENTITIES command.");
                return;
            }
            tables.add(table);
            System.out.println("Relation is successfully created");
        }
    }

    private boolean validRelation(String leftTableName, String rightTableName) {
        boolean leftFound = false;
        boolean rightFound = false;
        for (Table t : tables) {
            if (t instanceof Entity && t.getTableName().equalsIgnoreCase(leftTableName)) {
                leftFound = true;
            }
            if (t instanceof Entity && t.getTableName().equalsIgnoreCase(rightTableName)) {
                rightFound = true;
            }
        }
        return leftFound && rightFound;
    }

    public void removeEntity(String entityName) {
        for (Table t : tables) {
            if (t instanceof Entity && t.getTableName().equalsIgnoreCase(entityName)) {
                tables.remove(t);
                System.out.println("Entity is successfully removed");
                return;
            }
        }
        System.out.println("There is no entity named '" + entityName + "' in the system");
    }

    public void removeRelation(String relationName) {
        for (Table t : tables) {
            if (t instanceof Relation && t.getTableName().equalsIgnoreCase(relationName)) {
                tables.remove(t);
                System.out.println("Relation is successfully removed");
                return;
            }
        }
        System.out.println("There is no relation named '" + relationName + "' in the system");
    }

    public void showEntities() {
        boolean found = false;
        for (Table table : tables) {
            if (table instanceof Entity) {
                found = true;
                System.out.println(table);
            }
        }
        if (!found) {
            System.out.println("Currently, there is no entity in the system");
        }
    }

    public void showRelations() {
        boolean found = false;
        for (Table table : tables) {
            if (table instanceof Relation) {
                found = true;
                System.out.println(table);
            }
        }
        if (!found) {
            System.out.println("Currently, there is no relation in the system");
        }
    }

    public void showDiagram() {
        for (Table table : tables) {
            System.out.println(table);
        }
    }

    private boolean isUnique(String tableName) {
        for (Table t : tables) {
            if (t.getTableName().equalsIgnoreCase(tableName)) {
                return false;
            }
        }
        return true;
    }

    public void generateData(int rowCount) throws IOException, NoSuchMethodException {
        DataCount fake = new DataCount(rowCount, tables, this);
    }

    public void setBinomialDistribution(JSONArray data, String tableName, String attributeName,
                                        int trials, double p, int sampleSize) {
        ArrayList<Integer> values = IntegerDistribution.generateBinomialValues(trials, p, sampleSize);
        IntegerDistribution.setIntegerDistValues(data, tableName, attributeName, values);
    }

    public void setUniformIntegerDistribution(JSONArray data, String tableName, String attributeName,
                                              int lower, int upper, int sampleSize) {
        ArrayList<Integer> values = IntegerDistribution.generateUniformIntegerValues(lower, upper, sampleSize);
        IntegerDistribution.setIntegerDistValues(data, tableName, attributeName, values);
    }

    public void setUniformRealDistribution(JSONArray data, String tableName, String attributeName,
                                           double lower, double upper, int sampleSize) {
        ArrayList<Double> values = RealDistribution.generateUniformRealValues(lower, upper, sampleSize);
        RealDistribution.setRealDistValues(data, tableName, attributeName, values);
    }

    public void setNormalDistribution(JSONArray data, String tableName, String attributeName,
                                      double mean, double sd, int sampleSize) {
        ArrayList<Double> values = RealDistribution.generateNormalValues(mean, sd, sampleSize);
        RealDistribution.setRealDistValues(data, tableName, attributeName, values);
    }

    public void setGeometricDistribution(JSONArray data, String tableName, String attributeName,
                                         double p, int sampleSize) {
        ArrayList<Integer> values = IntegerDistribution.generateGeometricValues(p, sampleSize);
        IntegerDistribution.setIntegerDistValues(data, tableName, attributeName, values);
    }

    public void setPoissonDistribution(JSONArray data, String tableName, String attributeName,
                                       double p, int sampleSize) {
        ArrayList<Integer> values = IntegerDistribution.generatePoissonValues(p, sampleSize);
        IntegerDistribution.setIntegerDistValues(data, tableName, attributeName, values);
    }

    private void setExponentialDistribution(JSONArray data, String tableName, String attributeName,
                                            double mean, int sampleSize) {
        ArrayList<Double> values = RealDistribution.generateExponentialValues(mean, sampleSize);
        RealDistribution.setRealDistValues(data, tableName, attributeName, values);
    }

    private void setBetaDistribution(JSONArray data, String tableName, String attributeName,
                                     double alpha, double beta, int sampleSize) {
        ArrayList<Double> values = RealDistribution.generateBetaValues(alpha, beta, sampleSize);
        RealDistribution.setRealDistValues(data, tableName, attributeName, values);
    }

    private void setCauchyDistribution(JSONArray data, String tableName, String attributeName,
                                       double median, double scale, int sampleSize) {
        ArrayList<Double> values = RealDistribution.generateCauchyValues(median, scale, sampleSize);
        RealDistribution.setRealDistValues(data, tableName, attributeName, values);
    }

    private void setLogisticDistribution(JSONArray data, String tableName, String attributeName,
                                         double mu, double s, int sampleSize) {
        ArrayList<Double> values = RealDistribution.generateLogisticValues(mu, s, sampleSize);
        RealDistribution.setRealDistValues(data, tableName, attributeName, values);
    }

    private void setTDistribution(JSONArray data, String tableName, String attributeName,
                                  double degreesOfFreedom, int sampleSize) {
        ArrayList<Double> values = RealDistribution.generateTValues(degreesOfFreedom, sampleSize);
        RealDistribution.setRealDistValues(data, tableName, attributeName, values);
    }

    private void setChiSquareDistribution(JSONArray data, String tableName, String attributeName,
                                          double degreesOfFreedom, int sampleSize) {
        ArrayList<Double> values = RealDistribution.generateChiSquaredValues(degreesOfFreedom, sampleSize);
        RealDistribution.setRealDistValues(data, tableName, attributeName, values);
    }

    public JSONArray applySingleColumnDistributions(JSONArray data, int sampleSize) {
        for (SingleColumnDistribution dist : singleColumnDistributions) {

            String tableName = dist.getTableName();
            String attributeName = dist.getAttributeName();

            switch (dist.getType().toLowerCase()) {
            case "binomial":
                int trials = Integer.parseInt(dist.getParam1());
                double p = Double.parseDouble(dist.getParam2());
                setBinomialDistribution(data, tableName, attributeName, trials, p, sampleSize);
                break;
            case "geometric":
                double probability = Double.parseDouble(dist.getParam1());
                setGeometricDistribution(data, tableName, attributeName, probability, sampleSize);
                break;
            case "uniform_integer":
                int lower = Integer.parseInt(dist.getParam1());
                int upper = Integer.parseInt(dist.getParam2());
                setUniformIntegerDistribution(data, tableName, attributeName, lower, upper, sampleSize);
                break;
            case "uniform_real":
                double lower_double = Double.parseDouble(dist.getParam1());
                double upper_double = Double.parseDouble(dist.getParam2());
                setUniformRealDistribution(data, tableName, attributeName, lower_double, upper_double, sampleSize);
                break;
            case "normal":
                double normal_mean = Double.parseDouble(dist.getParam1());
                double sd = Double.parseDouble(dist.getParam2());
                setNormalDistribution(data, tableName, attributeName, normal_mean, sd, sampleSize);
                break;
            case "poisson":
                double prob = Double.parseDouble(dist.getParam1());
                setPoissonDistribution(data, tableName, attributeName, prob, sampleSize);
                break;
            case "exponential":
                double exponential_mean = Double.parseDouble(dist.getParam1());
                setExponentialDistribution(data, tableName, attributeName, exponential_mean, sampleSize);
                break;
            case "beta":
                double alpha = Double.parseDouble(dist.getParam1());
                double beta = Double.parseDouble(dist.getParam2());
                setBetaDistribution(data, tableName, attributeName, alpha, beta, sampleSize);
                break;
            case "cauchy":
                double median = Double.parseDouble(dist.getParam1());
                double scale = Double.parseDouble(dist.getParam2());
                setCauchyDistribution(data, tableName, attributeName, median, scale, sampleSize);
                break;
            case "logistic":
                double mu = Double.parseDouble(dist.getParam1());
                double s = Double.parseDouble(dist.getParam2());
                setLogisticDistribution(data, tableName, attributeName, mu, s, sampleSize);
                break;
            case "t":
                double degreesOfFreedom = Double.parseDouble(dist.getParam1());
                setTDistribution(data, tableName, attributeName, degreesOfFreedom, sampleSize);
                break;
            case "chisquare":
                double degreeOfFreedom = Double.parseDouble(dist.getParam1());
                setChiSquareDistribution(data, tableName, attributeName, degreeOfFreedom, sampleSize);
                break;
            default:
                return new JSONArray();
            }
        }
        return data;
    }

    private ArrayList<Object> getColumnDataFromDatabase(JSONArray data, String tableName, String attributeName) {
        for (int i = 0, n = data.length(); i < n; i++) {
            JSONObject table = data.getJSONObject(i);
            if (table.getString("tableName").equalsIgnoreCase(tableName)) {
                return getColumnDataFromTable(table.getJSONArray("data"), attributeName);
            }
        }

        return null;
    }

    private ArrayList<Object> getColumnDataFromTable(JSONArray data, String attributeName) {
        ArrayList<Object> columnData = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            JSONObject row = data.getJSONObject(i);
            Object value = row.get(attributeName);
            String string = String.valueOf(value);
            columnData.add(string);
        }

        return columnData;
    }

    private void setMultipleColumnIntegerDistribution(JSONArray data, String tableName, String attributeName1,
                                                      String attributeName2, Class<?> type,
                                                      HashMap<Object, ArrayList<Number>> mapToDistributionParams) {
        ArrayList<Object> independentColumnValues = getColumnDataFromDatabase(data, tableName, attributeName1);
        ArrayList<Integer> values = MultipleColumnIntegerDistribution.getMultiColIntegerDistributionValues(
                independentColumnValues, type, mapToDistributionParams);
        IntegerDistribution.setIntegerDistValues(data, tableName, attributeName2, values);
    }

    private void setMultipleColumnRealDistribution(JSONArray data, String tableName, String attributeName1,
                                                   String attributeName2, Class<?> type,
                                                   HashMap<Object, ArrayList<Number>> mapToDistributionParams) {
        ArrayList<Object> independentColumnValues = getColumnDataFromDatabase(data, tableName, attributeName1);
        ArrayList<Double> values = MultipleColumnRealDistribution.getMultiColRealDistributionValues(
                independentColumnValues, type, mapToDistributionParams);
        RealDistribution.setRealDistValues(data, tableName, attributeName2, values);
    }

    public JSONArray applyMultipleColumnDistributions(JSONArray data) {
        for (MultipleColumnDistribution dist : multipleColumnDistributions) {
            String tableName = dist.getTableName();
            String attributeName1 = dist.getAttributeName1();
            String attributeName2 = dist.getAttributeName2();
            HashMap<Object, ArrayList<Number>> mapToDistributionParams = dist.getMapToDistributionParams();
            switch (dist.getType().toLowerCase()) {
                case "binomial":
                    setMultipleColumnIntegerDistribution(data, tableName, attributeName1, attributeName2,
                            BinomialDistribution.class, mapToDistributionParams);
                    break;
                case "geometric":
                    setMultipleColumnIntegerDistribution(data, tableName, attributeName1, attributeName2,
                            GeometricDistribution.class, mapToDistributionParams);
                    break;
                case "uniform_integer":
                    setMultipleColumnIntegerDistribution(data, tableName, attributeName1, attributeName2,
                            UniformIntegerDistribution.class, mapToDistributionParams);
                    break;
                case "poisson":
                    setMultipleColumnIntegerDistribution(data, tableName, attributeName1, attributeName2,
                            PoissonDistribution.class, mapToDistributionParams);
                    break;
                case "uniform_real":
                    setMultipleColumnRealDistribution(data, tableName, attributeName1, attributeName2,
                            UniformRealDistribution.class, mapToDistributionParams);
                    break;
                case "normal":
                    setMultipleColumnRealDistribution(data, tableName, attributeName1, attributeName2,
                            NormalDistribution.class, mapToDistributionParams);
                    break;
                case "exponential":
                    setMultipleColumnRealDistribution(data, tableName, attributeName1, attributeName2,
                            ExponentialDistribution.class, mapToDistributionParams);
                    break;
                case "beta":
                    setMultipleColumnRealDistribution(data, tableName, attributeName1, attributeName2,
                            BetaDistribution.class, mapToDistributionParams);
                    break;
                case "cauchy":
                    setMultipleColumnRealDistribution(data, tableName, attributeName1, attributeName2,
                            CauchyDistribution.class, mapToDistributionParams);
                    break;
                case "logistic":
                    setMultipleColumnRealDistribution(data, tableName, attributeName1, attributeName2,
                            LogisticDistribution.class, mapToDistributionParams);
                    break;
                case "t":
                    setMultipleColumnRealDistribution(data, tableName, attributeName1, attributeName2,
                            TDistribution.class, mapToDistributionParams);
                    break;
                case "chisquare":
                    setMultipleColumnRealDistribution(data, tableName, attributeName1, attributeName2,
                            ChiSquaredDistribution.class, mapToDistributionParams);
                    break;
                default:
                    return new JSONArray();
            }
        }
        return data;
    }

    public void addSingleColumnDistribution(SingleColumnDistribution dist) {
        singleColumnDistributions.add(dist);
        System.out.println("Distribution constraint is successfully added.");
    }

    public void addMultipleColumnDistribution(MultipleColumnDistribution dist) {
        multipleColumnDistributions.add(dist);
        System.out.println("Multiple Column distribution constraint is successfully added.");
    }
    public void addJointProbabilityDistribution(String mean, String sd, String tableName){
        JointProbabilityDistribution.mean = Double.parseDouble(mean);
        JointProbabilityDistribution.sd = Double.parseDouble(sd);
        JointProbabilityDistribution.tableName = tableName;
        System.out.println("Joint probability distribution is successfully added.");
    }
}
