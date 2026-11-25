package com.chewylopez.echoofthefarlands.datagen;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class WorldFarlandsSettings extends WorldSavedData {

    public static final String DATA_ID = "farlands";

    private int farlandsPosition;
    private int farlandsGenType;

    private boolean isNew = false;

    public WorldFarlandsSettings() {
        super(DATA_ID);
    }

    public WorldFarlandsSettings(String name) {
        super(name);
    }

    public int getFarlandsPosition() {
        return farlandsPosition;
    }

    public int getFarlandsGenType() {
        return farlandsGenType;
    }

    public void setFarlandsPosition(int val) {
        farlandsPosition = val;
        this.markDirty();
    }

    public void setFarlandsGenType(int val) {
        farlandsGenType = val;
        this.markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        if (tag.hasKey("FarlandsPosition")) farlandsPosition = tag.getInteger("FarlandsPosition");
        if (tag.hasKey("FarlandsGenType")) farlandsGenType = tag.getInteger("FarlandsGenType");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setInteger("FarlandsPosition", farlandsPosition);
        tag.setInteger("FarlandsGenType", farlandsGenType);
        return tag;
    }

    public boolean isNewWorld() {
        return isNew;
    }

    public static WorldFarlandsSettings get(World world) {
        MapStorage storage = world.getPerWorldStorage();
        WorldFarlandsSettings data =
                (WorldFarlandsSettings) storage.getOrLoadData(WorldFarlandsSettings.class, DATA_ID);

        if (data == null) {
            data = new WorldFarlandsSettings();
            data.isNew = true;
            storage.setData(DATA_ID, data);
        }
        return data;
    }
}
