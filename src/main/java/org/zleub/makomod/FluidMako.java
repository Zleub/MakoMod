package org.zleub.makomod;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.item.Item;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidMako extends FlowingFluid {
    static FluidAttributes.Builder attr = FluidAttributes.builder(
        new ResourceLocation("makomod:block/mako_still"),
        new ResourceLocation("makomod:block/mako_flow")
    ).overlay( new ResourceLocation("makomod:block/mako_overlay"));

    static ForgeFlowingFluid.Properties props = new ForgeFlowingFluid.Properties(
        RegistryObject.of( new ResourceLocation("makomod:mako_still"), ForgeRegistries.FLUIDS ),
        RegistryObject.of( new ResourceLocation("makomod:mako_flowing"), ForgeRegistries.FLUIDS ),
        attr)
            .bucket( RegistryObject.of( new ResourceLocation("makomod:mako_bucket"), ForgeRegistries.ITEMS))
            .block( RegistryObject.of (new ResourceLocation("makomod:mako"), ForgeRegistries.BLOCKS));

    @Override
    public Fluid getFlowingFluid() {
        return ForgeRegistries.FLUIDS.getValue( new ResourceLocation("makomod:mako_flowing"));
    }

    @Override
    public Fluid getStillFluid() {
        return ForgeRegistries.FLUIDS.getValue( new ResourceLocation("makomod:mako_still"));
    }

    /**
     * Creates the fluid attributes object, which will contain all the extended values for the fluid that aren't part of the vanilla system.
     * Do not call this from outside. To retrieve the values use {@link Fluid#getAttributes()}
     */
    @Override
    protected FluidAttributes createAttributes() {
        return attr.build(this);
    }

    @Override
    protected boolean canSourcesMultiply() {
        return false;
    }

    @Override
    protected void beforeReplacingBlock(IWorld iWorld, BlockPos blockPos, BlockState blockState) {

    }

    @Override
    protected int getSlopeFindDistance(IWorldReader iWorldReader) {
        return 0;
    }

    @Override
    protected int getLevelDecreasePerBlock(IWorldReader iWorldReader) {
        return 0;
    }

    /**
     * Gets the render layer this block will render on. SOLID for solid blocks, CUTOUT or CUTOUT_MIPPED for on-off
     * transparency (glass, reeds), TRANSLUCENT for fully blended transparency (stained glass)
     */
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public Item getFilledBucket() {
        return null;
    }

    @Override
    protected boolean func_215665_a(IFluidState p_215665_1_, IBlockReader p_215665_2_, BlockPos p_215665_3_, Fluid p_215665_4_, Direction p_215665_5_) {
        return false;
    }

    @Override
    public int getTickRate(IWorldReader p_205569_1_) {
        return 0;
    }

    @Override
    protected float getExplosionResistance() {
        return 0;
    }

    @Override
    protected BlockState getBlockState(IFluidState state) {
        return ForgeRegistries.FLUIDS.getValue(new ResourceLocation("makomod:mako_still")).getDefaultState().with(FlowingFluidBlock.LEVEL, 15).getBlockState();
    }

    @Override
    public boolean isSource(IFluidState state) {
        return false;
    }

    @Override
    public int getLevel(IFluidState p_207192_1_) {
        return 0;
    }

//    public static class Flowing extends FluidMako {
//        public Flowing() {
//        }
//
//        protected void fillStateContainer(StateContainer.Builder<Fluid, IFluidState> p_207184_1_) {
//            super.fillStateContainer(p_207184_1_);
//            p_207184_1_.add(new IProperty[]{LEVEL_1_8});
//        }
//
//        public int getLevel(IFluidState p_207192_1_) {
//            return (Integer)p_207192_1_.get(LEVEL_1_8);
//        }
//
//        public boolean isSource(IFluidState p_207193_1_) {
//            return false;
//        }
//    }
//
//    public static class Source extends FluidMako {
//        public Source() {
//        }
//
//        public int getLevel(IFluidState p_207192_1_) {
//            return 8;
//        }
//
//        public boolean isSource(IFluidState p_207193_1_) {
//            return true;
//        }
//    }
}
