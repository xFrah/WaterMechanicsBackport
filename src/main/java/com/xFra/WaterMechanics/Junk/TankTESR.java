package com.xFra.WaterMechanics.Junk;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class TankTESR {


    public TankTESR() {

    }

    public void render(double x, double y, double z, Fluid fluid) {
        GlStateManager.pushMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.disableBlend();
        GlStateManager.translate((float)x, (float)y, (float)z);


        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        //bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        renderFluid(fluid);


        GlStateManager.popMatrix();
    }


    public void renderFluid(Fluid fluid) {
        if (true) {
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            ResourceLocation still = fluid.getStill();
            TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(still.toString());

            net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();

            GlStateManager.color(1, 1, 1, .5f);
            bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

            float u1 = sprite.getMinU();
            float v1 = sprite.getMinV();
            float u2 = sprite.getMaxU();
            float v2 = sprite.getMaxV();

            // Top
            bufferBuilder.pos(0, 1, 0).tex(u1, v1).color(255, 255, 255, 128).endVertex();
            bufferBuilder.pos(0, 1, 1).tex(u1, v2).color(255, 255, 255, 128).endVertex();
            bufferBuilder.pos(1, 1, 1).tex(u2, v2).color(255, 255, 255, 128).endVertex();
            bufferBuilder.pos(1, 1, 0).tex(u2, v1).color(255, 255, 255, 128).endVertex();

            // Bottom
            bufferBuilder.pos(1, 0, 0).tex(u2, v1).color(255, 255, 255, 128).endVertex();
            bufferBuilder.pos(1, 0, 1).tex(u2, v2).color(255, 255, 255, 128).endVertex();
            bufferBuilder.pos(0, 0, 1).tex(u1, v2).color(255, 255, 255, 128).endVertex();
            bufferBuilder.pos(0, 0, 0).tex(u1, v1).color(255, 255, 255, 128).endVertex();

            // Sides
            bufferBuilder.pos(0, 1, 1).tex(u1, v1).color(255, 255, 255, 128).endVertex();
            bufferBuilder.pos(0, 0, 1).tex(u1, v2).color(255, 255, 255, 128).endVertex();
            bufferBuilder.pos(1, 0, 1).tex(u2, v2).color(255, 255, 255, 128).endVertex();
            bufferBuilder.pos(1, 1, 1).tex(u2, v1).color(255, 255, 255, 128).endVertex();

            bufferBuilder.pos(1, 1, 0).tex(u2, v1).color(255, 255, 255, 128).endVertex();
            bufferBuilder.pos(1, 0, 0).tex(u2, v2).color(255, 255, 255, 128).endVertex();
            bufferBuilder.pos(0, 0, 0).tex(u1, v2).color(255, 255, 255, 128).endVertex();
            bufferBuilder.pos(0, 1, 0).tex(u1, v1).color(255, 255, 255, 128).endVertex();

            bufferBuilder.pos(1, 1, 1).tex(u2, v1).color(255, 255, 255, 128).endVertex();
            bufferBuilder.pos(1, 0, 1).tex(u2, v2).color(255, 255, 255, 128).endVertex();
            bufferBuilder.pos(1, 0, 0).tex(u1, v2).color(255, 255, 255, 128).endVertex();
            bufferBuilder.pos(1, 1, 0).tex(u1, v1).color(255, 255, 255, 128).endVertex();

            bufferBuilder.pos(0, 1, 0).tex(u1, v1).color(255, 255, 255, 128).endVertex();
            bufferBuilder.pos(0, 0, 0).tex(u1, v2).color(255, 255, 255, 128).endVertex();
            bufferBuilder.pos(0, 0, 1).tex(u2, v2).color(255, 255, 255, 128).endVertex();
            bufferBuilder.pos(0, 1, 1).tex(u2, v1).color(255, 255, 255, 128).endVertex();

            tessellator.draw();

            net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
        }
    }


}
