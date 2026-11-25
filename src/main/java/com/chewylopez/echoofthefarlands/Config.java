package com.chewylopez.echoofthefarlands;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;

public final class Config {

    private static final String CATEGORY = "general";

    private static final int DEFAULT_LOCATION = 1000000;
    private static final int DEFAULT_GEN_TYPE = 0;
    public static final int MAX_GEN_TYPE = 6;

    private static Configuration config;

    private static int farlandsLocationCustom = DEFAULT_LOCATION;
    private static int farlandsLocation = DEFAULT_LOCATION;
    private static int farlandsGenType = DEFAULT_GEN_TYPE;

    private Config() {}

    public static void load(File configFile) {
        config = new Configuration(configFile);
        config.load();
        read();
        if (config.hasChanged()) {
            config.save();
        }
    }

    private static void read() {
        Property custom = config.get(CATEGORY, "farlandsCustomLocation", DEFAULT_LOCATION,
                "this is where the custom value you can change from the in-game menu is stored, this is not referenced by the server",
                1, Integer.MAX_VALUE);
        Property location = config.get(CATEGORY, "farlandsLocation", DEFAULT_LOCATION,
                "FARLANDS LOCATION\nonly change this manually if you are setting up a server; existing worlds keep their own copy",
                1, Integer.MAX_VALUE);
        Property genType = config.get(CATEGORY, "farlandsGenType", DEFAULT_GEN_TYPE,
                "FARLANDS GEN TYPE\n0. piecewise\n1. proportional piecewise\n2. Unscaled\n3. exponential\n4. proportional exponential\n5. Linear\n6. Sine",
                0, MAX_GEN_TYPE);

        farlandsLocationCustom = custom.getInt(DEFAULT_LOCATION);
        farlandsLocation = location.getInt(DEFAULT_LOCATION);
        farlandsGenType = genType.getInt(DEFAULT_GEN_TYPE);
    }

    public static int getFarlandsLocationCustom() {
        return farlandsLocationCustom;
    }

    public static int getFarlandsLocation() {
        return farlandsLocation;
    }

    public static int getFarlandsGenType() {
        return farlandsGenType;
    }

    public static void setFarlandsLocationCustom(int value) {
        farlandsLocationCustom = value;
        if (config != null) {
            config.get(CATEGORY, "farlandsCustomLocation", DEFAULT_LOCATION).set(value);
        }
    }

    public static void setFarlandsLocation(int value) {
        farlandsLocation = value;
        if (config != null) {
            config.get(CATEGORY, "farlandsLocation", DEFAULT_LOCATION).set(value);
        }
    }

    public static void setFarlandsGenType(int value) {
        farlandsGenType = value;
        if (config != null) {
            config.get(CATEGORY, "farlandsGenType", DEFAULT_GEN_TYPE).set(value);
        }
    }

    public static void applyWorldValues(int location, int genType) {
        farlandsLocation = location;
        farlandsGenType = genType;
    }

    public static void save() {
        if (config != null && config.hasChanged()) {
            config.save();
        }
    }
}
