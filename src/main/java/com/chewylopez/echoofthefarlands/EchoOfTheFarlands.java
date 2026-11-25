package com.chewylopez.echoofthefarlands;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = EchoOfTheFarlands.MODID, name = EchoOfTheFarlands.NAME, version = EchoOfTheFarlands.VERSION)
public class EchoOfTheFarlands
{
    public static final String MODID = "echoofthefarlands";
    public static final String NAME = "Echo of the Farlands";
    public static final String VERSION = "1.0.0";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        logger.info("{} preInit", NAME);

        // Must happen in preInit: the coremod's noise hook reads these values, and world
        // generation can begin as soon as a world loads.
        Config.load(event.getSuggestedConfigurationFile());
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        logger.info("{} init", NAME);
    }
}
