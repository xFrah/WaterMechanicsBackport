package com.xFra.WaterMechanics.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockNewSoulSand extends BlockSoulSand{

	   public BlockNewSoulSand()  {
		   super();
		   this.setRegistryName("minecraft", "soul_sand");
		   this.setUnlocalizedName("hellsand");
		   this.setHardness(0.5F);
		   this.setSoundType(SoundType.SAND);
		   
	   }
	   
       @Override
	   public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		   BlockUpwardsBubbleColumn.placeBubbleColumn(worldIn, pos.up());
	   }

       @Override
	   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
    	   BlockUpwardsBubbleColumn.placeBubbleColumn(worldIn, pos.up());
	   }
}