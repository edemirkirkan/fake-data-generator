package com.cs4221.distributions;

import org.apache.commons.math3.distribution.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class IntegerDistribution {

    public static void setIntegerDistValues(JSONArray data, String tableName, String attributeName,
                                            ArrayList<Integer> values) {
        for (int i = 0, n = data.length(); i < n; i++) {
            JSONObject table = data.getJSONObject(i);
            if (table.getString("tableName").equalsIgnoreCase(tableName)) {
                setTableIntegerDistValues(table.getJSONArray("data"), attributeName, values);
            }
        }
    }

    private static void setTableIntegerDistValues(JSONArray data, String attributeName, ArrayList<Integer> values) {
        for (int i = 0; i < data.length(); i++) {
            JSONObject row = data.getJSONObject(i);
            row.put(attributeName, values.get(i));
        }
    }

    public static ArrayList<Integer> generateUniformIntegerValues(int lower, int upper, int sampleSize) {
        return generateSampleValues(new UniformIntegerDistribution(lower, upper), sampleSize);
    }

    public static ArrayList<Integer> generateGeometricValues(double p, int sampleSize) {
        return generateSampleValues(new GeometricDistribution(p), sampleSize);
    }

    public static ArrayList<Integer> generateBinomialValues(int trials, double p, int sampleSize) {
        return generateSampleValues(new BinomialDistribution(trials, p), sampleSize);
    }

    public static ArrayList<Integer> generatePoissonValues(double p, int sampleSize) {
        return generateSampleValues(new PoissonDistribution(p), sampleSize);
    }

    private static ArrayList<Integer> generateSampleValues(AbstractIntegerDistribution distribution, int sampleSize) {
        ArrayList<Integer> values = new ArrayList<>();
        for (int i = 0; i < sampleSize; i++) {
            values.add(distribution.sample());
        }
        return values;
    }
}
