package com.xFra.WaterMechanics.Junk;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockFluidRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import org.lwjgl.opengl.GL11;

public class NewBlockFluidRenderer extends BlockFluidRenderer {
	public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 15);
   private final  BlockColors blockColors;
   private final TextureAtlasSprite[] atlasSpritesLava = new TextureAtlasSprite[2];
   private final TextureAtlasSprite[] atlasSpritesWater = new TextureAtlasSprite[2];
   private TextureAtlasSprite atlasSpriteWaterOverlay;

   public NewBlockFluidRenderer(BlockColors blockColors) {
	  super(blockColors);
	  TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
	  this.atlasSpritesWater[0] = texturemap.getAtlasSprite("minecraft:blocks/water_still");
	  this.atlasSpritesWater[1] = texturemap.getAtlasSprite("minecraft:blocks/water_flow");
	  this.blockColors = blockColors;
   }
   
   private static boolean isAdjacentFluidSameAs(World worldIn, BlockPos pos, EnumFacing side) {
	      BlockPos blockpos = pos.offset(side);
	      Material material = worldIn.getBlockState(blockpos).getMaterial();
	      return material == Material.WATER;
	   }
   
//   private static boolean func_209556_a(World reader, BlockPos pos, EnumFacing face, float heightIn) {
//	      BlockPos blockpos = pos.offset(face);
//	      IBlockState iblockstate = reader.getBlockState(blockpos);
//	      if (iblockstate.isTopSolid()) {
//	         VoxelShape voxelshape = VoxelShapes.create(0.0D, 0.0D, 0.0D, 1.0D, (double)heightIn, 1.0D);
//	         VoxelShape voxelshape1 = iblockstate.getRenderShape(reader, blockpos);
//	         return VoxelShapes.isCubeSideCovered(voxelshape, voxelshape1, face);
//	      } else {
//	         return false;
//	      }
//	   }
   
   protected int getDepth(IBlockState p_189542_1_)
   {
       return p_189542_1_.getMaterial() == Material.WATER ? (Integer) 15 : -1;
	   //return p_189542_1_.getMaterial() == Material.WATER ? (Integer) p_189542_1_.getValue(LEVEL) : -1; TODO RIMETTERE QUESTO COME DEFAULT, CON LEVEL
   }

   protected int getRenderedDepth(IBlockState p_189545_1_)
   {
       int i = this.getDepth(p_189545_1_);
       return i >= 8 ? 0 : i;
   }
   
   protected Vec3d getFlow(IBlockAccess worldIn, BlockPos pos, IBlockState state)
   {
       double d0 = 0.0D;
       double d1 = 0.0D;
       double d2 = 0.0D;
       int i = this.getRenderedDepth(state);
       BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

       for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
       {
           blockpos$pooledmutableblockpos.setPos(pos).move(enumfacing);
           int j = this.getRenderedDepth(worldIn.getBlockState(blockpos$pooledmutableblockpos));

           if (j < 0)
           {
               if (!worldIn.getBlockState(blockpos$pooledmutableblockpos).getMaterial().blocksMovement())
               {
                   j = this.getRenderedDepth(worldIn.getBlockState(blockpos$pooledmutableblockpos.down()));

                   if (j >= 0)
                   {
                       int k = j - (i - 8);
                       d0 += (double)(enumfacing.getFrontOffsetX() * k);
                       d1 += (double)(enumfacing.getFrontOffsetY() * k);
                       d2 += (double)(enumfacing.getFrontOffsetZ() * k);
                   }
               }
           }
           else if (j >= 0)
           {
               int l = j - i;
               d0 += (double)(enumfacing.getFrontOffsetX() * l);
               d1 += (double)(enumfacing.getFrontOffsetY() * l);
               d2 += (double)(enumfacing.getFrontOffsetZ() * l);
           }
       }

       Vec3d vec3d = new Vec3d(d0, d1, d2);

	   if (true)
       //if ((Integer) state.getValue(LEVEL) >= 8) TODO RIMETTERE LEVEL
       {
           for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL)
           {
               blockpos$pooledmutableblockpos.setPos(pos).move(enumfacing1);

               if (causesDownwardCurrent(worldIn, blockpos$pooledmutableblockpos, enumfacing1) || causesDownwardCurrent(worldIn, blockpos$pooledmutableblockpos.up(), enumfacing1))
               {
                   vec3d = vec3d.normalize().addVector(0.0D, -6.0D, 0.0D);
                   break;
               }
           }
       }

       blockpos$pooledmutableblockpos.release();
       return vec3d.normalize();
   }
   
   private boolean causesDownwardCurrent(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
   {
       IBlockState iblockstate = worldIn.getBlockState(pos);
       Block block = iblockstate.getBlock();
       Material material = iblockstate.getMaterial();

       if (material == Material.WATER)
       {
           return false;
       }
       else if (side == EnumFacing.UP)
       {
           return true;
       }
       else if (material == Material.ICE)
       {
           return false;
       }
       else
       {
           boolean flag = isExceptBlockForAttachWithPiston(block) || block instanceof BlockStairs;
           return !flag && iblockstate.getBlockFaceShape(worldIn, pos, side) == BlockFaceShape.SOLID;
       }
   }
   
   protected static boolean isExceptionBlockForAttaching(Block attachBlock)
   {
       return attachBlock instanceof BlockShulkerBox || attachBlock instanceof BlockLeaves || attachBlock instanceof BlockTrapDoor || attachBlock == Blocks.BEACON || attachBlock == Blocks.CAULDRON || attachBlock == Blocks.GLASS || attachBlock == Blocks.GLOWSTONE || attachBlock == Blocks.ICE || attachBlock == Blocks.SEA_LANTERN || attachBlock == Blocks.STAINED_GLASS;
   }

   protected static boolean isExceptBlockForAttachWithPiston(Block attachBlock)
   {
       return isExceptionBlockForAttaching(attachBlock) || attachBlock == Blocks.PISTON || attachBlock == Blocks.STICKY_PISTON || attachBlock == Blocks.PISTON_HEAD;
   }
   
   @Override
   public boolean renderFluid(IBlockAccess blockAccess, IBlockState blockStateIn, BlockPos blockPosIn, BufferBuilder buffer) {
       //BlockLiquid blockliquid = (BlockLiquid)blockStateIn.getBlock();
//       BlockLiquid block = (BlockDynamicLiquid) blockStateIn.getBlock();
//       block.flo
       BlockPos pos = blockPosIn;
       IBlockState state = blockStateIn;
       int i = BiomeColorHelper.getWaterColorAtPos(blockAccess, pos);
       World worldIn = Minecraft.getMinecraft().world;
       float f = (float)(i >> 16 & 255) / 255.0F;
       float f1 = (float)(i >> 8 & 255) / 255.0F;
       float f2 = (float)(i & 255) / 255.0F;
       boolean flag1 = blockStateIn.shouldSideBeRendered(blockAccess, blockPosIn, EnumFacing.UP);
       boolean flag2 = blockStateIn.shouldSideBeRendered(blockAccess, blockPosIn, EnumFacing.DOWN);
       boolean[] aboolean = new boolean[] {blockStateIn.shouldSideBeRendered(blockAccess, blockPosIn, EnumFacing.NORTH), blockStateIn.shouldSideBeRendered(blockAccess, blockPosIn, EnumFacing.SOUTH), blockStateIn.shouldSideBeRendered(blockAccess, blockPosIn, EnumFacing.WEST), blockStateIn.shouldSideBeRendered(blockAccess, blockPosIn, EnumFacing.EAST)};

	   
	      TextureAtlasSprite[] atextureatlassprite = this.atlasSpritesWater;
	      boolean flag3 = !isAdjacentFluidSameAs(worldIn, pos, EnumFacing.NORTH);
	      boolean flag4 = !isAdjacentFluidSameAs(worldIn, pos, EnumFacing.SOUTH);
	      boolean flag5 = !isAdjacentFluidSameAs(worldIn, pos, EnumFacing.WEST);
	      boolean flag6 = !isAdjacentFluidSameAs(worldIn, pos, EnumFacing.EAST);
	      if (!flag1 && !flag2 && !flag6 && !flag5 && !flag3 && !flag4) {
	         return false;
	      } else {
	         boolean flag7 = false;
	         float f7 = this.getFluidHeight(worldIn, pos);
	         float f8 = this.getFluidHeight(worldIn, pos.south());
	         float f9 = this.getFluidHeight(worldIn, pos.east().south());
	         float f10 = this.getFluidHeight(worldIn, pos.east());
	         double d0 = (double)pos.getX();
	         double d1 = (double)pos.getY();
	         double d2 = (double)pos.getZ();
			  if (flag1) {
	            flag7 = true;
	            f7 -= 0.001F;
	            f8 -= 0.001F;
	            f9 -= 0.001F;
	            f10 -= 0.001F;
	            Vec3d vec3d = getFlow(worldIn, pos, state);
	            float f12;
	            float f13;
	            float f14;
	            float f15;
	            float f16;
	            float f17;
	            float f18;
	            float f19;
	            if (vec3d.x == 0.0D && vec3d.z == 0.0D) {
	               TextureAtlasSprite textureatlassprite1 = atextureatlassprite[0];
	               f12 = textureatlassprite1.getInterpolatedU(0.0D);
	               f16 = textureatlassprite1.getInterpolatedV(0.0D);
	               f13 = f12;
	               f17 = textureatlassprite1.getInterpolatedV(16.0D);
	               f14 = textureatlassprite1.getInterpolatedU(16.0D);
	               f18 = f17;
	               f15 = f14;
	               f19 = f16;
	            } else {
	               TextureAtlasSprite textureatlassprite = atextureatlassprite[1];
	               float f20 = (float)MathHelper.atan2(vec3d.z, vec3d.x) - ((float)Math.PI / 2F);
	               float f21 = MathHelper.sin(f20) * 0.25F;
	               float f22 = MathHelper.cos(f20) * 0.25F;
	               float f23 = 8.0F;
	               f12 = textureatlassprite.getInterpolatedU((double)(8.0F + (-f22 - f21) * 16.0F));
	               f16 = textureatlassprite.getInterpolatedV((double)(8.0F + (-f22 + f21) * 16.0F));
	               f13 = textureatlassprite.getInterpolatedU((double)(8.0F + (-f22 + f21) * 16.0F));
	               f17 = textureatlassprite.getInterpolatedV((double)(8.0F + (f22 + f21) * 16.0F));
	               f14 = textureatlassprite.getInterpolatedU((double)(8.0F + (f22 + f21) * 16.0F));
	               f18 = textureatlassprite.getInterpolatedV((double)(8.0F + (f22 - f21) * 16.0F));
	               f15 = textureatlassprite.getInterpolatedU((double)(8.0F + (f22 - f21) * 16.0F));
	               f19 = textureatlassprite.getInterpolatedV((double)(8.0F + (-f22 - f21) * 16.0F));
	            }

	            int i2 = this.getCombinedLightUpMax(worldIn, pos);
	            int j2 = i2 >> 16 & '\uffff';
	            int k2 = i2 & '\uffff';
	            float f42 = 1.0F * f;
	            float f43 = 1.0F * f1;
	            float f24 = 1.0F * f2;
	            buffer.pos(d0 + 0.0D, d1 + (double)f7, d2 + 0.0D).color(f42, f43, f24, 1.0F).tex((double)f12, (double)f16).lightmap(j2, k2).endVertex();
	            buffer.pos(d0 + 0.0D, d1 + (double)f8, d2 + 1.0D).color(f42, f43, f24, 1.0F).tex((double)f13, (double)f17).lightmap(j2, k2).endVertex();
	            buffer.pos(d0 + 1.0D, d1 + (double)f9, d2 + 1.0D).color(f42, f43, f24, 1.0F).tex((double)f14, (double)f18).lightmap(j2, k2).endVertex();
	            buffer.pos(d0 + 1.0D, d1 + (double)f10, d2 + 0.0D).color(f42, f43, f24, 1.0F).tex((double)f15, (double)f19).lightmap(j2, k2).endVertex();
	            if (shouldRenderSides(worldIn, pos.up())) {
	               buffer.pos(d0 + 0.0D, d1 + (double)f7, d2 + 0.0D).color(f42, f43, f24, 1.0F).tex((double)f12, (double)f16).lightmap(j2, k2).endVertex();
	               buffer.pos(d0 + 1.0D, d1 + (double)f10, d2 + 0.0D).color(f42, f43, f24, 1.0F).tex((double)f15, (double)f19).lightmap(j2, k2).endVertex();
	               buffer.pos(d0 + 1.0D, d1 + (double)f9, d2 + 1.0D).color(f42, f43, f24, 1.0F).tex((double)f14, (double)f18).lightmap(j2, k2).endVertex();
	               buffer.pos(d0 + 0.0D, d1 + (double)f8, d2 + 1.0D).color(f42, f43, f24, 1.0F).tex((double)f13, (double)f17).lightmap(j2, k2).endVertex();
	            }
	         }

	         if (flag2) {
	            float f33 = atextureatlassprite[0].getMinU();
	            float f34 = atextureatlassprite[0].getMaxU();
	            float f36 = atextureatlassprite[0].getMinV();
	            float f38 = atextureatlassprite[0].getMaxV();
	            int j1 = this.getCombinedLightUpMax(worldIn, pos.down());
	            int k1 = j1 >> 16 & '\uffff';
	            int l1 = j1 & '\uffff';
	            float f39 = 0.5F * f;
	            float f40 = 0.5F * f1;
	            float f41 = 0.5F * f2;
	            buffer.pos(d0, d1, d2 + 1.0D).color(f39, f40, f41, 1.0F).tex((double)f33, (double)f38).lightmap(k1, l1).endVertex();
	            buffer.pos(d0, d1, d2).color(f39, f40, f41, 1.0F).tex((double)f33, (double)f36).lightmap(k1, l1).endVertex();
	            buffer.pos(d0 + 1.0D, d1, d2).color(f39, f40, f41, 1.0F).tex((double)f34, (double)f36).lightmap(k1, l1).endVertex();
	            buffer.pos(d0 + 1.0D, d1, d2 + 1.0D).color(f39, f40, f41, 1.0F).tex((double)f34, (double)f38).lightmap(k1, l1).endVertex();
	            flag7 = true;
	         }

	         for(int i1 = 0; i1 < 4; ++i1) {
	            float f35;
	            float f37;
	            double d3;
	            double d4;
	            double d5;
	            double d6;
	            EnumFacing enumfacing;
	            boolean flag8;
	            if (i1 == 0) {
	               f35 = f7;
	               f37 = f10;
	               d3 = d0;
	               d5 = d0 + 1.0D;
	               d4 = d2 + (double)0.001F;
	               d6 = d2 + (double)0.001F;
	               enumfacing = EnumFacing.NORTH;
	               flag8 = flag3;
	            } else if (i1 == 1) {
	               f35 = f9;
	               f37 = f8;
	               d3 = d0 + 1.0D;
	               d5 = d0;
	               d4 = d2 + 1.0D - (double)0.001F;
	               d6 = d2 + 1.0D - (double)0.001F;
	               enumfacing = EnumFacing.SOUTH;
	               flag8 = flag4;
	            } else if (i1 == 2) {
	               f35 = f8;
	               f37 = f7;
	               d3 = d0 + (double)0.001F;
	               d5 = d0 + (double)0.001F;
	               d4 = d2 + 1.0D;
	               d6 = d2;
	               enumfacing = EnumFacing.WEST;
	               flag8 = flag5;
	            } else {
	               f35 = f10;
	               f37 = f9;
	               d3 = d0 + 1.0D - (double)0.001F;
	               d5 = d0 + 1.0D - (double)0.001F;
	               d4 = d2;
	               d6 = d2 + 1.0D;
	               enumfacing = EnumFacing.EAST;
	               flag8 = flag6;
	            }

	            if (flag8) {
	               flag7 = true;
	               BlockPos blockpos = pos.offset(enumfacing);
	               TextureAtlasSprite textureatlassprite2 = atextureatlassprite[1];
	               IBlockState blockstate = worldIn.getBlockState(blockpos);
	               if (blockstate.getBlockFaceShape(worldIn, blockpos, enumfacing) == net.minecraft.block.state.BlockFaceShape.SOLID) {
	                  textureatlassprite2 = this.atlasSpriteWaterOverlay;
	               }

	               float f44 = textureatlassprite2.getInterpolatedU(0.0D);
	               float f25 = textureatlassprite2.getInterpolatedU(8.0D);
	               float f26 = textureatlassprite2.getInterpolatedV((double)((1.0F - f35) * 16.0F * 0.5F));
	               float f27 = textureatlassprite2.getInterpolatedV((double)((1.0F - f37) * 16.0F * 0.5F));
	               float f28 = textureatlassprite2.getInterpolatedV(8.0D);
	               int j = this.getCombinedLightUpMax(worldIn, blockpos);
	               int k = j >> 16 & '\uffff';
	               int l = j & '\uffff';
	               float f29 = i1 < 2 ? 0.8F : 0.6F;
	               float f30 = 1.0F * f29 * f;
	               float f31 = 1.0F * f29 * f1;
	               float f32 = 1.0F * f29 * f2;
	               buffer.pos(d3, d1 + (double)f35, d4).color(f30, f31, f32, 1.0F).tex((double)f44, (double)f26).lightmap(k, l).endVertex();
	               buffer.pos(d5, d1 + (double)f37, d6).color(f30, f31, f32, 1.0F).tex((double)f25, (double)f27).lightmap(k, l).endVertex();
	               buffer.pos(d5, d1 + 0.0D, d6).color(f30, f31, f32, 1.0F).tex((double)f25, (double)f28).lightmap(k, l).endVertex();
	               buffer.pos(d3, d1 + 0.0D, d4).color(f30, f31, f32, 1.0F).tex((double)f44, (double)f28).lightmap(k, l).endVertex();
	               //if (textureatlassprite2 != this.atlasSpriteWaterOverlay) {
	                  //buffer.pos(d3, d1 + 0.0D, d4).color(f30, f31, f32, 1.0F).tex((double)f44, (double)f28).lightmap(k, l).endVertex();
	                  //buffer.pos(d5, d1 + 0.0D, d6).color(f30, f31, f32, 1.0F).tex((double)f25, (double)f28).lightmap(k, l).endVertex();
	                  //buffer.pos(d5, d1 + (double)f37, d6).color(f30, f31, f32, 1.0F).tex((double)f25, (double)f27).lightmap(k, l).endVertex();
	                  //buffer.pos(d3, d1 + (double)f35, d4).color(f30, f31, f32, 1.0F).tex((double)f44, (double)f26).lightmap(k, l).endVertex();
	               //}
	            }
	         }
	         return flag7;
	      }
   }
   
   public boolean shouldRenderSides(World worldIn, BlockPos pos) {
	      for(int i = -1; i <= 1; ++i) {
	         for(int j = -1; j <= 1; ++j) {
	            BlockPos blockpos = pos.add(i, 0, j);
	            IBlockState ifluidstate = worldIn.getBlockState(blockpos);
	            if (ifluidstate.getMaterial() != Material.WATER && !worldIn.getBlockState(blockpos).isOpaqueCube()) {
	               return true;
	            }
	         }
	      }

	      return false;
	   }
   
   private float getFluidHeight(World reader, BlockPos pos) {
	      int i = 0;
	      float f = 0.0F;

	      for(int j = 0; j < 4; ++j) {
	         BlockPos blockpos = pos.add(-(j & 1), 0, -(j >> 1 & 1));
	         if (reader.getBlockState(blockpos.up()).getMaterial() == Material.WATER) {
	            return 1.0F;
	         }

	         IBlockState ifluidstate = reader.getBlockState(blockpos);
	         if (ifluidstate.getMaterial() == Material.WATER) {
	            f += 0.8F;
	            ++i;
	         } else if (!reader.getBlockState(blockpos).getMaterial().isSolid()) {
	            ++i;
	         }
	      }

	      return f / (float)i;
	   }
   
   private int getCombinedLightUpMax(World reader, BlockPos pos) {
	      int i = reader.getCombinedLight(pos, 0);
	      int j = reader.getCombinedLight(pos.up(), 0);
	      int k = i & 255;
	      int l = j & 255;
	      int i1 = i >> 16 & 255;
	      int j1 = j >> 16 & 255;
	      return (k > l ? k : l) | (i1 > j1 ? i1 : j1) << 16;
	   }
}