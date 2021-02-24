package com.xFra.WaterMechanics.Junk;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class TankTESRBackup extends TileEntitySpecialRenderer<TankBlockEntity> {

    public static final float TANK_THICKNESS = 0.0625F;

    public TankTESRBackup() {

    }

    @Override
    public void render(TankBlockEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.disableBlend();
        GlStateManager.translate((float)x, (float)y, (float)z);

        Tessellator tesselator = Tessellator.getInstance();

        BlockPos pos = te.getPos();

        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        renderFluid(tesselator, te, pos);


        GlStateManager.popMatrix();
    }

    private void renderFluid(Tessellator tesselator, TankBlockEntity tank, BlockPos pos) {
        if(tank == null) {
            return;
        }

        FluidStack stack = tank.getTank().getFluid();
        if(stack == null) {
            return;
        }

        Fluid renderFluid = stack.getFluid();
        if(renderFluid == null) {
            return;
        }

        float scale = ((1.0f - TANK_THICKNESS * 2) * stack.amount / (tank.getTank().getCapacity()));
        scale = 1f;
        if (scale > 0.0f) {
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder renderer = tessellator.getBuffer();
            ResourceLocation still = renderFluid.getStill();
            TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(still.toString());

            net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();

            GlStateManager.disableLighting();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.color(1, 1, 1, 1);


            renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);


            float u1 = sprite.getMinU();
            float v1 = sprite.getMinV();
            float u2 = sprite.getMaxU();
            float v2 = sprite.getMaxV();

            // Top
            renderer.pos(TANK_THICKNESS, scale + TANK_THICKNESS, TANK_THICKNESS).tex(u1, v1).color(255, 255, 255, 255).endVertex();
            renderer.pos(TANK_THICKNESS, scale + TANK_THICKNESS, 1-TANK_THICKNESS).tex(u1, v2).color(255, 255, 255, 255).endVertex();
            renderer.pos(1-TANK_THICKNESS, scale + TANK_THICKNESS, 1-TANK_THICKNESS).tex(u2, v2).color(255, 255, 255, 255).endVertex();
            renderer.pos(1-TANK_THICKNESS, scale + TANK_THICKNESS, TANK_THICKNESS).tex(u2, v1).color(255, 255, 255, 255).endVertex();

            // Bottom
            renderer.pos(1-TANK_THICKNESS, TANK_THICKNESS, TANK_THICKNESS).tex(u2, v1).color(255, 255, 255, 255).endVertex();
            renderer.pos(1-TANK_THICKNESS, TANK_THICKNESS, 1-TANK_THICKNESS).tex(u2, v2).color(255, 255, 255, 255).endVertex();
            renderer.pos(TANK_THICKNESS, TANK_THICKNESS, 1-TANK_THICKNESS).tex(u1, v2).color(255, 255, 255, 255).endVertex();
            renderer.pos(TANK_THICKNESS, TANK_THICKNESS, TANK_THICKNESS).tex(u1, v1).color(255, 255, 255, 255).endVertex();

            // Sides
            renderer.pos(TANK_THICKNESS, scale + TANK_THICKNESS, 1-TANK_THICKNESS).tex(u1, v1).color(255, 255, 255, 255).endVertex();
            renderer.pos(TANK_THICKNESS, TANK_THICKNESS, 1-TANK_THICKNESS).tex(u1, v2).color(255, 255, 255, 255).endVertex();
            renderer.pos(1-TANK_THICKNESS, TANK_THICKNESS, 1-TANK_THICKNESS).tex(u2, v2).color(255, 255, 255, 255).endVertex();
            renderer.pos(1-TANK_THICKNESS, scale + TANK_THICKNESS, 1-TANK_THICKNESS).tex(u2, v1).color(255, 255, 255, 255).endVertex();

            renderer.pos(1-TANK_THICKNESS, scale + TANK_THICKNESS, TANK_THICKNESS).tex(u2, v1).color(255, 255, 255, 255).endVertex();
            renderer.pos(1-TANK_THICKNESS, TANK_THICKNESS, TANK_THICKNESS).tex(u2, v2).color(255, 255, 255, 255).endVertex();
            renderer.pos(TANK_THICKNESS, TANK_THICKNESS, TANK_THICKNESS).tex(u1, v2).color(255, 255, 255, 255).endVertex();
            renderer.pos(TANK_THICKNESS, scale + TANK_THICKNESS, TANK_THICKNESS).tex(u1, v1).color(255, 255, 255, 255).endVertex();

            renderer.pos(1-TANK_THICKNESS, scale + TANK_THICKNESS, 1-TANK_THICKNESS).tex(u2, v1).color(255, 255, 255, 255).endVertex();
            renderer.pos(1-TANK_THICKNESS, TANK_THICKNESS, 1-TANK_THICKNESS).tex(u2, v2).color(255, 255, 255, 255).endVertex();
            renderer.pos(1-TANK_THICKNESS, TANK_THICKNESS, TANK_THICKNESS).tex(u1, v2).color(255, 255, 255, 255).endVertex();
            renderer.pos(1-TANK_THICKNESS, scale + TANK_THICKNESS, TANK_THICKNESS).tex(u1, v1).color(255, 255, 255, 255).endVertex();

            renderer.pos(TANK_THICKNESS, scale + TANK_THICKNESS, TANK_THICKNESS).tex(u1, v1).color(255, 255, 255, 255).endVertex();
            renderer.pos(TANK_THICKNESS, TANK_THICKNESS, TANK_THICKNESS).tex(u1, v2).color(255, 255, 255, 255).endVertex();
            renderer.pos(TANK_THICKNESS, TANK_THICKNESS, 1-TANK_THICKNESS).tex(u2, v2).color(255, 255, 255, 255).endVertex();
            renderer.pos(TANK_THICKNESS, scale + TANK_THICKNESS, 1-TANK_THICKNESS).tex(u2, v1).color(255, 255, 255, 255).endVertex();



            tessellator.draw();

            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDisable(GL11.GL_BLEND);
            GlStateManager.enableLighting();
            net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
        }
    }


}
