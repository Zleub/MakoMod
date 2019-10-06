package org.zleub.makomod;

import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.resources.IResourceManager;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.common.extensions.IForgeBlock;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.block.BlockState;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import javax.annotation.processing.SupportedSourceVersion;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public class BlockStonedPillar extends Block
{
    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_0_8;

    public BlockStonedPillar(Block.Properties props) {
        super(props);
//        this.setDefaultState(this.stateContainer.getBaseState().with(LEVEL, Integer.valueOf(1)));
    }

//    @Override
//    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
//      builder.add(LEVEL);
//    }
//
    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        byte level = worldIn.getTileEntity(pos).getTileData().getByte("level");
        LOGGER.info(level);
        worldIn.getTileEntity(pos).getTileData().putByte("level", (byte)(level + 1));
        worldIn.notifyBlockUpdate(pos, state, state, 2);

    	ItemStack heldItem = player.getHeldItem(handIn);
        TileEntity te = worldIn.getTileEntity(pos);
   	    LazyOptional<IFluidHandler> fluidHandler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);

    	 if(FluidUtil.interactWithFluidHandler(player, handIn,worldIn, te.getPos(), null)) {
          return true; // return true as we did something
        }
//    	LOGGER.info(heldItem.getItem().properties);
//        if (heldItem)
        return FluidUtil.getFluidHandler(heldItem) != null;

    }

    @Override
    public BlockRenderLayer getRenderLayer() {
      return BlockRenderLayer.TRANSLUCENT;
   }

   @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
      return false;
    }

    /**
     * Called throughout the code as a replacement for ITileEntityProvider.createNewTileEntity
     * Return the same thing you would from that function.
     * This will fall back to ITileEntityProvider.createNewTileEntity(World) if this block is a ITileEntityProvider
     *
     * @param state The state of the current block
     * @param world The world to create the TE in
     * @return A instance of a class extending TileEntity
     */
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileTank( TileTank.TankType );
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public boolean hasTileEntity()
    {
        return true;
    }

}
