package com.xFra.WaterMechanics.Junk;

import com.xFra.WaterMechanics.RegistryHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

/**
 * Holds {@link TileEntity} data for TankBlocks,
 */
public class TankBlockEntity extends TileEntity implements IFluidHandler
{
    /**
     * The fill level of the tank.
     */
    private int fillLevel;
    private FluidTank internalTank;
    private IBlockState state;

    /**
     * Default constructor.
     */
    public TankBlockEntity()
    {
        super();
        internalTank = new FluidTank(new FluidStack( FluidRegistry.WATER, Fluid.BUCKET_VOLUME), 1);
        fillLevel = 0;
        this.state = RegistryHandler.waterlogged.copiedstate;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        fillLevel = tag.getByte("FillLevel");
        internalTank.readFromNBT(tag);
        this.state = NBTUtil.readBlockState(tag);
    }

    public void setFluid(FluidStack fluidstack) {
        internalTank.setFluid(fluidstack);
    }

    public FluidStack getFluid(FluidStack fluidstack) {
        return internalTank.getFluid();
    }

    public IBlockState getState() {
        return this.state;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        internalTank.writeToNBT(tag);
        tag.setByte("FillLevel", (byte) fillLevel);
        NBTUtil.writeBlockState(tag, state);
        return tag;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound tag = super.getUpdateTag();
        writeToNBT(tag);
        return tag;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(pos, -1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet)
    {
        int oldFillLevel = fillLevel;
        readFromNBT(packet.getNbtCompound());
    }


    /**
     * Checks if the TankBlock is empty.
     *
     * @return <code>true</code> if the TankBlock is empty, otherwise false.
     */
    public boolean isEmpty()
    {
        return fillLevel == 0;
    }


    /**
     * Gets the TankBlocks current fill level.
     *
     * @return The TankBlocks filling level in percent.
     */
    public int getFillLevel()
    {
        return fillLevel;
    }

    public static void syncBlockAndRerender(World world, BlockPos pos)
    {
        if (world == null || pos == null) return;

        IBlockState state = world.getBlockState(pos);

        world.markAndNotifyBlock(pos, null, state, state, 2);
    }

    /**
     * Sets the TankBlocks current fill level.
     *
     * @param level
     * The TankBlocks fill level.
     * @param forceBlockUpdate
     * Specifies if a block update should be forced.
     * @return <code>true</code> if the fill level has changed, otherwise <code>false</code>.
     */
    public boolean setFillLevel(int level, boolean forceBlockUpdate)
    {
        level = MathHelper.clamp(level, 0, 16);

        boolean levelChanged = (level != fillLevel);

        fillLevel = level;

        if (levelChanged || forceBlockUpdate)
        {
            syncBlockAndRerender(world, pos);
            markDirty();
        }

        return levelChanged;
    }

    /**
     * Gets the {@link Fluid} inside the multiblock tank structure.
     *
     * @return The fluid or <code>null</code> if the TankBlock is not linked to a ValveBlock or the multiblock tank is empty.
     */
    public FluidStack getFluid()
    {
        return internalTank.getFluid();
    }

    public int getCapacity()
    {
        return internalTank.getCapacity();
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return new IFluidTankProperties[0];
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return internalTank == null ? 0 : internalTank.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        return internalTank == null ? null : internalTank.drain(resource, doDrain);
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return internalTank == null ? null : internalTank.drain(maxDrain, doDrain);
    }

    public IFluidTank getTank() {
        return internalTank;
    }

}