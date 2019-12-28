package org.zleub.makomod.common.registry;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.zleub.makomod.common.tiles.TileMatrix;
import org.zleub.makomod.common.tiles.TilePillar;
import org.zleub.makomod.common.tiles.TileTank;

import static net.minecraft.world.biome.Biome.LOGGER;

public class RegistryTile {
    @SubscribeEvent
    public static void registerTileEntity(RegistryEvent.Register<TileEntityType<?>> event) {
        LOGGER.info("hello from tileentities");

        TileEntityType<?> type = TileEntityType.Builder.create(() -> new TileTank(TileTank.TankType), RegistryBlock.BLOCKS.get("makomod:stoned_tank")).build(null);
        TileTank.TankType = type;
        type.setRegistryName("makomod", "tank");
        event.getRegistry().register(type);
        ClientRegistry.bindTileEntitySpecialRenderer(TileTank.class, new TileTank.TileTankRenderer());

        type = TileEntityType.Builder.create(() -> new TilePillar(TilePillar.PillarType), RegistryBlock.BLOCKS.get("makomod:stoned")).build(null);
        TilePillar.PillarType = type;
        type.setRegistryName("makomod", "pillar");
        event.getRegistry().register(type);


        type = TileEntityType.Builder.create(() -> new TileMatrix(TileMatrix.MatrixType), RegistryBlock.BLOCKS.get("makomod:matrix")).build(null);
        TileMatrix.MatrixType = type;
        type.setRegistryName("makomod", "matrix");
        event.getRegistry().register(type);
        ClientRegistry.bindTileEntitySpecialRenderer(TileMatrix.class, new TileMatrix.TileMatrixRenderer());
    }
}
