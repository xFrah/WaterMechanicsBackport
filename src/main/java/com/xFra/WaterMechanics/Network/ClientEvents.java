package com.xFra.WaterMechanics.Network;

import com.xFra.WaterMechanics.Junk.DynamicModel;
import com.xFra.WaterMechanics.Junk.TankTESR;
import com.xFra.WaterMechanics.Renderer;
import com.xFra.WaterMechanics.WaterloggingManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector3d;
import java.util.Map;

public class ClientEvents {
    public static final ClientEvents instance = new ClientEvents();

    private ClientEvents() {};

//    @SubscribeEvent
//    public void onModelBakeEvent(ModelBakeEvent event)
//    {
//        Object object =  event.getModelRegistry().getObject(DynamicModel.variantTag);
//        if (object instanceof IBakedModel) {
//            IBakedModel existingModel = (IBakedModel)object;
//            DynamicModel customModel = new DynamicModel(existingModel);
//            event.getModelRegistry().putObject(DynamicModel.variantTag, customModel);
//        }
//    }

    @SubscribeEvent
    public void onRenderWorldLastEvent(final RenderWorldLastEvent event) {
        if(!(WaterloggingManager.clientBlocks.isEmpty())) {

            for (Map.Entry<BlockPos, String> entry : WaterloggingManager.clientBlocks.entrySet()) {
                //Minecraft.getMinecraft().player.sendMessage(new TextComponentString(WaterloggingManager.clientBlocks.size() + " render"));
                String value = entry.getValue();
                WaterloggingManager.tesr.render(entry.getKey().getX(), entry.getKey().getY(), entry.getKey().getZ(), FluidRegistry.getFluid(value));


            }

        }
    }
}