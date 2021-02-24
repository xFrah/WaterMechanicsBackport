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

public class MessageRemoveWaterloggedBlock implements IMessage {
    private BlockPos pos;
    //private NBTTagCompound tag;

    public MessageRemoveWaterloggedBlock(BlockPos pos) {
        this.pos = pos;
    }

    public MessageRemoveWaterloggedBlock() {
    }

    /**
     * Convert from the supplied buffer into your specific message type
     *
     * @param buf The buffer
     */
    @Override
    public void fromBytes(ByteBuf buf) {
        final int chunkX = buf.readInt();
        final int chunkY = buf.readInt();
        final int chunkZ = buf.readInt();
        pos = new BlockPos(chunkX, chunkY, chunkZ);
    }

    /**
     * Deconstruct your message into the supplied byte buffer
     *
     * @param buf The buffer
     */
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    public static class HandlerClient implements IMessageHandler<MessageRemoveWaterloggedBlock, IMessage> {

        @Nullable
        @Override
        public IMessage onMessage(MessageRemoveWaterloggedBlock message, MessageContext ctx) {
            //BlockPos blockPos = message.pos;
            WaterloggingManager.clientBlocks.remove(message.pos);
            return null;
        }
    }

    public static class HandlerServer implements IMessageHandler<MessageRemoveWaterloggedBlock, IMessage> {

        @Nullable
        @Override
        public IMessage onMessage(MessageRemoveWaterloggedBlock message, MessageContext ctx) {
            //TODO QUANDO UN PLAYER RIMUOVE IL BLOCCO
            //BlockPos blockPos = message.pos;
            return null;
        }
    }
}