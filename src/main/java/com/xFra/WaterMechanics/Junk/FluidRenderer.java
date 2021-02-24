package com.xFra.WaterMechanics.Junk;

import com.xFra.WaterMechanics.RegistryHandler;
import com.xFra.WaterMechanics.Network.ServerEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static org.lwjgl.opengl.GL11.GL_QUADS;

@SideOnly(Side.CLIENT)
public class FluidRenderer
{
    private final TextureAtlasSprite[] atlasSpritesLava = new TextureAtlasSprite[2];
    private final TextureAtlasSprite[] atlasSpritesWater = new TextureAtlasSprite[2];
    private TextureAtlasSprite atlasSpriteWaterOverlay;
    private final TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();

    public FluidRenderer()
    {
        this.initAtlasSprites();
    }

    protected void initAtlasSprites()
    {
        //TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
        this.atlasSpritesLava[0] = texturemap.getAtlasSprite("minecraft:blocks/lava_still");
        this.atlasSpritesLava[1] = texturemap.getAtlasSprite("minecraft:blocks/lava_flow");
        this.atlasSpritesWater[0] = texturemap.getAtlasSprite("minecraft:blocks/water_still");
        this.atlasSpritesWater[1] = texturemap.getAtlasSprite("minecraft:blocks/water_flow");
        this.atlasSpriteWaterOverlay = texturemap.getAtlasSprite("minecraft:blocks/water_overlay");
    }

    public boolean renderFluid2(IBlockAccess blockAccess, IBlockState blockStateIn, BlockPos blockPosIn, BufferBuilder bufferBuilderIn)
    {
        //BlockLiquid blockliquid = (BlockLiquid)blockStateIn.getBlock();
        TankBlockEntity teUp = (TankBlockEntity) ServerEvents.currentWorld.getTileEntity(blockPosIn);
        if (teUp == null) return false;
        Material material = teUp.getFluid().getFluid().getBlock().getDefaultState().getMaterial();
        boolean flag = material == Material.LAVA;
        TextureAtlasSprite[] atextureatlassprite = flag ? this.atlasSpritesLava : this.atlasSpritesWater;
        int i = Minecraft.getMinecraft().getBlockColors().colorMultiplier(blockStateIn, blockAccess, blockPosIn, 0);
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
            float f3 = 0.5F;
            float f4 = 1.0F;
            float f5 = 0.8F;
            float f6 = 0.6F;
            //Material material = blockStateIn.getMaterial();
            float f7 = this.getFluidHeight(blockAccess, blockPosIn, material);
            float f8 = this.getFluidHeight(blockAccess, blockPosIn.south(), material);
            float f9 = this.getFluidHeight(blockAccess, blockPosIn.east().south(), material);
            float f10 = this.getFluidHeight(blockAccess, blockPosIn.east(), material);
            double d0 = (double)blockPosIn.getX();
            double d1 = (double)blockPosIn.getY();
            double d2 = (double)blockPosIn.getZ();
            float f11 = 0.001F;

            if (flag1)
            {
                flag3 = true;
                float f12 = getSlopeAngle(blockAccess, blockPosIn, material, blockStateIn);
                TextureAtlasSprite textureatlassprite = f12 > -999.0F ? atextureatlassprite[1] : atextureatlassprite[0];
                f7 -= 0.001F;
                f8 -= 0.001F;
                f9 -= 0.001F;
                f10 -= 0.001F;
                float f13;
                float f14;
                float f15;
                float f16;
                float f17;
                float f18;
                float f19;
                float f20;

                if (f12 < -999.0F)
                {
                    f13 = textureatlassprite.getInterpolatedU(0.0D);
                    f17 = textureatlassprite.getInterpolatedV(0.0D);
                    f14 = f13;
                    f18 = textureatlassprite.getInterpolatedV(16.0D);
                    f15 = textureatlassprite.getInterpolatedU(16.0D);
                    f19 = f18;
                    f16 = f15;
                    f20 = f17;
                }
                else
                {
                    float f21 = MathHelper.sin(f12) * 0.25F;
                    float f22 = MathHelper.cos(f12) * 0.25F;
                    float f23 = 8.0F;
                    f13 = textureatlassprite.getInterpolatedU((double)(8.0F + (-f22 - f21) * 16.0F));
                    f17 = textureatlassprite.getInterpolatedV((double)(8.0F + (-f22 + f21) * 16.0F));
                    f14 = textureatlassprite.getInterpolatedU((double)(8.0F + (-f22 + f21) * 16.0F));
                    f18 = textureatlassprite.getInterpolatedV((double)(8.0F + (f22 + f21) * 16.0F));
                    f15 = textureatlassprite.getInterpolatedU((double)(8.0F + (f22 + f21) * 16.0F));
                    f19 = textureatlassprite.getInterpolatedV((double)(8.0F + (f22 - f21) * 16.0F));
                    f16 = textureatlassprite.getInterpolatedU((double)(8.0F + (f22 - f21) * 16.0F));
                    f20 = textureatlassprite.getInterpolatedV((double)(8.0F + (-f22 - f21) * 16.0F));
                }

                int k2 = blockStateIn.getPackedLightmapCoords(blockAccess, blockPosIn);
                int l2 = k2 >> 16 & 65535;
                int i3 = k2 & 65535;
                float f24 = 1.0F * f;
                float f25 = 1.0F * f1;
                float f26 = 1.0F * f2;
                bufferBuilderIn.pos(d0 + 0.0D, d1 + (double)f7, d2 + 0.0D).color(f24, f25, f26, 1.0F).tex((double)f13, (double)f17).lightmap(l2, i3).endVertex();
                bufferBuilderIn.pos(d0 + 0.0D, d1 + (double)f8, d2 + 1.0D).color(f24, f25, f26, 1.0F).tex((double)f14, (double)f18).lightmap(l2, i3).endVertex();
                bufferBuilderIn.pos(d0 + 1.0D, d1 + (double)f9, d2 + 1.0D).color(f24, f25, f26, 1.0F).tex((double)f15, (double)f19).lightmap(l2, i3).endVertex();
                bufferBuilderIn.pos(d0 + 1.0D, d1 + (double)f10, d2 + 0.0D).color(f24, f25, f26, 1.0F).tex((double)f16, (double)f20).lightmap(l2, i3).endVertex();

                if (shouldRenderSides(blockAccess, blockPosIn.up(), material))
                {
                    bufferBuilderIn.pos(d0 + 0.0D, d1 + (double)f7, d2 + 0.0D).color(f24, f25, f26, 1.0F).tex((double)f13, (double)f17).lightmap(l2, i3).endVertex();
                    bufferBuilderIn.pos(d0 + 1.0D, d1 + (double)f10, d2 + 0.0D).color(f24, f25, f26, 1.0F).tex((double)f16, (double)f20).lightmap(l2, i3).endVertex();
                    bufferBuilderIn.pos(d0 + 1.0D, d1 + (double)f9, d2 + 1.0D).color(f24, f25, f26, 1.0F).tex((double)f15, (double)f19).lightmap(l2, i3).endVertex();
                    bufferBuilderIn.pos(d0 + 0.0D, d1 + (double)f8, d2 + 1.0D).color(f24, f25, f26, 1.0F).tex((double)f14, (double)f18).lightmap(l2, i3).endVertex();
                }
            }

            if (flag2)
            {
                float f35 = atextureatlassprite[0].getMinU();
                float f36 = atextureatlassprite[0].getMaxU();
                float f37 = atextureatlassprite[0].getMinV();
                float f38 = atextureatlassprite[0].getMaxV();
                int l1 = blockStateIn.getPackedLightmapCoords(blockAccess, blockPosIn.down());
                int i2 = l1 >> 16 & 65535;
                int j2 = l1 & 65535;
                bufferBuilderIn.pos(d0, d1, d2 + 1.0D).color(0.5F, 0.5F, 0.5F, 1.0F).tex((double)f35, (double)f38).lightmap(i2, j2).endVertex();
                bufferBuilderIn.pos(d0, d1, d2).color(0.5F, 0.5F, 0.5F, 1.0F).tex((double)f35, (double)f37).lightmap(i2, j2).endVertex();
                bufferBuilderIn.pos(d0 + 1.0D, d1, d2).color(0.5F, 0.5F, 0.5F, 1.0F).tex((double)f36, (double)f37).lightmap(i2, j2).endVertex();
                bufferBuilderIn.pos(d0 + 1.0D, d1, d2 + 1.0D).color(0.5F, 0.5F, 0.5F, 1.0F).tex((double)f36, (double)f38).lightmap(i2, j2).endVertex();
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
                TextureAtlasSprite textureatlassprite1 = atextureatlassprite[1];

                if (!flag)
                {
                    IBlockState state = blockAccess.getBlockState(blockpos);

                    if (state.getBlockFaceShape(blockAccess, blockpos, EnumFacing.VALUES[i1+2].getOpposite()) == net.minecraft.block.state.BlockFaceShape.SOLID)
                    {
                        textureatlassprite1 = this.atlasSpriteWaterOverlay;
                    }
                }

                if (aboolean[i1])
                {
                    float f39;
                    float f40;
                    double d3;
                    double d4;
                    double d5;
                    double d6;

                    if (i1 == 0)
                    {
                        f39 = f7;
                        f40 = f10;
                        d3 = d0;
                        d5 = d0 + 1.0D;
                        d4 = d2 + 0.0010000000474974513D;
                        d6 = d2 + 0.0010000000474974513D;
                    }
                    else if (i1 == 1)
                    {
                        f39 = f9;
                        f40 = f8;
                        d3 = d0 + 1.0D;
                        d5 = d0;
                        d4 = d2 + 1.0D - 0.0010000000474974513D;
                        d6 = d2 + 1.0D - 0.0010000000474974513D;
                    }
                    else if (i1 == 2)
                    {
                        f39 = f8;
                        f40 = f7;
                        d3 = d0 + 0.0010000000474974513D;
                        d5 = d0 + 0.0010000000474974513D;
                        d4 = d2 + 1.0D;
                        d6 = d2;
                    }
                    else
                    {
                        f39 = f10;
                        f40 = f9;
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
                    bufferBuilderIn.pos(d3, d1 + (double)f39, d4).color(f32, f33, f34, 1.0F).tex((double)f41, (double)f28).lightmap(k, l).endVertex();
                    bufferBuilderIn.pos(d5, d1 + (double)f40, d6).color(f32, f33, f34, 1.0F).tex((double)f27, (double)f29).lightmap(k, l).endVertex();
                    bufferBuilderIn.pos(d5, d1 + 0.0D, d6).color(f32, f33, f34, 1.0F).tex((double)f27, (double)f30).lightmap(k, l).endVertex();
                    bufferBuilderIn.pos(d3, d1 + 0.0D, d4).color(f32, f33, f34, 1.0F).tex((double)f41, (double)f30).lightmap(k, l).endVertex();

                    if (textureatlassprite1 != this.atlasSpriteWaterOverlay)
                    {
                        bufferBuilderIn.pos(d3, d1 + 0.0D, d4).color(f32, f33, f34, 1.0F).tex((double)f41, (double)f30).lightmap(k, l).endVertex();
                        bufferBuilderIn.pos(d5, d1 + 0.0D, d6).color(f32, f33, f34, 1.0F).tex((double)f27, (double)f30).lightmap(k, l).endVertex();
                        bufferBuilderIn.pos(d5, d1 + (double)f40, d6).color(f32, f33, f34, 1.0F).tex((double)f27, (double)f29).lightmap(k, l).endVertex();
                        bufferBuilderIn.pos(d3, d1 + (double)f39, d4).color(f32, f33, f34, 1.0F).tex((double)f41, (double)f28).lightmap(k, l).endVertex();
                    }
                }
            }
            Tessellator.getInstance().draw();

            GL11.glPopMatrix();
            return flag3;
        }
    }















    public void renderFluid(IBlockAccess blockAccess, IBlockState blockStateIn, BlockPos blockPosIn, FluidStack fluidstack, BufferBuilder bufferBuilder) {
        //BlockLiquid blockliquid = (BlockLiquid) blockStateIn.getBlock();
        Fluid fluid = fluidstack.getFluid();

        //Tessellator tessellator = Tessellator.getInstance();
        //BufferBuilder bufferBuilder = tessellator.getBuffer();
        ResourceLocation still = fluid.getStill();
        TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(still.toString());

        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();

        GlStateManager.color(1, 1, 1, 1f);
        //bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);


        TextureAtlasSprite[] atextureatlassprite = new TextureAtlasSprite[2];
        atextureatlassprite[0] = texturemap.getAtlasSprite(String.valueOf(fluid.getStill()));
        atextureatlassprite[1] = texturemap.getAtlasSprite(String.valueOf(fluid.getFlowing()));
        atlasSpriteWaterOverlay = texturemap.getAtlasSprite(String.valueOf(fluid.getOverlay()));


            int i = Minecraft.getMinecraft().getBlockColors().colorMultiplier(blockStateIn, blockAccess, blockPosIn, 0);
            float f = (float) (i >> 16 & 255) / 255.0F;
            float f1 = (float) (i >> 8 & 255) / 255.0F;
            float f2 = (float) (i & 255) / 255.0F;
            boolean flag1 = blockStateIn.shouldSideBeRendered(blockAccess, blockPosIn, EnumFacing.UP);
            boolean flag2 = blockStateIn.shouldSideBeRendered(blockAccess, blockPosIn, EnumFacing.DOWN);
            boolean[] aboolean = new boolean[]{blockStateIn.shouldSideBeRendered(blockAccess, blockPosIn, EnumFacing.NORTH), blockStateIn.shouldSideBeRendered(blockAccess, blockPosIn, EnumFacing.SOUTH), blockStateIn.shouldSideBeRendered(blockAccess, blockPosIn, EnumFacing.WEST), blockStateIn.shouldSideBeRendered(blockAccess, blockPosIn, EnumFacing.EAST)};

            if (!(!flag1 && !flag2 && !aboolean[0] && !aboolean[1] && !aboolean[2] && !aboolean[3])) {
                //boolean flag3 = false;
                Material material = fluid.getBlock().getDefaultState().getMaterial();
                float f7 = getFluidHeight(blockAccess, blockPosIn, material);
                float f8 = getFluidHeight(blockAccess, blockPosIn.south(), material);
                float f9 = getFluidHeight(blockAccess, blockPosIn.east().south(), material);
                float f10 = getFluidHeight(blockAccess, blockPosIn.east(), material);
                double d0 = (double) blockPosIn.getX();
                double d1 = (double) blockPosIn.getY();
                double d2 = (double) blockPosIn.getZ();

                if (flag1) {
                    //flag3 = true;
                    float f12 = getSlopeAngle(blockAccess, blockPosIn, material, blockStateIn);
                    TextureAtlasSprite textureatlassprite = f12 > -999.0F ? atextureatlassprite[1] : atextureatlassprite[0];
                    f7 -= 0.001F;
                    f8 -= 0.001F;
                    f9 -= 0.001F;
                    f10 -= 0.001F;
                    float f13;
                    float f14;
                    float f15;
                    float f16;
                    float f17;
                    float f18;
                    float f19;
                    float f20;

                    if (f12 < -999.0F) {
                        f13 = textureatlassprite.getInterpolatedU(0.0D);
                        f17 = textureatlassprite.getInterpolatedV(0.0D);
                        f14 = f13;
                        f18 = textureatlassprite.getInterpolatedV(16.0D);
                        f15 = textureatlassprite.getInterpolatedU(16.0D);
                        f19 = f18;
                        f16 = f15;
                        f20 = f17;
                    } else {
                        float f21 = MathHelper.sin(f12) * 0.25F;
                        float f22 = MathHelper.cos(f12) * 0.25F;
                        f13 = textureatlassprite.getInterpolatedU((double) (8.0F + (-f22 - f21) * 16.0F));
                        f17 = textureatlassprite.getInterpolatedV((double) (8.0F + (-f22 + f21) * 16.0F));
                        f14 = textureatlassprite.getInterpolatedU((double) (8.0F + (-f22 + f21) * 16.0F));
                        f18 = textureatlassprite.getInterpolatedV((double) (8.0F + (f22 + f21) * 16.0F));
                        f15 = textureatlassprite.getInterpolatedU((double) (8.0F + (f22 + f21) * 16.0F));
                        f19 = textureatlassprite.getInterpolatedV((double) (8.0F + (f22 - f21) * 16.0F));
                        f16 = textureatlassprite.getInterpolatedU((double) (8.0F + (f22 - f21) * 16.0F));
                        f20 = textureatlassprite.getInterpolatedV((double) (8.0F + (-f22 - f21) * 16.0F));
                    }

                    int k2 = blockStateIn.getPackedLightmapCoords(blockAccess, blockPosIn);
                    int l2 = k2 >> 16 & 65535;
                    int i3 = k2 & 65535;
                    float f24 = 1.0F * f;
                    float f25 = 1.0F * f1;
                    float f26 = 1.0F * f2;
                    bufferBuilder.pos(d0 + 0.0D, d1 + (double) f7, d2 + 0.0D).color(f24, f25, f26, 1.0F).tex((double) f13, (double) f17).lightmap(l2, i3).endVertex();
                    bufferBuilder.pos(d0 + 0.0D, d1 + (double) f8, d2 + 1.0D).color(f24, f25, f26, 1.0F).tex((double) f14, (double) f18).lightmap(l2, i3).endVertex();
                    bufferBuilder.pos(d0 + 1.0D, d1 + (double) f9, d2 + 1.0D).color(f24, f25, f26, 1.0F).tex((double) f15, (double) f19).lightmap(l2, i3).endVertex();
                    bufferBuilder.pos(d0 + 1.0D, d1 + (double) f10, d2 + 0.0D).color(f24, f25, f26, 1.0F).tex((double) f16, (double) f20).lightmap(l2, i3).endVertex();

                    if (shouldRenderSides(blockAccess, blockPosIn.up(), material)) {
                        bufferBuilder.pos(d0 + 0.0D, d1 + (double) f7, d2 + 0.0D).color(f24, f25, f26, 1.0F).tex((double) f13, (double) f17).lightmap(l2, i3).endVertex();
                        bufferBuilder.pos(d0 + 1.0D, d1 + (double) f10, d2 + 0.0D).color(f24, f25, f26, 1.0F).tex((double) f16, (double) f20).lightmap(l2, i3).endVertex();
                        bufferBuilder.pos(d0 + 1.0D, d1 + (double) f9, d2 + 1.0D).color(f24, f25, f26, 1.0F).tex((double) f15, (double) f19).lightmap(l2, i3).endVertex();
                        bufferBuilder.pos(d0 + 0.0D, d1 + (double) f8, d2 + 1.0D).color(f24, f25, f26, 1.0F).tex((double) f14, (double) f18).lightmap(l2, i3).endVertex();
                    }
                }

                if (flag2) {
                    float f35 = atextureatlassprite[0].getMinU();
                    float f36 = atextureatlassprite[0].getMaxU();
                    float f37 = atextureatlassprite[0].getMinV();
                    float f38 = atextureatlassprite[0].getMaxV();
                    int l1 = blockStateIn.getPackedLightmapCoords(blockAccess, blockPosIn.down());
                    int i2 = l1 >> 16 & 65535;
                    int j2 = l1 & 65535;
                    bufferBuilder.pos(d0, d1, d2 + 1.0D).color(0.5F, 0.5F, 0.5F, 1.0F).tex((double) f35, (double) f38).lightmap(i2, j2).endVertex();
                    bufferBuilder.pos(d0, d1, d2).color(0.5F, 0.5F, 0.5F, 1.0F).tex((double) f35, (double) f37).lightmap(i2, j2).endVertex();
                    bufferBuilder.pos(d0 + 1.0D, d1, d2).color(0.5F, 0.5F, 0.5F, 1.0F).tex((double) f36, (double) f37).lightmap(i2, j2).endVertex();
                    bufferBuilder.pos(d0 + 1.0D, d1, d2 + 1.0D).color(0.5F, 0.5F, 0.5F, 1.0F).tex((double) f36, (double) f38).lightmap(i2, j2).endVertex();
                    //flag3 = true;
                }

                for (int i1 = 0; i1 < 4; ++i1) {
                    int j1 = 0;
                    int k1 = 0;

                    if (i1 == 0) {
                        --k1;
                    }

                    if (i1 == 1) {
                        ++k1;
                    }

                    if (i1 == 2) {
                        --j1;
                    }

                    if (i1 == 3) {
                        ++j1;
                    }

                    BlockPos blockpos = blockPosIn.add(j1, 0, k1);
                    TextureAtlasSprite textureatlassprite1 = atextureatlassprite[1];

                    IBlockState state = blockAccess.getBlockState(blockpos);

                    if (state.getBlockFaceShape(blockAccess, blockpos, EnumFacing.VALUES[i1 + 2].getOpposite()) == net.minecraft.block.state.BlockFaceShape.SOLID) {
                        textureatlassprite1 = atlasSpriteWaterOverlay;
                    }

                    if (aboolean[i1]) {
                        float f39;
                        float f40;
                        double d3;
                        double d4;
                        double d5;
                        double d6;

                        if (i1 == 0) {
                            f39 = f7;
                            f40 = f10;
                            d3 = d0;
                            d5 = d0 + 1.0D;
                            d4 = d2 + 0.0010000000474974513D;
                            d6 = d2 + 0.0010000000474974513D;
                        } else if (i1 == 1) {
                            f39 = f9;
                            f40 = f8;
                            d3 = d0 + 1.0D;
                            d5 = d0;
                            d4 = d2 + 1.0D - 0.0010000000474974513D;
                            d6 = d2 + 1.0D - 0.0010000000474974513D;
                        } else if (i1 == 2) {
                            f39 = f8;
                            f40 = f7;
                            d3 = d0 + 0.0010000000474974513D;
                            d5 = d0 + 0.0010000000474974513D;
                            d4 = d2 + 1.0D;
                            d6 = d2;
                        } else {
                            f39 = f10;
                            f40 = f9;
                            d3 = d0 + 1.0D - 0.0010000000474974513D;
                            d5 = d0 + 1.0D - 0.0010000000474974513D;
                            d4 = d2;
                            d6 = d2 + 1.0D;
                        }

                        //flag3 = true;
                        float f41 = textureatlassprite1.getInterpolatedU(0.0D);
                        float f27 = textureatlassprite1.getInterpolatedU(8.0D);
                        float f28 = textureatlassprite1.getInterpolatedV((double) ((1.0F - f39) * 16.0F * 0.5F));
                        float f29 = textureatlassprite1.getInterpolatedV((double) ((1.0F - f40) * 16.0F * 0.5F));
                        float f30 = textureatlassprite1.getInterpolatedV(8.0D);
                        int j = blockStateIn.getPackedLightmapCoords(blockAccess, blockpos);
                        int k = j >> 16 & 65535;
                        int l = j & 65535;
                        float f31 = i1 < 2 ? 0.8F : 0.6F;
                        float f32 = 1.0F * f31 * f;
                        float f33 = 1.0F * f31 * f1;
                        float f34 = 1.0F * f31 * f2;
                        bufferBuilder.pos(d3, d1 + (double) f39, d4).color(f32, f33, f34, 1.0F).tex((double) f41, (double) f28).lightmap(k, l).endVertex();
                        bufferBuilder.pos(d5, d1 + (double) f40, d6).color(f32, f33, f34, 1.0F).tex((double) f27, (double) f29).lightmap(k, l).endVertex();
                        bufferBuilder.pos(d5, d1 + 0.0D, d6).color(f32, f33, f34, 1.0F).tex((double) f27, (double) f30).lightmap(k, l).endVertex();
                        bufferBuilder.pos(d3, d1 + 0.0D, d4).color(f32, f33, f34, 1.0F).tex((double) f41, (double) f30).lightmap(k, l).endVertex();

                        if (textureatlassprite1 != this.atlasSpriteWaterOverlay) {
                            bufferBuilder.pos(d3, d1 + 0.0D, d4).color(f32, f33, f34, 1.0F).tex((double) f41, (double) f30).lightmap(k, l).endVertex();
                            bufferBuilder.pos(d5, d1 + 0.0D, d6).color(f32, f33, f34, 1.0F).tex((double) f27, (double) f30).lightmap(k, l).endVertex();
                            bufferBuilder.pos(d5, d1 + (double) f40, d6).color(f32, f33, f34, 1.0F).tex((double) f27, (double) f29).lightmap(k, l).endVertex();
                            bufferBuilder.pos(d3, d1 + (double) f39, d4).color(f32, f33, f34, 1.0F).tex((double) f41, (double) f28).lightmap(k, l).endVertex();
                        }
                    }
                }

                //return flag3;

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

    public static BlockDynamicLiquid getFlowingBlock(Material materialIn)
    {
        if (materialIn == Material.WATER)
        {
            return Blocks.FLOWING_WATER;
        }
        else if (materialIn == Material.LAVA)
        {
            return Blocks.FLOWING_LAVA;
        }
        else
        {
            throw new IllegalArgumentException("Invalid material");
        }
    }

    @SideOnly(Side.CLIENT)
    public float getSlopeAngle(IBlockAccess worldIn, BlockPos pos, Material materialIn, IBlockState state)
    {
        Vec3d vec3d = getFlow(worldIn, pos, state, materialIn);
        return vec3d.x == 0.0D && vec3d.z == 0.0D ? -1000.0F : (float)MathHelper.atan2(vec3d.z, vec3d.x) - ((float)Math.PI / 2F);
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
                int k = (Integer) iblockstate.getValue(BlockLiquid.LEVEL);

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

    protected int getRenderedDepth(IBlockState state, Material material)
    {
        int i = getDepth(state, material);
        return i >= 8 ? 0 : i;
    }

    protected int getDepth(IBlockState state, Material material)
    {
        if(state.getBlock() == RegistryHandler.waterlogged.getDefaultState().getBlock()) {return 7;}
        Block block = state.getBlock();
        Block block2 = RegistryHandler.waterlogged.getDefaultState().getBlock();
        Material material2 = material;
        Material material3 = material;
        return state.getMaterial() == material ? ((Integer)state.getValue(BlockLiquid.LEVEL)).intValue() : -1;
    }

    protected Vec3d getFlow(IBlockAccess worldIn, BlockPos pos, IBlockState state, Material material)
    {
        double d0 = 0.0D;
        double d1 = 0.0D;
        double d2 = 0.0D;
        //int i = getRenderedDepth(state, state.getMaterial());
        int i = 15;
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
        {
            blockpos$pooledmutableblockpos.setPos(pos).move(enumfacing);
            IBlockState blockstate = worldIn.getBlockState(blockpos$pooledmutableblockpos);
            int j = getRenderedDepth(blockstate, material);

            if (j < 0)
            {
                if (!worldIn.getBlockState(blockpos$pooledmutableblockpos).getMaterial().blocksMovement())
                {
                    j = getRenderedDepth(worldIn.getBlockState(blockpos$pooledmutableblockpos.down()), material);

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

        //if (((Integer)state.getValue(LEVEL)).intValue() >= 8)
        if(true)
        {
            for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL)
            {
                blockpos$pooledmutableblockpos.setPos(pos).move(enumfacing1);

                if (causesDownwardCurrent(worldIn, blockpos$pooledmutableblockpos, enumfacing1, material) || causesDownwardCurrent(worldIn, blockpos$pooledmutableblockpos.up(), enumfacing1, material))
                {
                    vec3d = vec3d.normalize().addVector(0.0D, -6.0D, 0.0D);
                    break;
                }
            }
        }

        blockpos$pooledmutableblockpos.release();
        return vec3d.normalize();
    }

    private boolean causesDownwardCurrent(IBlockAccess worldIn, BlockPos pos, EnumFacing side, Material ourMaterial)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();
        Material material = iblockstate.getMaterial();

        if (material == ourMaterial)
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
            boolean flag = block instanceof BlockStairs;
            return !flag && iblockstate.getBlockFaceShape(worldIn, pos, side) == BlockFaceShape.SOLID;
        }
    }
}
