package org.zleub.makomod;

import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.spi.LoggerRegistry;
import net.minecraft.util.ResourceLocation;

import java.util.logging.Logger;

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static net.minecraftforge.fluids.capability.CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;

public class TilePillar extends TileEntity implements ITickableTileEntity {
    public static TileEntityType<?> PillarType;

    public TilePillar(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
    }

    @Override
    public void tick() {
        BlockPos i = new BlockPos(pos.up());
        FluidStack fs = new FluidStack(ForgeRegistries.FLUIDS.getValue(new ResourceLocation("makomod:mako_still")), 250);
        while ((world.getTileEntity(i) != null)
                && world.getTileEntity(i).getCapability(FLUID_HANDLER_CAPABILITY).isPresent()
                && world.getTileEntity(i).getCapability(FLUID_HANDLER_CAPABILITY).orElse(null).fill(fs, IFluidHandler.FluidAction.SIMULATE) == 0)
            i = i.up();

        BlockPos top = new BlockPos(pos.up());
        while (!(world.getBlockState(top).getBlock() instanceof BlockStoned))
            top = top.up();

        BlockPos diff = top.subtract(pos);
        int height = diff.getY();

        boolean extract = false;
        ResourceLocation dirt_tag = new ResourceLocation("minecraft", "dirt_like");

        for (int j = 1; j < height + 1; j++) {
            for (int x = -(height - j); x <= (height - j); x++) {
                for (int y = -(height - j); y <= (height - j); y++) {
                    BlockPos cur = pos.subtract(new BlockPos(new Vec3i(x, j, y)));
                    if (cos(x) * cos(y) < 0.3 && BlockTags.getCollection().get(dirt_tag).contains(world.getBlockState(cur).getBlock())) {
                        extract = true;
                        world.setBlockState(cur, Blocks.AIR.getDefaultState(), 1);
                        break ;
                    }
                    if (extract == true)
                        break ;
                }
            }
            if (extract == true)
                break ;

            BlockPos cur = pos.subtract(new BlockPos(new Vec3i(0, j, 0)));
            if (BlockTags.getCollection().get(dirt_tag).contains(world.getBlockState(cur).getBlock())) {
                extract = true;
                world.setBlockState(cur, Blocks.AIR.getDefaultState(), 1);
                break ;
            }
        }

        if (extract && (world.getTileEntity(i) != null)
                && world.getTileEntity(i).getCapability(FLUID_HANDLER_CAPABILITY).isPresent()
                && world.getTileEntity(i).getCapability(FLUID_HANDLER_CAPABILITY).orElse(null).fill(fs, IFluidHandler.FluidAction.SIMULATE) != 0)
            world.getTileEntity(i).getCapability(FLUID_HANDLER_CAPABILITY).orElse(null).fill(fs, IFluidHandler.FluidAction.EXECUTE);
    }
}
