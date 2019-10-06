package org.zleub.makomod;

import net.minecraft.block.Block;
import net.minecraft.util.BlockRenderLayer;

public class BlockStoned extends Block {
    public BlockStoned(Block.Properties props) {
        super(props);
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
      return BlockRenderLayer.CUTOUT;
   }

}