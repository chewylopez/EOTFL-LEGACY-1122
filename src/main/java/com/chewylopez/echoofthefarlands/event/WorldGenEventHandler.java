package com.chewylopez.echoofthefarlands.event;

import com.chewylopez.echoofthefarlands.Config;
import com.chewylopez.echoofthefarlands.EchoOfTheFarlands;
import com.chewylopez.echoofthefarlands.datagen.WorldFarlandsSettings;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = EchoOfTheFarlands.MODID)
public class WorldGenEventHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        World world = event.getWorld();
        if (world == null || world.isRemote) {
            return;
        }

        WorldFarlandsSettings settings = WorldFarlandsSettings.get(world);

        if (settings.isNewWorld()) {
            settings.setFarlandsPosition(Config.getFarlandsLocation());
            settings.setFarlandsGenType(Config.getFarlandsGenType());

            LOGGER.info("new world (dim {}) seeded from global config -- farlands position: {}, gen type: {}",
                    world.provider.getDimension(),
                    settings.getFarlandsPosition(),
                    settings.getFarlandsGenType());
        }

        Config.applyWorldValues(settings.getFarlandsPosition(), settings.getFarlandsGenType());
        settings.markDirty();
    }
}
