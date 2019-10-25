package org.zleub.makomod;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
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
    ).overlay(new ResourceLocation("makomod:block/mako_overlay"));

    static ForgeFlowingFluid.Properties props = new ForgeFlowingFluid.Properties(
            RegistryObject.of(new ResourceLocation("makomod:mako_still"), ForgeRegistries.FLUIDS),
            RegistryObject.of(new ResourceLocation("makomod:mako_flowing"), ForgeRegistries.FLUIDS),
            attr)
            .bucket(RegistryObject.of(new ResourceLocation("makomod:mako_bucket"), ForgeRegistries.ITEMS))
            .block(RegistryObject.of(new ResourceLocation("makomod:mako"), ForgeRegistries.BLOCKS));

    @Override
    public Fluid getFlowingFluid() {
        return ForgeRegistries.FLUIDS.getValue(new ResourceLocation("makomod:mako_flowing"));
    }

    @Override
    public Fluid getStillFluid() {
        return ForgeRegistries.FLUIDS.getValue(new ResourceLocation("makomod:mako_still"));
    }

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

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public Item getFilledBucket() {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation("makomod:mako_bucket"));
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
        return state.getBlockState();
    }

    @Override
    public boolean isSource(IFluidState state) {
        return false;
    }

    @Override
    public int getLevel(IFluidState p_207192_1_) {
        return p_207192_1_.getBlockState().getFluidState().getLevel();
    }

}
