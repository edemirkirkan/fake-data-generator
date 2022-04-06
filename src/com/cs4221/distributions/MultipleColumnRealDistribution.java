package com.cs4221.distributions;

import org.apache.commons.math3.distribution.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MultipleColumnRealDistribution {
    public static ArrayList<Double> getMultiColRealDistributionValues(
            ArrayList<Object> independentColumn,
            Class<?> distributionType,
            HashMap<Object, ArrayList<Number>> mapToDistributionParams)
    {
        // Generate map from independent column value to distribution instance.
        HashMap<Object, ? extends AbstractRealDistribution> distributions = getRealDistributionFromParams(
                distributionType,
                mapToDistributionParams);

        // Generate dependent column value based on independent column value and the distribution.
        ArrayList<Double> output = new ArrayList<>();
        for (Object independentColumnValue: independentColumn) {
            AbstractRealDistribution distribution = distributions.get(independentColumnValue);
            Double value = distribution.sample();
            output.add(value);
        }

        return output;
    }

    // Generate map from independent column value to distribution instance
    // from distribution type and map from independent column value to parameters.
    public static HashMap<Object, ? extends AbstractRealDistribution> getRealDistributionFromParams(
            Class<?> distributionType,
            HashMap<Object, ArrayList<Number>> mapToDistributionParams)
    {
        if (UniformRealDistribution.class.equals(distributionType)) {
            return getUniformRealDistribution(mapToDistributionParams);
        } else if (NormalDistribution.class.equals(distributionType)) {
            return getNormalDistribution(mapToDistributionParams);
        } else if (ExponentialDistribution.class.equals(distributionType)) {
            return getExponentialDistribution(mapToDistributionParams);
        } else if (BetaDistribution.class.equals(distributionType)) {
            return getBetaDistribution(mapToDistributionParams);
        } else if (CauchyDistribution.class.equals(distributionType)) {
            return getCauchyDistribution(mapToDistributionParams);
        } else if (LogisticDistribution.class.equals(distributionType)) {
            return getLogisticDistribution(mapToDistributionParams);
        } else if (TDistribution.class.equals(distributionType)) {
            return getTDistribution(mapToDistributionParams);
        } else if (ChiSquaredDistribution.class.equals(distributionType)) {
            return getChiSquaredDistribution(mapToDistributionParams);
        }
        return null;
    }

    public static HashMap<Object, UniformRealDistribution> getUniformRealDistribution(
            HashMap<Object, ArrayList<Number>> mapToDistributionParams)
    {
        HashMap<Object, UniformRealDistribution> map = new HashMap<>();
        for (Map.Entry<Object, ArrayList<Number>> entry: mapToDistributionParams.entrySet()) {
            Object independentColumnValue = entry.getKey();
            ArrayList<Number> distributionParams = entry.getValue();
            map.put(independentColumnValue,
                    new UniformRealDistribution(distributionParams.get(0).doubleValue(),
                            distributionParams.get(1).doubleValue()));
        }

        return map;
    }

    public static HashMap<Object, NormalDistribution> getNormalDistribution(
            HashMap<Object, ArrayList<Number>> mapToDistributionParams)
    {
        HashMap<Object, NormalDistribution> map = new HashMap<>();
        for (Map.Entry<Object, ArrayList<Number>> entry: mapToDistributionParams.entrySet()) {
            Object independentColumnValue = entry.getKey();
            ArrayList<Number> distributionParams = entry.getValue();
            map.put(independentColumnValue,
                    new NormalDistribution(distributionParams.get(0).doubleValue(),
                            distributionParams.get(1).doubleValue()));
        }

        return map;
    }

    public static HashMap<Object, ExponentialDistribution> getExponentialDistribution(
            HashMap<Object, ArrayList<Number>> mapToDistributionParams)
    {
        HashMap<Object, ExponentialDistribution> map = new HashMap<>();
        for (Map.Entry<Object, ArrayList<Number>> entry: mapToDistributionParams.entrySet()) {
            Object independentColumnValue = entry.getKey();
            ArrayList<Number> distributionParams = entry.getValue();
            map.put(independentColumnValue,
                    new ExponentialDistribution(distributionParams.get(0).doubleValue()));
        }

        return map;
    }

    public static HashMap<Object, BetaDistribution> getBetaDistribution(
            HashMap<Object, ArrayList<Number>> mapToDistributionParams)
    {
        HashMap<Object, BetaDistribution> map = new HashMap<>();
        for (Map.Entry<Object, ArrayList<Number>> entry: mapToDistributionParams.entrySet()) {
            Object independentColumnValue = entry.getKey();
            ArrayList<Number> distributionParams = entry.getValue();
            map.put(independentColumnValue,
                    new BetaDistribution(distributionParams.get(0).doubleValue(),
                            distributionParams.get(1).doubleValue()));
        }

        return map;
    }

    public static HashMap<Object, CauchyDistribution> getCauchyDistribution(
            HashMap<Object, ArrayList<Number>> mapToDistributionParams)
    {
        HashMap<Object, CauchyDistribution> map = new HashMap<>();
        for (Map.Entry<Object, ArrayList<Number>> entry: mapToDistributionParams.entrySet()) {
            Object independentColumnValue = entry.getKey();
            ArrayList<Number> distributionParams = entry.getValue();
            map.put(independentColumnValue,
                    new CauchyDistribution(distributionParams.get(0).doubleValue(),
                            distributionParams.get(1).doubleValue()));
        }

        return map;
    }

    public static HashMap<Object, LogisticDistribution> getLogisticDistribution(
            HashMap<Object, ArrayList<Number>> mapToDistributionParams)
    {
        HashMap<Object, LogisticDistribution> map = new HashMap<>();
        for (Map.Entry<Object, ArrayList<Number>> entry: mapToDistributionParams.entrySet()) {
            Object independentColumnValue = entry.getKey();
            ArrayList<Number> distributionParams = entry.getValue();
            map.put(independentColumnValue,
                    new LogisticDistribution(distributionParams.get(0).doubleValue(),
                            distributionParams.get(1).doubleValue()));
        }

        return map;
    }

    public static HashMap<Object, TDistribution> getTDistribution(
            HashMap<Object, ArrayList<Number>> mapToDistributionParams)
    {
        HashMap<Object, TDistribution> map = new HashMap<>();
        for (Map.Entry<Object, ArrayList<Number>> entry: mapToDistributionParams.entrySet()) {
            Object independentColumnValue = entry.getKey();
            ArrayList<Number> distributionParams = entry.getValue();
            map.put(independentColumnValue,
                    new TDistribution(distributionParams.get(0).doubleValue()));
        }

        return map;
    }

    public static HashMap<Object, ChiSquaredDistribution> getChiSquaredDistribution(
            HashMap<Object, ArrayList<Number>> mapToDistributionParams)
    {
        HashMap<Object, ChiSquaredDistribution> map = new HashMap<>();
        for (Map.Entry<Object, ArrayList<Number>> entry: mapToDistributionParams.entrySet()) {
            Object independentColumnValue = entry.getKey();
            ArrayList<Number> distributionParams = entry.getValue();
            map.put(independentColumnValue,
                    new ChiSquaredDistribution(distributionParams.get(0).doubleValue()));
        }

        return map;
    }

}
