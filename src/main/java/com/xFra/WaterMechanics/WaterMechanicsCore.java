package com.xFra.WaterMechanics;

import com.xFra.WaterMechanics.Network.CommonProxy;
import com.xFra.WaterMechanics.Network.MessageRemoveWaterloggedBlock;
import com.xFra.WaterMechanics.Network.MessageSendWaterloggedBlock;
import com.xFra.WaterMechanics.Network.ServerEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.reflect.InvocationTargetException;

import org.apache.logging.log4j.Logger;

@Mod(modid = WaterMechanicsCore.MODID, name = WaterMechanicsCore.NAME, version = WaterMechanicsCore.VERSION)
public class WaterMechanicsCore
{
	
    public static final String MODID = "113_water_mechanics";
    public static final String NAME = "1.13 Water Mechanics";
    public static final String VERSION = "1.0.3";
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(WaterMechanicsCore.MODID);
    private static Logger logger;

    @SidedProxy(clientSide="com.xFra.WaterMechanics.Network.StartupClientOnly", serverSide="com.xFra.WaterMechanics.Network.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        proxy.preInit();
        MinecraftForge.EVENT_BUS.register(ServerEvents.class);
        INSTANCE.registerMessage(MessageSendWaterloggedBlock.HandlerServer.class, MessageSendWaterloggedBlock.class, 1, Side.SERVER);
        INSTANCE.registerMessage(MessageSendWaterloggedBlock.HandlerClient.class, MessageSendWaterloggedBlock.class, 2, Side.CLIENT);
        INSTANCE.registerMessage(MessageRemoveWaterloggedBlock.HandlerClient.class, MessageRemoveWaterloggedBlock.class, 3, Side.CLIENT);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        logger.info("1.13 Water Mechanics Backport is WOOOORKIIING!");
    }
}
