package org.zleub.makomod;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.logging.Logger;

import static net.minecraftforge.fluids.capability.CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;

public class BlockStonedPillar extends Block {
    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_0_3;

    public BlockStonedPillar(Block.Properties props) {
        super(props);
        this.setDefaultState(this.stateContainer.getBaseState().with(LEVEL, 0));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }

    public void updateTankNeigbors(World world, BlockPos pos) {
        TileTank te = (TileTank)world.getTileEntity(pos);
        TileEntity up = world.getTileEntity(pos.up());
        TileEntity down = world.getTileEntity(pos.down());

        if (up != null && up.getCapability(FLUID_HANDLER_CAPABILITY, Direction.DOWN).isPresent()) {
            te.registerNeighbors(up.getCapability(FLUID_HANDLER_CAPABILITY), Direction.UP);
        }
        if (down != null && down.getCapability(FLUID_HANDLER_CAPABILITY, Direction.UP).isPresent()) {
            te.registerNeighbors(down.getCapability(FLUID_HANDLER_CAPABILITY), Direction.DOWN);
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack item) {
        super.onBlockPlacedBy(world, pos, state, entity, item);
        updateTankNeigbors(world, pos);
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block bock, BlockPos update_pos, boolean bool) {
        super.neighborChanged(state, world, pos, bock, update_pos, bool);
        updateTankNeigbors(world, pos);

        BlockPos test = update_pos.subtract( pos );
        Direction from = Direction.func_218383_a(test.getX(), test.getY(), test.getZ());
//        world.notifyNeighborsOfStateExcept(pos, this, from);
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack heldItem = player.getHeldItem(handIn);
        TileTank te = (TileTank) world.getTileEntity(pos);
        LazyOptional<IFluidHandler> fluidHandler = te.getCapability(FLUID_HANDLER_CAPABILITY, hit.getFace());

        if (FluidUtil.interactWithFluidHandler(player, handIn, world, te.getPos(), hit.getFace())) {
            updateTankNeigbors(world, pos);
            return true;
        }

        return super.onBlockActivated(state, world, pos, player, handIn, hit);
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return false;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileTank(TileTank.TankType);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public boolean hasTileEntity() {
        return true;
    }

}
