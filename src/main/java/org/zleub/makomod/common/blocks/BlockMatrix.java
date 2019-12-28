package org.zleub.makomod.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import org.zleub.makomod.MakoMod;
import org.zleub.makomod.common.tiles.TileMatrix;

import javax.annotation.Nullable;

import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

public class BlockMatrix extends Block {

    public BlockMatrix(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

//      @Override
//    public BlockRenderType getRenderType(BlockState state) {
//        return BlockRenderType.MODEL;
//    }


    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        Vec3d vec = hit.getHitVec();

        double x = vec.x - pos.getX();
        double z = vec.z - pos.getZ();
        if (x > 1 / 16. && x < 3 / 16. && z > 1 / 16. && z < 3 / 16.)
        {
            MakoMod.print("hit ?");
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileMatrix) {
                IItemHandler handler = ((TileMatrix)te).getCapability(ITEM_HANDLER_CAPABILITY).orElse(null);

                ResourceLocation myTagId = new ResourceLocation("forge", "gems");
                MakoMod.print( String.valueOf(ItemTags.getCollection().get(myTagId).contains(player.getHeldItem(hand).getItem())) );
            }
        }
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileMatrix(TileMatrix.MatrixType);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
}

