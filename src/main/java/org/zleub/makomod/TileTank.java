package org.zleub.makomod;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;
import sun.rmi.runtime.Log;



import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Predicate;
import java.util.logging.Logger;

import static org.zleub.makomod.BlockStonedPillar.LEVEL;

public class TileTank extends TileEntity implements ITickableTileEntity {
    public static TileEntityType<?> TankType;
    public static int capacity = 3000;

    protected FluidTank tank;
    private final LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> tank);
    private LazyOptional<IFluidHandler>[] neighbors;

    public TileTank(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
        tank = new FluidTank(capacity, this);
        neighbors = new LazyOptional[2];
    }

    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);
        tank.readFromNBT(tag);
        Logger.getGlobal().info("read " + tag);
        Logger.getGlobal().info(tank.getFluid().toString());
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        CompoundNBT nbt = super.write(tag);
        tank.writeToNBT(tag);
        Logger.getGlobal().info("write " + tag);
        return nbt;
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return holder.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = super.getUpdateTag();
        tank.writeToNBT(nbt);
        MakoMod.LOGGER.info("getUpdateTag {}", nbt);
        return nbt;
    }

    @Override
    public void handleUpdateTag(CompoundNBT nbt) {
        Logger.getGlobal().info("handleUpdateTag " + nbt);
        tank.readFromNBT(nbt);
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        //Write your data into the nbtTag
        CompoundNBT nbt = new CompoundNBT();
        tank.writeToNBT(nbt);
        MakoMod.LOGGER.info("getupdatepacket {}", nbt);
        return new SUpdateTileEntityPacket(getPos(), 1, nbt);
    }

    public void registerNeighbors(LazyOptional<IFluidHandler> te, Direction facing) {
        if (facing == Direction.UP) {
            this.neighbors[0] = te;
        }
        else if ( facing == Direction.DOWN) {
            this.neighbors[1] = te;
        }
    }

    public void updateTank() {
        if ( this.neighbors[1] != null && this.neighbors[1].isPresent() ) { // push down
            IFluidHandler handler = this.neighbors[1].orElse(null);
            FluidStack fs = FluidUtil.tryFluidTransfer(handler, this.tank, this.tank.getFluid(), false);
            if (fs.getAmount() > 0)
                FluidUtil.tryFluidTransfer(handler, this.tank, this.tank.getFluid(), true);
        }
        if ( this.neighbors[0] != null && this.neighbors[0].isPresent() ) { // pull up
            IFluidHandler handler = this.neighbors[0].orElse(null);
            FluidStack fs = FluidUtil.tryFluidTransfer(this.tank, handler, handler.getFluidInTank(1), false);
            if (fs.getAmount() > 0)
                FluidUtil.tryFluidTransfer(this.tank, handler, handler.getFluidInTank(1), true);
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT nbt = pkt.getNbtCompound();
        //Handle your Data
        Logger.getGlobal().info("ondatapacket " + nbt);
        tank.readFromNBT(nbt);
    }

    @Override
    public void tick() {
        updateTank();
    }

    public static class FluidTank extends net.minecraftforge.fluids.capability.templates.FluidTank {
        public TileTank te = null;

        public FluidTank(int capacity, TileTank te) {
            super(capacity);
            this.te = te;
        }

        public FluidTank(int capacity) { super(capacity); }

        public FluidTank(int capacity, Predicate<FluidStack> validator) {
            super(capacity, validator);
        }

        @Nonnull
        @Override
        public FluidStack drain(int maxDrain, FluidAction action)
        {
            int drained = maxDrain;
            if (fluid.getAmount() < drained)
            {
                drained = fluid.getAmount();
            }
            FluidStack stack = new FluidStack(fluid, drained);
            if (action.execute() && drained > 0)
            {
                fluid.shrink(drained);
                onContentsChanged();
            }
            return stack;
        }

        @Override
        protected void onContentsChanged() {
            if (te == null)
                return ;

            BlockPos pos = te.getPos();

            World world = te.getWorld();
            int f = (int)(((float)te.tank.getFluidAmount() / te.tank.getCapacity()) * 3);
            BlockState newstate = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("makomod:stoned_pillar")).getDefaultState().with(LEVEL, f);
            world.setBlockState(pos, newstate);
            world.notifyBlockUpdate(pos, newstate, newstate, 2);
        }
    }

    public static class TileTankRenderer extends TileEntityRenderer<TileTank> {
        @Override
        public void render(TileTank te, double x, double y, double z, float partialTick, int destroyStage) {
//            Logger.getGlobal().info("test render");

            Minecraft mc = Minecraft.getInstance();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder renderer = tessellator.getBuffer();


            BlockRendererDispatcher blockRenderer = Minecraft.getInstance().getBlockRendererDispatcher();
            BlockPos blockpos = te.getPos();


            this.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);

            RenderHelper.disableStandardItemLighting();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.enableBlend();
            GlStateManager.disableCull();
            if (Minecraft.isAmbientOcclusionEnabled()) {
                GlStateManager.shadeModel(7425);
            } else {
                GlStateManager.shadeModel(7424);
            }

            BlockModelRenderer.enableCache();

            renderer.begin(7, DefaultVertexFormats.BLOCK);

            renderer.setTranslation(x - (double) blockpos.getX(), y - (double) blockpos.getY(), z - (double) blockpos.getZ());

            if (te.tank.getFluid().getFluid() != Fluids.EMPTY) {

                BlockState blockState = ForgeRegistries.BLOCKS.getValue(te.tank.getFluid().getFluid().getRegistryName()).getDefaultState();
//                blockState.getBlock().delegate;
//                Logger.getGlobal().info(blockState.toString());
                String model_name = "makomod:" + te.tank.getFluid().getFluid().getRegistryName().getPath() + "level" + (te.tank.getFluidAmount() / FluidAttributes.BUCKET_VOLUME);
                IBakedModel iBakedModel = MakoMod.innerMap.get(new ResourceLocation(model_name));

                BlockPos blockPos = blockpos;
                BufferBuilder bufferBuilder = renderer;
                boolean b = false;
                Random random = new Random();
                long l = blockState.getPositionRandom(blockPos);
                IModelData modelData = ModelDataManager.getModelData(getWorld(), blockPos);
                blockRenderer.getBlockModelRenderer().renderModel(getWorld(), iBakedModel, blockState, blockPos, bufferBuilder, b, random, l, modelData);

            }
            renderer.setTranslation(0.0D, 0.0D, 0.0D);

            tessellator.draw();
            BlockModelRenderer.disableCache();
//                GlStateManager.disableBlend();
//            GlStateManager.popMatrix();

            RenderHelper.enableStandardItemLighting();
        }
    }
}
