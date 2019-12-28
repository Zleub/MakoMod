package org.zleub.makomod;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.*;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.*;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zleub.makomod.common.blocks.BlockMatrix;
import org.zleub.makomod.common.blocks.BlockStoned;
import org.zleub.makomod.common.blocks.BlockStonedTank;
import org.zleub.makomod.common.fluids.FluidMako;
import org.zleub.makomod.common.tiles.TileMatrix;
import org.zleub.makomod.common.tiles.TilePillar;
import org.zleub.makomod.common.tiles.TileTank;

import java.util.*;
import java.util.stream.Collectors;

import static net.minecraft.item.Items.BUCKET;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("makomod")
public class MakoMod {
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();


    public static void print(String msg) {
        LOGGER.info(msg);

        MinecraftServer mcs = ServerLifecycleHooks.getCurrentServer();
        if (mcs != null)
            mcs.getPlayerList().sendMessage(new StringTextComponent(msg));
    }

    public MakoMod() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());

//         ModelLoaderRegistry.registerLoader(BlockStonedPillar.CustomModelLoader.INSTANCE);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);

        OBJLoader.INSTANCE.addDomain("makomod");
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("makomod", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m -> m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

//    static Map<String, Block> blocks = new HashMap<>();
//
//    static {
//        blocks.put("makomod:stoned", new BlockStoned(Block.Properties.create(Material.ROCK)));
//        blocks.put("makomod:stoned_tank", new BlockStonedTank(
//            Block.Properties.create(Material.ROCK)
//                    .lightValue(15)
//                .variableOpacity()
//        ));
//        blocks.put("makomod:mako", new FlowingFluidBlock(
//                RegistryObject.of(new ResourceLocation("makomod:mako_still"), ForgeRegistries.FLUIDS),
//                Block.Properties.create(Material.WATER)));
//        blocks.put("makomod:matrix", new BlockMatrix((Block.Properties.create(Material.ROCK))));
//    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void registerModel(ModelRegistryEvent event) {
            LOGGER.info("hello from model");
//            ModelLoaderRegistry.registerLoader(BlockStonedPillar.CustomModelLoader.INSTANCE);

        }


//        @SubscribeEvent
//        public static void onPreTextureStitch(TextureStitchEvent.Pre event) {
//            event.addSprite(
//              ResourceLocation.tryCreate("makomod:block/stone_test")
//            );
//        }
    }
}
