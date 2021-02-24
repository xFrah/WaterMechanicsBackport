package com.xFra.WaterMechanics.Junk;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.property.IUnlistedProperty;

/**
 * Created by TheGreyGhost on 20/04/2015.
 */
public class UnlistedPropertyCopiedBlockPos implements IUnlistedProperty<BlockPos>
{
    @Override
    public String getName() {
        return "UnlistedPropertyCopiedBlockPos";
    }

    @Override
    public boolean isValid(BlockPos value) {
        return true;
    }

    @Override
    public Class<BlockPos> getType() {
        return BlockPos.class;
    }

    @Override
    public String valueToString(BlockPos value) {
        return value.toString();
    }
}