package org.zleub.makomod.common.tiles;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.zleub.makomod.MakoMod;
import org.zleub.makomod.client.models.RegistryModels;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Random;

import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

public class TileMatrix extends TileEntity {
    public static TileEntityType<?> MatrixType;

    private ItemStackHandler inventory = new ItemStackHandler(13) {
        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

    };

    private final LazyOptional<ItemStackHandler> holder = LazyOptional.of(() -> inventory);


    public TileMatrix(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (capability == ITEM_HANDLER_CAPABILITY)
            return holder.cast();
        return super.getCapability(capability, facing);
    }

    public static class TileMatrixRenderer extends TileEntityRenderer<TileMatrix> {
        @Override
        public void render(TileMatrix te, double x, double y, double z, float partialTick, int destroyStage) {
            super.render(te, x, y, z, partialTick, destroyStage);

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

            BlockState blockState = te.getBlockState();
            IBakedModel iBakedModel = RegistryModels.innerMap.get(new ResourceLocation("makomod:matrix_stone1"));

            BlockPos blockPos = blockpos;
            BufferBuilder bufferBuilder = renderer;
            boolean b = false;
            Random random = new Random();
            long l = blockState.getPositionRandom(blockPos);
            IModelData modelData = ModelDataManager.getModelData(getWorld(), blockPos);
            blockRenderer.getBlockModelRenderer().renderModel(getWorld(), iBakedModel, blockState, blockPos, bufferBuilder, b, random, l, modelData);

            renderer.setTranslation(0.0D, 0.0D, 0.0D);

            tessellator.draw();
            BlockModelRenderer.disableCache();
//                GlStateManager.disableBlend();
//            GlStateManager.popMatrix();

            RenderHelper.enableStandardItemLighting();
        }
    }

}
