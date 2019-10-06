package org.zleub.makomod;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftGame;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.model.ModelRotation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.resources.IResource;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.opengl.GL11;
import sun.reflect.Reflection;
import sun.rmi.runtime.Log;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.logging.Logger;

public class TileTank extends TileEntity {
    public static TileEntityType<?> TankType;

    protected FluidTank tank = new FluidTank(4);
    private final LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> tank);


    public TileTank(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
    }

    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);
        tank.readFromNBT(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        CompoundNBT nbt = super.write(tag);
        tank.writeToNBT(tag);
        return nbt;
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return holder.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = super.getUpdateTag();
//        nbt.putByte("level", this.level);
        MakoMod.LOGGER.info("getUpdateTag {}", nbt);
        return nbt;
    }

    @Override
    public void handleUpdateTag(CompoundNBT tag) {
        Logger.getGlobal().info("handleUpdateTag " + tag);
//        this.level = tag.getCompound("ForgeData").getByte("level");
//        this.write(tag);
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket(){
        //Write your data into the nbtTag
        CompoundNBT nbt = new CompoundNBT();
        nbt.putByte("level", this.getTileData().getByte("level"));
        MakoMod.LOGGER.info("getupdatepacket {}", nbt);
        return new SUpdateTileEntityPacket(getPos(), 1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt){
        CompoundNBT tag = pkt.getNbtCompound();
        //Handle your Data
        Logger.getGlobal().info("ondatapacket " + tag);
//        this.level = tag.getByte("level");
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

            renderer.setTranslation(x - (double)blockpos.getX(), y - (double)blockpos.getY(), z - (double)blockpos.getZ());

            if (te.tank.getFluid().getFluid() != Fluids.EMPTY) {

                BlockState blockState = ForgeRegistries.BLOCKS.getValue(te.tank.getFluid().getFluid().getRegistryName()).getDefaultState();
                IBakedModel iBakedModel = MakoMod.innerMap.get(new ResourceLocation("makomod:" + te.tank.getFluid().getFluid().getRegistryName().getPath()));

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
