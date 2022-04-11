package com.cs4221.distributions;

import org.apache.commons.math3.distribution.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MultipleColumnIntegerDistribution {
    public static ArrayList<Integer> getMultiColIntegerDistributionValues(
            ArrayList<Object> independentColumn,
            Class<?> distributionType,
            HashMap<Object, ArrayList<Number>> mapToDistributionParams) {
        // Generate map from independent column value to distribution instance.
        HashMap<Object, ? extends AbstractIntegerDistribution> distributions = getIntegerDistributionFromParams(
                distributionType,
                mapToDistributionParams);

        // Generate dependent column value based on independent column value and the distribution.
        ArrayList<Integer> output = new ArrayList<>();
        for (Object independentColumnValue : independentColumn) {
            assert distributions != null;
            AbstractIntegerDistribution distribution = distributions.get(independentColumnValue);
            if (distribution == null) {
                distribution = distributions.get(null);
            }
            Integer value = distribution.sample();
            output.add(value);
        }

        return output;
    }

    // Generate map from independent column value to distribution instance
    // from distribution type and map from independent column value to parameters.
    public static HashMap<Object, ? extends AbstractIntegerDistribution> getIntegerDistributionFromParams(
            Class<?> distributionType,
            HashMap<Object, ArrayList<Number>> mapToDistributionParams) {
        if (UniformIntegerDistribution.class.equals(distributionType)) {
            return getUniformIntegerDistribution(mapToDistributionParams);
        } else if (BinomialDistribution.class.equals(distributionType)) {
            return getBinomialDistribution(mapToDistributionParams);
        } else if (GeometricDistribution.class.equals(distributionType)) {
            return getGeometricDistribution(mapToDistributionParams);
        } else if (PoissonDistribution.class.equals(distributionType)) {
            return getPoissonDistribution(mapToDistributionParams);
        }
        return null;
    }

    public static HashMap<Object, UniformIntegerDistribution> getUniformIntegerDistribution(
            HashMap<Object, ArrayList<Number>> mapToDistributionParams) {
        HashMap<Object, UniformIntegerDistribution> map = new HashMap<>();
        for (Map.Entry<Object, ArrayList<Number>> entry : mapToDistributionParams.entrySet()) {
            Object independentColumnValue = entry.getKey();
            ArrayList<Number> distributionParams = entry.getValue();
            map.put(independentColumnValue,
                    new UniformIntegerDistribution(distributionParams.get(0).intValue(),
                            distributionParams.get(1).intValue()));
        }

        return map;
    }

    public static HashMap<Object, BinomialDistribution> getBinomialDistribution(
            HashMap<Object, ArrayList<Number>> mapToDistributionParams) {
        HashMap<Object, BinomialDistribution> map = new HashMap<>();
        for (Map.Entry<Object, ArrayList<Number>> entry : mapToDistributionParams.entrySet()) {
            Object independentColumnValue = entry.getKey();
            ArrayList<Number> distributionParams = entry.getValue();
            map.put(independentColumnValue,
                    new BinomialDistribution(distributionParams.get(0).intValue(),
                            distributionParams.get(1).doubleValue()));
        }

        return map;
    }

    public static HashMap<Object, GeometricDistribution> getGeometricDistribution(
            HashMap<Object, ArrayList<Number>> mapToDistributionParams) {
        HashMap<Object, GeometricDistribution> map = new HashMap<>();
        for (Map.Entry<Object, ArrayList<Number>> entry : mapToDistributionParams.entrySet()) {
            Object independentColumnValue = entry.getKey();
            ArrayList<Number> distributionParams = entry.getValue();
            map.put(independentColumnValue,
                    new GeometricDistribution(distributionParams.get(0).doubleValue()));
        }

        return map;
    }

    public static HashMap<Object, PoissonDistribution> getPoissonDistribution(
            HashMap<Object, ArrayList<Number>> mapToDistributionParams) {
        HashMap<Object, PoissonDistribution> map = new HashMap<>();
        for (Map.Entry<Object, ArrayList<Number>> entry : mapToDistributionParams.entrySet()) {
            Object independentColumnValue = entry.getKey();
            ArrayList<Number> distributionParams = entry.getValue();
            map.put(independentColumnValue,
                    new PoissonDistribution(distributionParams.get(0).doubleValue()));
        }

        return map;
    }
}