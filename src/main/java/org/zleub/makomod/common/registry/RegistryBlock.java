package org.zleub.makomod.common.registry;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.zleub.makomod.common.blocks.BlockMatrix;
import org.zleub.makomod.common.blocks.BlockStoned;
import org.zleub.makomod.common.blocks.BlockStonedTank;

import java.util.HashMap;
import java.util.Map;

import static net.minecraft.world.biome.Biome.LOGGER;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryBlock {
    static Map<String, Block> BLOCKS = new HashMap<>();

    static {
        BLOCKS.put("makomod:stoned", new BlockStoned(Block.Properties.create(Material.ROCK)));
        BLOCKS.put("makomod:stoned_tank", new BlockStonedTank(
            Block.Properties.create(Material.ROCK)
                    .lightValue(15)
                .variableOpacity()
        ));
        BLOCKS.put("makomod:mako", new FlowingFluidBlock(
                RegistryObject.of(new ResourceLocation("makomod:mako_still"), ForgeRegistries.FLUIDS),
                Block.Properties.create(Material.WATER)));
        BLOCKS.put("makomod:matrix", new BlockMatrix((Block.Properties.create(Material.ROCK))));
    }

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> blockRegistryEvent) {
        // register a new block here
        LOGGER.info("HELLO from Register Block");

        BLOCKS.forEach((k, v) -> blockRegistryEvent.getRegistry().register(v.setRegistryName(k)));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        BLOCKS.forEach((k, v) -> event.getRegistry().register(new BlockItem(v, new Item.Properties().group(ItemGroup.MISC)).setRegistryName(k)));
    }

}
