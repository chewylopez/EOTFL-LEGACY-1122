package com.chewylopez.echoofthefarlands.world.farlands;

import com.chewylopez.echoofthefarlands.Config;

public final class FarlandsNoise {

    public static final double NOISE_SCALAR_CONSTANT = 171.103;
    public static final double FARLANDS_ORIGINAL = 12550823;
    private static final double LINEAR_SCALAR_FACTOR = Math.pow(10.0, 15.0);

    private FarlandsNoise() {}

    private static double getFarlandsLocation() {
        return Config.getFarlandsLocation();
    }

    private static int getFarlandsGenType() {
        return Config.getFarlandsGenType();
    }

    private static double getStartPosDelta() {
        return FARLANDS_ORIGINAL - getFarlandsLocation();
    }

    private static double getScaledFarlandsLocation() {
        return getFarlandsLocation() * NOISE_SCALAR_CONSTANT;
    }

    private static double getApproxFarlandsProgress(double value) {
        return (Math.abs(value) - getScaledFarlandsLocation()) / NOISE_SCALAR_CONSTANT;
    }

    private static double expScaleFactorStatic(double value) {
        return 1 + (2 * ((getApproxFarlandsProgress(Math.abs(value))) / 1000000));
    }

    private static double expScaleFactorProportional(double value) {
        return 1 + (2 * (getApproxFarlandsProgress(Math.abs(value)) / (getFarlandsLocation())));
    }

    public static double transform(double trueValue, double vanillaWrapped) {
        if (Math.abs(trueValue) <= getScaledFarlandsLocation()) {
            return vanillaWrapped;
        }

        if (getFarlandsLocation() > FARLANDS_ORIGINAL) {
            return farlandsDelayedNoise(trueValue);
        }

        switch (getFarlandsGenType()) {
            case 0:
                return piecewiseStaticScaling(trueValue, vanillaWrapped);
            case 1:
                return piecewiseProportionalScaling(trueValue, vanillaWrapped);
            case 2:
                return offsetUnscaledGeneration(trueValue);
            case 3:
                return exponentialScalingStatic(trueValue);
            case 4:
                return exponentialScalingProportional(trueValue);
            case 5:
                return linearScaling(trueValue);
            case 6:
                return sineWaveScaling(trueValue);
            default:
                return vanillaWrapped;
        }
    }

    private static double offsetUnscaledGeneration(double value) {
        if (value >= 0) {
            return value + (getStartPosDelta() * NOISE_SCALAR_CONSTANT);
        }
        return value - (getStartPosDelta() * NOISE_SCALAR_CONSTANT);
    }

    private static double farlandsDelayedNoise(double value) {
        return value * (FARLANDS_ORIGINAL / getFarlandsLocation());
    }

    private static double exponentialScalingStatic(double value) {
        if (value >= 0) {
            return Math.pow(Integer.MAX_VALUE, expScaleFactorStatic(value));
        }
        return -Math.pow(Integer.MAX_VALUE, expScaleFactorStatic(value));
    }

    private static double exponentialScalingProportional(double value) {
        if (value >= 0) {
            return Math.pow(Integer.MAX_VALUE, expScaleFactorProportional(value));
        }
        return -Math.pow(Integer.MAX_VALUE, expScaleFactorProportional(value));
    }

    private static double linearScaling(double value) {
        return value * LINEAR_SCALAR_FACTOR;
    }

    private static double sineWaveScaling(double value) {
        if (value >= 0) {
            return (2 * Math.sin(value)) * (value + (getStartPosDelta() * NOISE_SCALAR_CONSTANT));
        }
        return -(2 * Math.sin(value)) * (value - (getStartPosDelta() * NOISE_SCALAR_CONSTANT));
    }

    private static double piecewiseStaticScaling(double value, double vanillaWrapped) {
        double progress = getApproxFarlandsProgress(value);
        if (progress >= 4000000) {
            return vanillaWrapped;
        }

        double returnable = Math.abs(value);
        if (progress >= 0) {
            returnable += Integer.MAX_VALUE;
        }
        if (progress >= 500000 && progress < 1000000) {
            returnable += 1.5 * (progress / getFarlandsLocation()) * Math.pow(10, 51);
        }
        if (progress >= 1000000) {
            returnable += exponentialScalingStatic((getFarlandsLocation() + 2243051.5) * NOISE_SCALAR_CONSTANT);
        }
        if (progress >= 1500000) {
            returnable += Double.MAX_VALUE;
        }
        return returnable;
    }

    private static double piecewiseProportionalScaling(double value, double vanillaWrapped) {
        double breakUnscaled = getFarlandsLocation() * 0.5;
        double breakFartherlands = getFarlandsLocation();
        double breakMoat = getFarlandsLocation() * 1.5;
        double breakDistantlands = getFarlandsLocation() * 4;

        double progress = getApproxFarlandsProgress(value);
        if (progress >= breakDistantlands) {
            return vanillaWrapped;
        }

        double returnable = Math.abs(value);
        if (progress >= 0) {
            returnable += Integer.MAX_VALUE;
        }
        if (progress >= breakUnscaled && progress < breakFartherlands) {
            returnable += 1.5 * (progress / getFarlandsLocation()) * Math.pow(10, 51);
        }
        if (progress >= breakFartherlands) {
            returnable += exponentialScalingStatic((getFarlandsLocation() + 2243051.5) * NOISE_SCALAR_CONSTANT);
        }
        if (progress >= breakMoat) {
            returnable += Double.MAX_VALUE;
        }
        return returnable;
    }
}
