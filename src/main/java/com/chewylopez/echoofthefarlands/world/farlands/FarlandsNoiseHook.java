package com.chewylopez.echoofthefarlands.world.farlands;

import net.minecraft.world.gen.NoiseGeneratorImproved;

public final class FarlandsNoiseHook {

    private FarlandsNoiseHook() {}

    public static void populateNoiseArray(
            NoiseGeneratorImproved generator,
            double[] noiseArray,
            double xWrapped, double y, double zWrapped,
            int xSize, int ySize, int zSize,
            double xScaleOctave, double yScaleOctave, double zScaleOctave, double octaveFactor,
            int xOffsetBase, int zOffsetBase) {

        double trueX = (double) xOffsetBase * xScaleOctave;
        double trueZ = (double) zOffsetBase * zScaleOctave;

        double x = FarlandsNoise.transform(trueX, xWrapped);
        double z = FarlandsNoise.transform(trueZ, zWrapped);

        generator.populateNoiseArray(noiseArray, x, y, z,
                xSize, ySize, zSize, xScaleOctave, yScaleOctave, zScaleOctave, octaveFactor);
    }
}
