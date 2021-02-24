package com.xFra.WaterMechanics.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockMagma;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockNewMagma extends BlockMagma{

	   public BlockNewMagma()  {
		   super();
		   this.setHardness(0.5F);
		   this.setLightLevel(3);
		   this.setSoundType(SoundType.STONE);
		   this.setRegistryName("minecraft", "magma");
		   this.setUnlocalizedName("magma");
	   }
	   
       @Override
	   public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		   BlockDownwardsBubbleColumn.placeBubbleColumn(worldIn, pos.up());
	   }

       @Override
	   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
    	   BlockDownwardsBubbleColumn.placeBubbleColumn(worldIn, pos.up());
	   }
}