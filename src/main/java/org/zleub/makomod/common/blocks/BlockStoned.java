package org.zleub.makomod.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.zleub.makomod.common.tiles.TilePillar;

import javax.annotation.Nullable;

import static net.minecraftforge.fluids.capability.CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;

public class BlockStoned extends Block {
    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;

    public BlockStoned(Block.Properties props) {
        super(props);
        this.setDefaultState(this.stateContainer.getBaseState().with(ENABLED, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(ENABLED);
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    public void updatePillarNeighors(World world, BlockPos pos) {
        BlockPos i = new BlockPos(pos.up());
        while ( world.getTileEntity(i) != null && world.getTileEntity(i).getCapability(FLUID_HANDLER_CAPABILITY).isPresent() )
            i = i.up();

        boolean check = (!i.equals(pos.up())) && world.getBlockState(i).getBlock() instanceof BlockStoned;
        BlockState newstate = this.getDefaultState().with(ENABLED, check);
        world.setBlockState(pos, newstate);
        world.notifyBlockUpdate(pos, newstate, newstate, 2);
//        if (check) {
//            world.setBlockState(i, newstate);
//            world.notifyBlockUpdate(i, newstate, newstate, 2);
//        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entiy, ItemStack item) {
        super.onBlockPlacedBy(world, pos, state, entiy, item);
        updatePillarNeighors(world, pos);
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos update_pos, boolean bool) {
        super.neighborChanged(state, world, pos, block, update_pos, bool);
        updatePillarNeighors(world, pos);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if ( state.get(ENABLED) )
            return new TilePillar(TilePillar.PillarType);
        return null;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.get(ENABLED);
    }
}