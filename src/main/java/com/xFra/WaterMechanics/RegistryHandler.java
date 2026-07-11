package com.xFra.WaterMechanics;

import com.xFra.WaterMechanics.Blocks.BlockDownwardsBubbleColumn;
import com.xFra.WaterMechanics.Blocks.BlockNewMagma;
import com.xFra.WaterMechanics.Blocks.BlockNewSoulSand;
import com.xFra.WaterMechanics.Blocks.BlockUpwardsBubbleColumn;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber
public class RegistryHandler 
{
	public static final BlockDownwardsBubbleColumn DOWNWARDS_BUBBLE_COLUMN = new BlockDownwardsBubbleColumn();
	public static final BlockUpwardsBubbleColumn UPWARDS_BUBBLE_COLUMN = new BlockUpwardsBubbleColumn();

	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().register(new BlockNewMagma());
		event.getRegistry().register(DOWNWARDS_BUBBLE_COLUMN);
		event.getRegistry().register(new BlockNewSoulSand());
		event.getRegistry().register(UPWARDS_BUBBLE_COLUMN);
	}

	@SubscribeEvent
	public static void onWorldTick(net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent event) {
		if (event.phase == net.minecraftforge.fml.common.gameevent.TickEvent.Phase.END) {
			for (net.minecraft.entity.Entity entity : event.world.loadedEntityList) {
				if (entity instanceof net.minecraft.entity.projectile.EntityThrowable || entity instanceof net.minecraft.entity.projectile.EntityArrow) {
					net.minecraft.util.math.BlockPos pos = new net.minecraft.util.math.BlockPos(entity);
					IBlockState state = event.world.getBlockState(pos);
					if (state.getBlock() == DOWNWARDS_BUBBLE_COLUMN) {
						DOWNWARDS_BUBBLE_COLUMN.onEntityCollidedWithBlock(event.world, pos, state, entity);
					} else if (state.getBlock() == UPWARDS_BUBBLE_COLUMN) {
						UPWARDS_BUBBLE_COLUMN.onEntityCollidedWithBlock(event.world, pos, state, entity);
					}
				}
			}
		}
	}

	@EventBusSubscriber(value = Side.CLIENT, modid = WaterMechanicsCore.MODID)
	public static class ClientRegistryHandler {
		@SubscribeEvent
		public static void onModelRegister(ModelRegistryEvent event) {
			StateMapperBase ignoreState = new StateMapperBase() {
				@Override
				protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
					return new ModelResourceLocation("minecraft:water", "level=0");
				}
			};
			ModelLoader.setCustomStateMapper(DOWNWARDS_BUBBLE_COLUMN, ignoreState);
			ModelLoader.setCustomStateMapper(UPWARDS_BUBBLE_COLUMN, ignoreState);
		}
	}
}