package com.cs4221.database;

import com.cs4221.distributions.IntegerDistribution;
import com.cs4221.distributions.RealDistribution;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;

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

    public JSONArray applyMultipleColumnDistributions(JSONArray data, int sampleSize) {
        for (MultipleColumnDistribution dist : multipleColumnDistributions) {
            String tableName1 = dist.getTableName1();
            String attributeName1 = dist.getAttributeName1();
            String tableName2 = dist.getTableName2();
            String attributeName2 = dist.getAttributeName2();
            switch (dist.getType().toLowerCase()) {
            case "binomial":

                break;
            case "geometric":

                break;
            case "uniform_integer":

                break;
            case "uniform_real":

                break;
            case "normal":

                break;
            case "poisson":

                break;
            case "exponential":

                break;
            case "beta":

                break;
            case "cauchy":

                break;
            case "logistic":

                break;
            case "t":

                break;
            case "chisquare":

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
        System.out.println("Joint distribution constraint is successfully added.");
    }
}
