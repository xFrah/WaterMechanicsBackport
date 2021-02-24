package com.xFra.WaterMechanics.Junk;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WLBlock extends Block implements ITileEntityProvider{
    public IBlockState copiedstate;

    public WLBlock() {
        super(Material.ROCK);
        this.setRegistryName("waterlogged_block");
        this.setUnlocalizedName("Waterlogged Block");
    }
    public Fluid liquid;
    public AxisAlignedBB collblock;

    // the block will render in the SOLID layer.  See http://greyminecraftcoder.blogspot.co.at/2014/12/block-rendering-18.html for more information.
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.SOLID;
    }

    // used by the renderer to control lighting and visibility of other blocks.
    // set to true because this block is opaque and occupies the entire 1x1x1 space
    // not strictly required because the default (super method) is true
    @Override
    public boolean isOpaqueCube(IBlockState iBlockState) {
         return false;
    }

    // used by the renderer to control lighting and visibility of other blocks, also by
    // (eg) wall or fence to control whether the fence joins itself to this block
    // set to true because this block occupies the entire 1x1x1 space
    // not strictly required because the default (super method) is true
    @Override
    public boolean isFullCube(IBlockState iBlockState) {
        return false;
    }



    // render using an IBakedModel
    // not strictly required because the default (super method) is MODEL.

    @Override
    public EnumBlockRenderType getRenderType(IBlockState iBlockState) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        if (layer == BlockRenderLayer.TRANSLUCENT) {
            return true;
        } else if (layer == BlockRenderLayer.CUTOUT) {
            return true;
        } else if (layer == BlockRenderLayer.SOLID) {
            return true;
        } else {
            return true;
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        //Blocks.BEACON.
        return collblock;
    }

    // createBlockState is used to define which properties your blocks possess
    // Vanilla BlockState is composed of listed properties only.  A variant is created for each combination of listed
    //   properties; for example two properties ON(true/false) and READY(true/false) would give rise to four variants
    //   [on=true, ready=true]
    //   [on=false, ready=true]
    //   [on=true, ready=false]
    //   [on=false, ready=false]
    // Forge adds ExtendedBlockState, which has two types of property:
    // - listed properties (like vanilla), and
    // - unlisted properties, which can be used to convey information but do not cause extra variants to be created.
    @Override
    protected BlockStateContainer createBlockState() {
        IProperty [] listedProperties = new IProperty[] {}; // no listed properties
        IUnlistedProperty [] unlistedProperties = new IUnlistedProperty[] {COPIEDBLOCK, BLOCKPOS};
        return new ExtendedBlockState(this, listedProperties, unlistedProperties);
    }

    // this method uses the block state and BlockPos to update the unlisted COPIEDBLOCK property in IExtendedBlockState based
    // on non-metadata information.  This is then conveyed to the IBakedModel#getQuads during rendering.
    // In this case, we look around the camouflage block to find a suitable adjacent block it should camouflage itself as
    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (state instanceof IExtendedBlockState) {  // avoid crash in case of mismatch
            IExtendedBlockState retval = (IExtendedBlockState)state;
//            IBlockState bestAdjacentBlock = selectBlock(world, pos);
            TankBlockEntity teUp = (TankBlockEntity) Minecraft.getMinecraft().world.getTileEntity(pos);
            retval = retval.withProperty(COPIEDBLOCK, teUp.getState());
            retval = retval.withProperty(BLOCKPOS, pos);
            //IExtendedBlockState state0 = (IExtendedBlockState) new ExtendedBlockState(Blocks.WATER, new IProperty[]{BlockFluidBase.LEVEL}, BlockFluidBase.FLUID_RENDER_PROPS.toArray(new IUnlistedProperty<?>[0])).getBaseState();
            //for (int i = 0; i < 4; i++) state0 = state0.withProperty(BlockFluidBase.LEVEL_CORNERS[i], 15 / 16F);
            //state0 = (IExtendedBlockState) state0.withProperty(BlockFluidBase.LEVEL, 15);
            //retval = (IExtendedBlockState) retval.withProperty(BlockFluidBase.LEVEL, 15);

            return retval;
        }
        return state;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state;  //for debugging - useful spot for a breakpoint.  Not necessary.
    }

    // the COPIEDBLOCK property is used to store the identity of the block that BlockCamouflage will copy
    public static final UnlistedPropertyCopiedBlock COPIEDBLOCK = new UnlistedPropertyCopiedBlock();
    public static final UnlistedPropertyCopiedBlockPos BLOCKPOS = new UnlistedPropertyCopiedBlockPos();


    private IBlockState selectBlock(IBlockAccess world, BlockPos blockPos) {
        EnumFacing facing = EnumFacing.DOWN;
        BlockPos adjacentPosition = blockPos.add(facing.getFrontOffsetX(),
                facing.getFrontOffsetY(),
                facing.getFrontOffsetZ());
        IBlockState adjacentIBS = world.getBlockState(adjacentPosition);
        Block adjacentBlock = adjacentIBS.getBlock();
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(blockPos.toString()));
        collblock = adjacentIBS.getCollisionBoundingBox(world, adjacentPosition);
        if (adjacentBlock != Blocks.AIR && adjacentBlock != this) {
            return adjacentIBS;
        } else {
            return Blocks.REDSTONE_BLOCK.getDefaultState();
        }

//        if (adjacentBlockCount.isEmpty()) {
//            return UNCAMOUFLAGED_BLOCK;
//       }

//        if (adjacentSolidBlocks.size() == 1) {
//           IBlockState singleAdjacentBlock = adjacentSolidBlocks.firstEntry().getValue();
//            if (singleAdjacentBlock.getBlock() == this) {
//                return UNCAMOUFLAGED_BLOCK;
//            } else {
//                return singleAdjacentBlock;
//            }
//        }

    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TankBlockEntity();
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }
}
