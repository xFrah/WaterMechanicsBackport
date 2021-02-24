package com.xFra.WaterMechanics.Blocks;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockDownwardsBubbleColumn extends BlockLiquid {

   public static IBlockState state;
	
   public BlockDownwardsBubbleColumn() {
      super(Material.WATER);
      this.setRegistryName("downwards_bubble_column");
      this.setUnlocalizedName("Downwards Bubble Column");
      this.setDefaultState(this.blockState.getBaseState());
      state = this.blockState.getBaseState();
   }
   
   public void onEnterBubbleColumn(Entity entityIn) {
	  entityIn.motionY = Math.max(-0.3D, entityIn.motionY - 0.03D);
	  entityIn.fallDistance = 0.0F;
   }
   
   public void onEnterBubbleColumnWithAirAbove(Entity entityIn) {
	   entityIn.motionY = Math.max(-0.9D, entityIn.motionY - 0.03D);
   }
   
   @Override
   public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
      IBlockState iblockstate = worldIn.getBlockState(pos.up());
      if (iblockstate.getBlock() == Blocks.AIR) {
         onEnterBubbleColumnWithAirAbove(entityIn);
      } else {
         onEnterBubbleColumn(entityIn);
      }
   }
   
   public static void placeBubbleColumn(World world, BlockPos pos) {
      if (canHoldBubbleColumn(world, pos)) {
          world.setBlockState(pos, state);
      }

   }

   public static boolean canHoldBubbleColumn(World world, BlockPos pos) {
      return (world.getBlockState(pos).getMaterial() == Material.WATER && world.getBlockState(pos).getBlock() instanceof BlockLiquid && ((BlockLiquid) world.getBlockState(pos).getBlock()).getMetaFromState(world.getBlockState(pos)) == 0) && (world.getBlockState(pos.down()).getBlock() == Blocks.MAGMA || world.getBlockState(pos.down()) == state);
   }

   @Override
   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
      double d0 = (double)pos.getX();
      double d1 = (double)pos.getY();
      double d2 = (double)pos.getZ();
      worldIn.spawnParticle(EnumParticleTypes.WATER_BUBBLE, d0 + 0.5D, d1, d2 + 0.5D, 0.0D, -1D, 0.0D);
      worldIn.spawnParticle(EnumParticleTypes.WATER_BUBBLE, d0 + (double)rand.nextFloat(), d1 + (double)rand.nextFloat(), d2 + (double)rand.nextFloat(), 0.0D, -1D, 0.0D);
      worldIn.spawnParticle(EnumParticleTypes.WATER_BUBBLE, d0 + (double)rand.nextFloat(), d1 + (double)rand.nextFloat(), d2 + (double)rand.nextFloat(), 0.0D, -1D, 0.0D);
   }

   @Override
   public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
      if (!isValidPosition(worldIn, pos)) {
         worldIn.setBlockState(pos, Blocks.WATER.getDefaultState());
      }
      BlockDownwardsBubbleColumn.placeBubbleColumn(worldIn, pos.up());
   }

   public boolean isValidPosition(World worldIn, BlockPos pos) {
      Block block = worldIn.getBlockState(pos.down()).getBlock();
      return block == this || block == Blocks.MAGMA;
   }
   
   @Override
   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
   {
       BlockDownwardsBubbleColumn.placeBubbleColumn(worldIn, pos.up());
   }

}