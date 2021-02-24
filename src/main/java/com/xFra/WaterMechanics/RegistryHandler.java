package com.xFra.WaterMechanics;

import com.xFra.WaterMechanics.Blocks.BlockDownwardsBubbleColumn;
import com.xFra.WaterMechanics.Blocks.BlockNewMagma;
import com.xFra.WaterMechanics.Blocks.BlockNewSoulSand;
import com.xFra.WaterMechanics.Blocks.BlockUpwardsBubbleColumn;
import com.xFra.WaterMechanics.Junk.TankBlockEntity;
import com.xFra.WaterMechanics.Junk.WLBlock;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@EventBusSubscriber
public class RegistryHandler 
{
	public static WLBlock waterlogged = new WLBlock();

//	@EventHandler
//	public void Init(FMLInitializationEvent event){ meglio mettere preInit
//		Blocks.bedrock.setHardness(100.0F);
//		Blocks.bedrock.setHarvestLevel("pickaxe", 4);
//		Blocks.end_portal_frame.setHardness(1.0F);
//		Blocks.end_portal_frame.setHarvestLevel("pickaxe", 4);
//	}

	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event)
	{
		ItemBlock itblock = new ItemBlock(waterlogged);
		itblock.setRegistryName(waterlogged.getRegistryName());
		event.getRegistry().register(itblock);
	}

	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().register(new BlockNewMagma());
		event.getRegistry().register(new BlockDownwardsBubbleColumn());
		event.getRegistry().register(new BlockNewSoulSand());
		GameRegistry.registerTileEntity(TankBlockEntity.class, new ResourceLocation("113_water_mechanics", "TankBlockEntity"));
		event.getRegistry().register(waterlogged);
        ObfuscationReflectionHelper.setPrivateValue(Block.class, Blocks.SOUL_SAND, new BlockNewSoulSand().getDefaultState(), "field_176228_M", "defaultBlockState", "c");
        //TODO mettere anche il magma block qui che magari fa lo stesso lag nel nether
		event.getRegistry().register(new BlockUpwardsBubbleColumn());
	}

}