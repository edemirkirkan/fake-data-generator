package com.cs4221.distributions;

import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.Precision;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class  RealDistribution {

    public static void setRealDistValues(JSONArray data, String tableName, String attributeName, ArrayList<Double> values) {
        for (int i = 0; i < data.length(); i++) {
            JSONObject table = data.getJSONObject(i);
            if (table.getString("tableName").equals(tableName)) {
                setTableRealDistValues(table.getJSONArray("data"), attributeName, values);
            }
        }
    }

    private static void setTableRealDistValues(JSONArray rows, String attributeName, ArrayList<Double> values) {
        for (int i = 0; i < rows.length(); i++) {
            JSONObject row = rows.getJSONObject(i);
            row.put(attributeName, values.get(i));
        }
    }

    public static ArrayList<Double> generateUniformRealValues(Double lower, Double upper, int sampleSize) {
        return generateSampleValues(new UniformRealDistribution(lower, upper), sampleSize);
    }

    public static ArrayList<Double> generateNormalValues(Double mean, Double sd, int sampleSize) {
        return generateSampleValues(new NormalDistribution(mean, sd), sampleSize);
    }

    public static ArrayList<Double> generateExponentialValues(Double mean, int sampleSize) {
        return generateSampleValues(new ExponentialDistribution(mean), sampleSize);
    }

    public static ArrayList<Double> generateBetaValues(Double alpha, Double beta, int sampleSize) {
        return generateSampleValues(new BetaDistribution(alpha, beta), sampleSize);
    }

    public static ArrayList<Double> generateCauchyValues(Double median, Double scale, int sampleSize) {
        return generateSampleValues(new CauchyDistribution(median, scale), sampleSize);
    }

    public static ArrayList<Double> generateLogisticValues(Double mu, Double s, int sampleSize) {
        return generateSampleValues(new LogisticDistribution(mu, s), sampleSize);
    }

    public static ArrayList<Double> generateTValues(Double degreesOfFreedom, int sampleSize) {
        return generateSampleValues(new TDistribution(degreesOfFreedom), sampleSize);
    }

    public static ArrayList<Double> generateChiSquaredValues(Double degreesOfFreedom, int sampleSize) {
        return generateSampleValues(new ChiSquaredDistribution(degreesOfFreedom), sampleSize);
    }

    private static ArrayList<Double> generateSampleValues(AbstractRealDistribution distribution, int sampleSize) {
        ArrayList<Double> values = new ArrayList<>();
        for (int i = 0; i < sampleSize; i++) {
            values.add(Precision.round(distribution.sample(), 2));
        }
        return values;
    }

}
