package com.xFra.WaterMechanics.Network;

import com.xFra.WaterMechanics.Junk.FluidRenderer;
import com.xFra.WaterMechanics.Junk.TankBlockEntity;
import com.xFra.WaterMechanics.RegistryHandler;
import com.xFra.WaterMechanics.WaterMechanicsCore;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerEvents {
    private static final FluidRenderer fluidrenderer = new FluidRenderer();
    public static ConcurrentHashMap<ChunkPos, ConcurrentHashMap<BlockPos, String>> data = new ConcurrentHashMap<>();
    public static World currentWorld;

    public ServerEvents() {};


    @SubscribeEvent
    public static void onChunkLoad(@org.jetbrains.annotations.NotNull ChunkDataEvent.Load event) {
        //if(event.getWorld().isRemote)
           // return;

        NBTTagCompound compound = event.getData();

        if(compound.hasKey("waterlogging")) {
            NBTTagList tag = compound.getTagList("waterlogging", Constants.NBT.TAG_COMPOUND);
            for(NBTBase basetaggina : tag) {
                NBTTagCompound taggina = (NBTTagCompound) basetaggina;
                //data.put(getPosFromTag(taggina), taggina.getString("Fluid"));
                ChunkPos chunkKey = currentWorld.getChunkFromBlockCoords(getPosFromTag(taggina)).getPos();
                if (!(data.containsKey(chunkKey))) {
                    data.put(chunkKey, new ConcurrentHashMap<>());
                }
                data.get(chunkKey).put(getPosFromTag(taggina), taggina.getString("Fluid"));
                try {
                    //Minecraft.getMinecraft().player.sendMessage(new TextComponentString("load"));
                    //Minecraft.getMinecraft().player.sendMessage(new TextComponentString(data.size() + " load"));
                } catch (Exception ignored){

                }
            }
        }
    }

    @SubscribeEvent
    public static void onChunkUnload(ChunkEvent.Unload event) {
        //if(event.getWorld().isRemote)
        //return;
        data.remove(event.getChunk().getPos());
        try {
            //Minecraft.getMinecraft().player.sendMessage(new TextComponentString(String.valueOf(data.size()) + " remove"));
        } catch (Exception ignored){}
    }

    @SubscribeEvent
    public static void onChunkSave(@org.jetbrains.annotations.NotNull ChunkDataEvent.Save event) {
        //if(event.getWorld().isRemote)
        //return;
        try {
            //Minecraft.getMinecraft().player.sendMessage(new TextComponentString(String.valueOf(data.size())));
        } catch (Exception ignored){}
        NBTTagList nbtlist = new NBTTagList();
        NBTTagCompound compound = event.getData();
        ConcurrentHashMap<BlockPos, String> data2 = data.get(event.getChunk().getPos());
        try {
            data2.forEach((key, value) -> {
                nbtlist.appendTag(createWLTag(key, value));
            });
        }catch (Exception ignored){}

        compound.setTag("waterlogging", nbtlist);

    }

    @SubscribeEvent
    public static void chunkWatch(final ChunkWatchEvent.Watch event) {
        //final EntityPlayerMP player = event.getPlayer();
        if (data.containsKey(event.getChunkInstance().getPos())) {
            ConcurrentHashMap<BlockPos, String> data2 = data.get(event.getChunkInstance().getPos());
            try {
                for (Map.Entry<BlockPos, String> entry : data2.entrySet()) {
                    BlockPos key = entry.getKey();
                    String value = entry.getValue();
                    MessageSendWaterloggedBlock packet = new MessageSendWaterloggedBlock(key, value);
                    WaterMechanicsCore.INSTANCE.sendTo(packet, event.getPlayer());
                }
            }catch (Exception ignored){}
        }
        //.sendTo(packet, player);
    }

    @SubscribeEvent
    public static void chunkUnwatch(final ChunkWatchEvent.UnWatch event) {
        //final EntityPlayerMP player = event.getPlayer();
        if (data.containsKey(event.getChunkInstance().getPos())) {
            ConcurrentHashMap<BlockPos, String> data2 = data.get(event.getChunkInstance().getPos());
            try {
                for (Map.Entry<BlockPos, String> entry : data2.entrySet()) {
                    BlockPos key = entry.getKey();
                    String value = entry.getValue();
                    MessageSendWaterloggedBlock packet = new MessageSendWaterloggedBlock(key, value);
                    WaterMechanicsCore.INSTANCE.sendTo(packet, event.getPlayer());
                }
            }catch (Exception ignored){}
        }
        //.sendTo(packet, player);
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent event) {

        currentWorld = event.world;
    }

    public static BlockPos getPosFromTag(NBTTagCompound tag)
    {
        return new BlockPos(tag.getInteger("X"), tag.getInteger("Y"), tag.getInteger("Z"));
    }

    public static NBTTagCompound createWLTag(BlockPos pos, String fluidName)
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setInteger("X", pos.getX());
        nbttagcompound.setInteger("Y", pos.getY());
        nbttagcompound.setInteger("Z", pos.getZ());
        nbttagcompound.setString("Fluid", fluidName);
        return nbttagcompound;
    }

    @SubscribeEvent
    public static void onBucketUse(FillBucketEvent e) {
        e.setCanceled(true);
        EnumHand hand = e.getEntityPlayer().getActiveHand();
        if (hand != EnumHand.MAIN_HAND) return;
        if (e.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.BUCKET) {
            return;
        }
        EntityPlayer player = e.getEntityPlayer();
        //Chunk chunk = RegistryHandler.currentWorld.getChunkFromBlockCoords(player.getPosition());
        if (player == null) return;
        RayTraceResult target =e.getTarget();
        if (target == null) return;
        World worldIn = e.getWorld();
        BlockPos pos = target.getBlockPos();
        IBlockState copiedblockstate = worldIn.getBlockState(pos);
        ItemStack itemstack = e.getEmptyBucket();
        Item itemHeld = itemstack.getItem();
        Fluid fluid = null;

        if (itemHeld == Items.WATER_BUCKET || itemHeld == Items.LAVA_BUCKET) {

            fluid = itemHeld == Items.WATER_BUCKET ? FluidRegistry.WATER : FluidRegistry.LAVA;

            if (!player.isCreative()) {
                player.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.BUCKET));
            }

        } else if (itemHeld instanceof UniversalBucket) {

            UniversalBucket bucket = (UniversalBucket) itemHeld;
            FluidStack fluidStack = bucket.getFluid(itemstack);
            assert fluidStack != null;
            fluid = fluidStack.getFluid();
            player.setHeldItem(EnumHand.MAIN_HAND, bucket.getEmpty());
        }


        //RegistryHandler.waterlogged.copiedstate = copiedblockstate;
        //worldIn.setBlockToAir(pos);

        //worldIn.setBlockState(pos, RegistryHandler.waterlogged.getBlockState().getBaseState());
        //TankBlockEntity teUp = (TankBlockEntity) currentWorld.getTileEntity(pos);
        //teUp.setFillLevel(15, true);

        if(!worldIn.isRemote) {
            MessageSendWaterloggedBlock packet = new MessageSendWaterloggedBlock(pos, fluid.getName());
            WaterMechanicsCore.INSTANCE.sendToServer(packet);
        }

    }


}