package org.zleub.makomod;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.*;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.model.TRSRTransformation;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static net.minecraft.item.Items.BUCKET;


// The value here should match an entry in the META-INF/mods.toml file
@Mod("makomod")
public class MakoMod {
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static Map<ResourceLocation, IBakedModel> innerMap = new HashMap<ResourceLocation, IBakedModel>();

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

    static Map<String, Block> blocks = new HashMap<>();

    static {
        blocks.put("makomod:stoned", new BlockStoned(Block.Properties.create(Material.ROCK)));
        blocks.put("makomod:stoned_tank", new BlockStonedTank(
            Block.Properties.create(Material.ROCK)
                    .lightValue(15)
                .variableOpacity()
        ));
        blocks.put("makomod:mako", new FlowingFluidBlock(
                RegistryObject.of(new ResourceLocation("makomod:mako_still"), ForgeRegistries.FLUIDS),
                Block.Properties.create(Material.WATER)));
        blocks.put("makomod:matrix", new BlockMatrix((Block.Properties.create(Material.ROCK))));
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");

            blocks.forEach((k, v) -> blockRegistryEvent.getRegistry().register(v.setRegistryName(k)));
        }

//        public static final FlowingFluid WATER = (FlowingFluid)register("water", new Source());

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            blocks.forEach((k, v) -> event.getRegistry().register(new BlockItem(v, new Item.Properties().group(ItemGroup.MISC)).setRegistryName(k)));
            event.getRegistry().register(new BucketItem(
                    RegistryObject.of(new ResourceLocation("makomod:mako_still"), ForgeRegistries.FLUIDS),
                    (new Item.Properties()).containerItem(BUCKET).maxStackSize(1).group(ItemGroup.MISC)).setRegistryName("makomod:mako_bucket")
            );
//           event.getRegistry().addBucketForFluid(FluidRegistry.getFluid(TestFluid.name));

        }

        @SubscribeEvent
        public static void registerFluids(RegistryEvent.Register<Fluid> event) {
            event.getRegistry().register(new ForgeFlowingFluid.Source(FluidMako.props).setRegistryName("makomod:mako_still"));
            event.getRegistry().register(new ForgeFlowingFluid.Flowing(FluidMako.props).setRegistryName("makomod:mako_flowing"));

//            event.getRegistry().addBucketForFluid(FluidRegistry.getFluid(TestFluid.name));

        }

        @SubscribeEvent
        public static void registerTileEntity(RegistryEvent.Register<TileEntityType<?>> event) {
            LOGGER.info("hello from tileentities");

            TileEntityType<?> type = TileEntityType.Builder.create(() -> new TileTank(TileTank.TankType), blocks.get("makomod:stoned_tank")).build(null);
            TileTank.TankType = type;
            type.setRegistryName("makomod", "tank");
            event.getRegistry().register(type);
            ClientRegistry.bindTileEntitySpecialRenderer(TileTank.class, new TileTank.TileTankRenderer());

            type = TileEntityType.Builder.create(() -> new TilePillar(TilePillar.PillarType), blocks.get("makomod:stoned")).build(null);
            TilePillar.PillarType = type;
            type.setRegistryName("makomod", "pillar");
            event.getRegistry().register(type);


            type = TileEntityType.Builder.create(() -> new TileMatrix(TileMatrix.MatrixType), blocks.get("makomod:matrix")).build(null);
            TileMatrix.MatrixType = type;
            type.setRegistryName("makomod", "matrix");
            event.getRegistry().register(type);
            ClientRegistry.bindTileEntitySpecialRenderer(TileMatrix.class, new TileMatrix.TileMatrixRenderer());


        }

        @SubscribeEvent
        public static void registerModel(ModelRegistryEvent event) {
            LOGGER.info("hello from model");
//            ModelLoaderRegistry.registerLoader(BlockStonedPillar.CustomModelLoader.INSTANCE);
        }

        @SubscribeEvent
        public static void registerBakedModels(ModelBakeEvent e) {
            LOGGER.info("hello from model");

            Minecraft mc = Minecraft.getInstance();
            ModelLoader modelbakery = new ModelLoader(mc.getResourceManager(), mc.getTextureMap(), mc.getBlockColors(), mc.getProfiler());

            Set<Map.Entry<ResourceLocation, Fluid>> fluidsEntries = ForgeRegistries.FLUIDS.getEntries();

            fluidsEntries.forEach(entry -> {
                ResourceLocation rl = entry.getKey();

                Fluid fluid = entry.getValue();
                ResourceLocation fluidTex;
                if (rl.toString().matches(".*flowing.*"))
                    fluidTex = fluid.getAttributes().getFlowingTexture();
                else
                    fluidTex = fluid.getAttributes().getStillTexture();

                // size of block in blockstate format / nbr of models
                int size = BlockStonedTank.LEVEL.getAllowedValues().size();
                float ratio = 16 / size;
                BlockStonedTank.LEVEL.getAllowedValues().forEach(i -> {
                    String json = Utils.readFile("models/block/_stoned_tank_inner.json")
                            .replace("[[height]]", String.valueOf(((i + 1) * ratio)))
                            .replace("[[r_height]]", String.valueOf(16 - ((i + 1) * ratio)));
                    BlockModel inner = BlockModel.deserialize(json);
                    inner.textures.replace("0", fluidTex == null ? "minecraft:block/missingno" : fluidTex.toString());

                    IBakedModel baked = inner.bake(modelbakery, mc.getTextureMap()::getSprite, ModelRotation.X0_Y0, net.minecraft.client.renderer.vertex.DefaultVertexFormats.BLOCK);
                    ResourceLocation location = new ResourceLocation("makomod", "_stoned_tank_inner_" + rl.getPath() + "_level" + i);
                    LOGGER.info("Model created: " + location);
                    innerMap.put(location, baked);
                });

            });

            String json = Utils.readFile("models/block/matrix_stone1.json");
            BlockModel stone = BlockModel.deserialize(json);

            IBakedModel baked = stone.bake(modelbakery, mc.getTextureMap()::getSprite, ModelRotation.X0_Y0, net.minecraft.client.renderer.vertex.DefaultVertexFormats.BLOCK);
            ResourceLocation location = new ResourceLocation("makomod", "matrix_stone1");
            LOGGER.info("Model created: " + location);
            innerMap.put(location, baked);

//            try {
//            IUnbakedModel model = ModelLoaderRegistry.getModelOrLogError(new ResourceLocation("makomod:block/matrix.obj"),
//                    "Missing water wheel mod﻿el");
//
//            if (model instanceof OBJModel) {
//               IBakedModel bakedModel = model.bake(e.getModelLoader(), ModelLoader.defaultTextureGetter(), new BasicState(model.getDefaultState(), false), DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
//                e.getModelRegistry().put(new ModelResourceLocation("makomod:matrix", ""), bakedModel);
//            }
//
//            } catch (Exception ec) {
//                ec.printStackTrace();
//            }

        }

//        @SubscribeEvent
//        public static void onPreTextureStitch(TextureStitchEvent.Pre event) {
//            event.addSprite(
//              ResourceLocation.tryCreate("makomod:block/stone_test")
//            );
//        }
    }
}
