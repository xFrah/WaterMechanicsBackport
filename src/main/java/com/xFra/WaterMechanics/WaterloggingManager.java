package com.xFra.WaterMechanics;

import com.xFra.WaterMechanics.Junk.TankTESR;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;

public class WaterloggingManager {

    public static HashMap<BlockPos, String> clientBlocks = new HashMap<>();
    public static final Renderer fluidRenderer = new Renderer(Minecraft.getMinecraft().getBlockColors());
    public static TankTESR tesr = new TankTESR();
}
