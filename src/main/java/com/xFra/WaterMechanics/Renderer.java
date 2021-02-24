package com.xFra.WaterMechanics;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Renderer
{
    private final BlockColors blockColors;
    private final TextureAtlasSprite[] atlasSpritesLava = new TextureAtlasSprite[2];
    private final TextureAtlasSprite[] atlasSpritesWater = new TextureAtlasSprite[2];
    private TextureAtlasSprite atlasSpriteWaterOverlay;

    public Renderer(BlockColors blockColorsIn)
    {
        this.blockColors = blockColorsIn;
        this.initAtlasSprites();
    }

    protected void initAtlasSprites()
    {
        TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
        this.atlasSpritesLava[0] = texturemap.getAtlasSprite("minecraft:blocks/lava_still");
        this.atlasSpritesLava[1] = texturemap.getAtlasSprite("minecraft:blocks/lava_flow");
        this.atlasSpritesWater[0] = texturemap.getAtlasSprite("minecraft:blocks/water_still");
        this.atlasSpritesWater[1] = texturemap.getAtlasSprite("minecraft:blocks/water_flow");
        this.atlasSpriteWaterOverlay = texturemap.getAtlasSprite("minecraft:blocks/water_overlay");
    }

    public boolean renderFluid(IBlockAccess blockAccess, IBlockState blockStateIn, BlockPos blockPosIn, BufferBuilder bufferBuilderIn, Fluid fluid)
    {
        //boolean flag = blockStateIn.getMaterial() == Material.LAVA;
        ResourceLocation still = fluid.getStill();
        TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(still.toString());
        //TextureAtlasSprite[] atextureatlassprite = flag ? this.atlasSpritesLava : this.atlasSpritesWater;
        int i = this.blockColors.colorMultiplier(blockStateIn, blockAccess, blockPosIn, 0);
        float f = (float)(i >> 16 & 255) / 255.0F;
        float f1 = (float)(i >> 8 & 255) / 255.0F;
        float f2 = (float)(i & 255) / 255.0F;
        boolean flag1 = blockStateIn.shouldSideBeRendered(blockAccess, blockPosIn, EnumFacing.UP);
        boolean flag2 = blockStateIn.shouldSideBeRendered(blockAccess, blockPosIn, EnumFacing.DOWN);
        boolean[] aboolean = new boolean[] {blockStateIn.shouldSideBeRendered(blockAccess, blockPosIn, EnumFacing.NORTH), blockStateIn.shouldSideBeRendered(blockAccess, blockPosIn, EnumFacing.SOUTH), blockStateIn.shouldSideBeRendered(blockAccess, blockPosIn, EnumFacing.WEST), blockStateIn.shouldSideBeRendered(blockAccess, blockPosIn, EnumFacing.EAST)};

        if (!flag1 && !flag2 && !aboolean[0] && !aboolean[1] && !aboolean[2] && !aboolean[3])
        {
            return false;
        }
        else
        {
            boolean flag3 = false;

            Material material = fluid.getBlock().getDefaultState().getMaterial();

            //IBlockState state = blockAccess.getBlockState(pos.add(i, 0, j));
            float height;
            if(blockAccess.getBlockState(blockPosIn.add(0, 1, 0)).getMaterial() == material) {
                height = 1F;
            } else {
                height = (float) 0.8888889;
            }

            //double d0 = (double)blockPosIn.getX();
            //double d1 = (double)blockPosIn.getY();
            //double d2 = (double)blockPosIn.getZ();
            double d0 = 0;
            double d1 = 0;
            double d2 = 0;
            if (flag1)
            {
                flag3 = true;
                //float f12 = BlockLiquid.getSlopeAngle(blockAccess, blockPosIn, material, blockStateIn);
                TextureAtlasSprite textureatlassprite = sprite;
                height -= 0.001F;
                float f13;
                float f14;
                float f15;
                float f16;
                float f17;
                float f18;
                float f19;
                float f20;

                //if (true)
                //{
                    f13 = textureatlassprite.getInterpolatedU(0.0D);
                    f17 = textureatlassprite.getInterpolatedV(0.0D);
                    f14 = f13;
                    f18 = textureatlassprite.getInterpolatedV(16.0D);
                    f15 = textureatlassprite.getInterpolatedU(16.0D);
                    f19 = f18;
                    f16 = f15;
                    f20 = f17;
                //}
                //else
                //{
                    //float f21 = MathHelper.sin(f12) * 0.25F;
                    //float f22 = MathHelper.cos(f12) * 0.25F;
                    //float f23 = 8.0F;
                    //f13 = textureatlassprite.getInterpolatedU((double)(8.0F + (-f22 - f21) * 16.0F));
                    //f17 = textureatlassprite.getInterpolatedV((double)(8.0F + (-f22 + f21) * 16.0F));
                    //f14 = textureatlassprite.getInterpolatedU((double)(8.0F + (-f22 + f21) * 16.0F));
                    //f18 = textureatlassprite.getInterpolatedV((double)(8.0F + (f22 + f21) * 16.0F));
                    //f15 = textureatlassprite.getInterpolatedU((double)(8.0F + (f22 + f21) * 16.0F));
                    //f19 = textureatlassprite.getInterpolatedV((double)(8.0F + (f22 - f21) * 16.0F));
                    //f16 = textureatlassprite.getInterpolatedU((double)(8.0F + (f22 - f21) * 16.0F));
                    //f20 = textureatlassprite.getInterpolatedV((double)(8.0F + (-f22 - f21) * 16.0F));
                //}

                //int k2 = blockStateIn.getPackedLightmapCoords(blockAccess, blockPosIn);
                //int l2 = k2 >> 16 & 65535;
                // int i3 = k2 & 65535;
                float f24 = 1.0F * f;
                float f25 = 1.0F * f1;
                float f26 = 1.0F * f2;
                bufferBuilderIn.pos(d0 + 0.0D, d1 + (double)height, d2 + 0.0D).tex((double)f13, (double)f17).color(f24, f25, f26, 1.0F).endVertex();
                bufferBuilderIn.pos(d0 + 0.0D, d1 + (double)height, d2 + 1.0D).tex((double)f14, (double)f18).color(f24, f25, f26, 1.0F).endVertex();
                bufferBuilderIn.pos(d0 + 1.0D, d1 + (double)height, d2 + 1.0D).tex((double)f15, (double)f19).color(f24, f25, f26, 1.0F).endVertex();
                bufferBuilderIn.pos(d0 + 1.0D, d1 + (double)height, d2 + 0.0D).tex((double)f16, (double)f20).color(f24, f25, f26, 1.0F).endVertex();

                if (shouldRenderSides(blockAccess, blockPosIn.up(), material))
                {
                    bufferBuilderIn.pos(d0 + 0.0D, d1 + (double)height, d2 + 0.0D).tex((double)f13, (double)f17).color(f24, f25, f26, 1.0F).endVertex();
                    bufferBuilderIn.pos(d0 + 1.0D, d1 + (double)height, d2 + 0.0D).tex((double)f16, (double)f20).color(f24, f25, f26, 1.0F).endVertex();
                    bufferBuilderIn.pos(d0 + 1.0D, d1 + (double)height, d2 + 1.0D).tex((double)f15, (double)f19).color(f24, f25, f26, 1.0F).endVertex();
                    bufferBuilderIn.pos(d0 + 0.0D, d1 + (double)height, d2 + 1.0D).tex((double)f14, (double)f18).color(f24, f25, f26, 1.0F).endVertex();
                }
            }

            if (flag2)
            {
                float f35 = sprite.getMinU();
                float f36 = sprite.getMaxU();
                float f37 = sprite.getMinV();
                float f38 = sprite.getMaxV();
                int l1 = blockStateIn.getPackedLightmapCoords(blockAccess, blockPosIn.down());
                int i2 = l1 >> 16 & 65535;
                int j2 = l1 & 65535;
                bufferBuilderIn.pos(d0, d1, d2 + 1.0D).tex((double)f35, (double)f38).color(0.5F, 0.5F, 0.5F, 1.0F).endVertex();
                bufferBuilderIn.pos(d0, d1, d2).tex((double)f35, (double)f37).color(0.5F, 0.5F, 0.5F, 1.0F).endVertex();
                bufferBuilderIn.pos(d0 + 1.0D, d1, d2).tex((double)f36, (double)f37).color(0.5F, 0.5F, 0.5F, 1.0F).endVertex();
                bufferBuilderIn.pos(d0 + 1.0D, d1, d2 + 1.0D).tex((double)f36, (double)f38).color(0.5F, 0.5F, 0.5F, 1.0F).endVertex();
                flag3 = true;
            }

            for (int i1 = 0; i1 < 4; ++i1)
            {
                int j1 = 0;
                int k1 = 0;

                if (i1 == 0)
                {
                    --k1;
                }

                if (i1 == 1)
                {
                    ++k1;
                }

                if (i1 == 2)
                {
                    --j1;
                }

                if (i1 == 3)
                {
                    ++j1;
                }

                BlockPos blockpos = blockPosIn.add(j1, 0, k1);
                TextureAtlasSprite textureatlassprite1 = sprite;

                //if (false)
                //{
                    //IBlockState state = blockAccess.getBlockState(blockpos);

                    //if (state.getBlockFaceShape(blockAccess, blockpos, EnumFacing.VALUES[i1+2].getOpposite()) == net.minecraft.block.state.BlockFaceShape.SOLID)
                    //{
                        //    textureatlassprite1 = this.atlasSpriteWaterOverlay;
                    //}
                //}

                if (aboolean[i1])
                {
                    float f39;
                    float f40;
                    double d3;
                    double d4;
                    double d5;
                    double d6;
                    f39 = height;
                    f40 = height;

                    if (i1 == 0)
                    {
                        d3 = d0;
                        d5 = d0 + 1.0D;
                        d4 = d2 + 0.0010000000474974513D;
                        d6 = d2 + 0.0010000000474974513D;
                    }
                    else if (i1 == 1)
                    {
                        d3 = d0 + 1.0D;
                        d5 = d0;
                        d4 = d2 + 1.0D - 0.0010000000474974513D;
                        d6 = d2 + 1.0D - 0.0010000000474974513D;
                    }
                    else if (i1 == 2)
                    {
                        d3 = d0 + 0.0010000000474974513D;
                        d5 = d0 + 0.0010000000474974513D;
                        d4 = d2 + 1.0D;
                        d6 = d2;
                    }
                    else
                    {
                        d3 = d0 + 1.0D - 0.0010000000474974513D;
                        d5 = d0 + 1.0D - 0.0010000000474974513D;
                        d4 = d2;
                        d6 = d2 + 1.0D;
                    }

                    flag3 = true;
                    float f41 = textureatlassprite1.getInterpolatedU(0.0D);
                    float f27 = textureatlassprite1.getInterpolatedU(8.0D);
                    float f28 = textureatlassprite1.getInterpolatedV((double)((1.0F - f39) * 16.0F * 0.5F));
                    float f29 = textureatlassprite1.getInterpolatedV((double)((1.0F - f40) * 16.0F * 0.5F));
                    float f30 = textureatlassprite1.getInterpolatedV(8.0D);
                    int j = blockStateIn.getPackedLightmapCoords(blockAccess, blockpos);
                    int k = j >> 16 & 65535;
                    int l = j & 65535;
                    float f31 = i1 < 2 ? 0.8F : 0.6F;
                    float f32 = 1.0F * f31 * f;
                    float f33 = 1.0F * f31 * f1;
                    float f34 = 1.0F * f31 * f2;
                    bufferBuilderIn.pos(d3, d1 + (double)f39, d4).tex((double)f41, (double)f28).color(f32, f33, f34, 1.0F).endVertex();
                    bufferBuilderIn.pos(d5, d1 + (double)f40, d6).tex((double)f27, (double)f29).color(f32, f33, f34, 1.0F).endVertex();
                    bufferBuilderIn.pos(d5, d1 + 0.0D, d6).tex((double)f27, (double)f30).color(f32, f33, f34, 1.0F).endVertex();
                    bufferBuilderIn.pos(d3, d1 + 0.0D, d4).tex((double)f41, (double)f30).color(f32, f33, f34, 1.0F).endVertex();

                    //if (textureatlassprite1 != this.atlasSpriteWaterOverlay && false)
                    //{
                    //    bufferBuilderIn.pos(d3, d1 + 0.0D, d4).color(f32, f33, f34, 1.0F).tex((double)f41, (double)f30).lightmap(k, l).endVertex();
                    //    bufferBuilderIn.pos(d5, d1 + 0.0D, d6).color(f32, f33, f34, 1.0F).tex((double)f27, (double)f30).lightmap(k, l).endVertex();
                    //    bufferBuilderIn.pos(d5, d1 + (double)f40, d6).color(f32, f33, f34, 1.0F).tex((double)f27, (double)f29).lightmap(k, l).endVertex();
                    //    bufferBuilderIn.pos(d3, d1 + (double)f39, d4).color(f32, f33, f34, 1.0F).tex((double)f41, (double)f28).lightmap(k, l).endVertex();
                    //}
                }
            }

            return flag3;
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldRenderSides(IBlockAccess blockAccess, BlockPos pos, Material material)
    {
        for (int i = -1; i <= 1; ++i)
        {
            for (int j = -1; j <= 1; ++j)
            {
                IBlockState iblockstate = blockAccess.getBlockState(pos.add(i, 0, j));

                if (iblockstate.getMaterial() != material && !iblockstate.isFullBlock())
                {
                    return true;
                }
            }
        }

        return false;
    }

    private float getFluidHeight(IBlockAccess blockAccess, BlockPos blockPosIn, Material blockMaterial)
    {
        int i = 0;
        float f = 0.0F;

        for (int j = 0; j < 4; ++j)
        {
            BlockPos blockpos = blockPosIn.add(-(j & 1), 0, -(j >> 1 & 1));

            if (blockAccess.getBlockState(blockpos.up()).getMaterial() == blockMaterial)
            {
                return 1.0F;
            }

            IBlockState iblockstate = blockAccess.getBlockState(blockpos);
            Material material = iblockstate.getMaterial();

            if (material != blockMaterial)
            {
                if (!material.isSolid())
                {
                    ++f;
                    ++i;
                }
            }
            else
            {
                int k = ((Integer)iblockstate.getValue(BlockLiquid.LEVEL)).intValue();

                if (k >= 8 || k == 0)
                {
                    f += BlockLiquid.getLiquidHeightPercent(k) * 10.0F;
                    i += 10;
                }

                f += BlockLiquid.getLiquidHeightPercent(k);
                ++i;
            }
        }

        return 1.0F - f / (float)i;
    }
}
