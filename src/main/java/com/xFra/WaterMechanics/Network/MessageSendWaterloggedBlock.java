package com.xFra.WaterMechanics.Network;

import com.xFra.WaterMechanics.WaterloggingManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentHashMap;

public class MessageSendWaterloggedBlock implements IMessage {
    private BlockPos pos;
    private String fluidName;
    //private NBTTagCompound tag;

    public MessageSendWaterloggedBlock(BlockPos pos, String fluidName) {
        this.pos = pos;
        this.fluidName = fluidName;
    }

    public MessageSendWaterloggedBlock() {
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

    /**
     * Convert from the supplied buffer into your specific message type
     *
     * @param buf The buffer
     */
    @Override
    public void fromBytes(ByteBuf buf) {
        //final int chunkX = buf.readInt();
        //final int chunkY = buf.readInt();
        //final int chunkZ = buf.readInt();
        //pos = new BlockPos(chunkX, chunkY, chunkZ);
        //fluidName = buf.toString();
        //fluidName = ByteBufUtils.readUTF8String(buf);
        NBTTagCompound tag = ByteBufUtils.readTag(buf);
        pos = getPosFromTag(tag);
        fluidName = tag.getString("Fluid");
    }

    /**
     * Deconstruct your message into the supplied byte buffer
     *
     * @param buf The buffer
     */
    @Override
    public void toBytes(ByteBuf buf) {
        //buf.writeInt(pos.getX());
        //buf.writeInt(pos.getY());
        //buf.writeInt(pos.getZ());
        //buf.writeBytes(fluidName.getBytes());
        //ByteBufUtils.writeUTF8String(buf, fluidName);
        ByteBufUtils.writeTag(buf, createWLTag(pos, fluidName));
    }

    public static class HandlerClient implements IMessageHandler<MessageSendWaterloggedBlock, IMessage> {

        /**
         * Called when a message is received of the appropriate type. You can optionally return a reply message, or null if no reply
         * is needed.
         *
         * @param message The message
         * @param ctx     The message context
         * @return An optional return message
         */
        @Nullable
        @Override
        public IMessage onMessage(MessageSendWaterloggedBlock message, MessageContext ctx) {
            //BlockPos blockPos = message.pos;
            //String Fluid = message.fluidName;
                ChunkPos chunkKey = ServerEvents.currentWorld.getChunkFromBlockCoords(message.pos).getPos();
                if (!(ServerEvents.data.containsKey(chunkKey))) {
                    ServerEvents.data.put(chunkKey, new ConcurrentHashMap<>());
                }
                ServerEvents.data.get(chunkKey).put(message.pos, message.fluidName);
                //BucketEvent.data.put(message.pos, message.fluidName);

            return null;
        }
    }

    public static class HandlerServer implements IMessageHandler<MessageSendWaterloggedBlock, IMessage> {

        /**
         * Called when a message is received of the appropriate type. You can optionally return a reply message, or null if no reply
         * is needed.
         *
         * @param message The message
         * @param ctx     The message context
         * @return An optional return message
         */
        @Nullable
        @Override
        public IMessage onMessage(MessageSendWaterloggedBlock message, MessageContext ctx) {
            //BlockPos blockPos = message.pos;
            //String Fluid = message.fluidName;
            WaterloggingManager.clientBlocks.put(message.pos, message.fluidName);
            return null;
        }
    }
}