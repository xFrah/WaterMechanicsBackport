package com.xFra.WaterMechanics;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.lang.reflect.InvocationTargetException;

import org.apache.logging.log4j.Logger;

@Mod(modid = WaterMechanicsCore.MODID, name = WaterMechanicsCore.NAME, version = WaterMechanicsCore.VERSION)
public class WaterMechanicsCore
{
	
    public static final String MODID = "113_water_mechanics";
    public static final String NAME = "Bubble Columns Backport";
    public static final String VERSION = "1.0.6";
    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        logger.info("1.13 Water Mechanics Backport is WOOOORKIIING!");
    }
}
