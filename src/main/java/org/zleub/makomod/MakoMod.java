package org.zleub.makomod;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.system.CallbackI;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import sun.reflect.Reflection;


// The value here should match an entry in the META-INF/mods.toml file
@Mod("makomod")
public class MakoMod
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static BlockModel inner;
//    public static BlockState inner;
    public static Map<ResourceLocation, IBakedModel> innerMap = new HashMap<ResourceLocation, IBakedModel>();

     public static String readFile(String path) {
            try {
                Path p = Paths.get(MakoMod.class.getResource("/assets/makomod/" + path).toURI());
                BufferedReader bufferedreader = Files.newBufferedReader(p);
                return bufferedreader.lines().collect(Collectors.joining());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

    static {

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

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());

//         ModelLoaderRegistry.registerLoader(BlockStonedPillar.CustomModelLoader.INSTANCE);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("makomod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    static Map<String, Block> blocks = new HashMap<>();;
    static {
        blocks.put("makomod:stoned", new BlockStoned(Block.Properties.create(Material.ROCK)));
        blocks.put("makomod:stoned_pillar", new BlockStonedPillar(Block.Properties.create(Material.ROCK).lightValue(15)));
//        blocks.put("makomod:stoned_pillar_inner", new Block(Block.Properties.create(Material.ROCK)));
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");

            blocks.forEach( (k, v) -> blockRegistryEvent.getRegistry().register(v.setRegistryName(k)) );
        }

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            blocks.forEach( (k, v) -> event.getRegistry().register(new BlockItem(v, new Item.Properties().group(ItemGroup.MISC)).setRegistryName(k)) );
        }

//        @SubscribeEvent
//        public static void registerModel(ModelRegistryEvent event) {
//            LOGGER.info("hello from model");
//            ModelLoaderRegistry.registerLoader(BlockStonedPillar.CustomModelLoader.INSTANCE);
//        }

      @SubscribeEvent
        public static void registerTileEntity(RegistryEvent.Register<TileEntityType<?>> event) {
          LOGGER.info("hello from tileentities");
//          LOGGER.info(Minecraft.getInstance().getTextureManager().getTexture(ResourceLocation.tryCreate("minecraft:stone")));
          TileEntityType<?> type = TileEntityType.Builder.create(() -> new TileTank(TileTank.TankType), blocks.get("makomod:stoned_pillar")).build(null);
          TileTank.TankType = type;
          type.setRegistryName("makomod", "tank");
          event.getRegistry().register(type);

          ClientRegistry.bindTileEntitySpecialRenderer(TileTank.class, new TileTank.TileTankRenderer());
        }

//        @SubscribeEvent
//        public static void registerModel(ModelRegistryEvent event) {
//            LOGGER.info("hello from model");
//            ModelLoaderRegistry.registerLoader(BlockStonedPillar.CustomModelLoader.INSTANCE);
//        }

        @SubscribeEvent
        public static void registerBakedModels(ModelBakeEvent e) {
//            LOGGER.info("hello from model");
//            LOGGER.info(e.getModelRegistry().entrySet().stream().filter( entry -> entry.getKey().getNamespace().equals("makomod")).collect(Collectors.toList()));
////            e.getModelRegistry().replace("makomod:stoned_pillar#level=0", )
//            IBakedModel model = e.getModelRegistry().get("makomod:stoned_pillar#level=1");
////            LOGGER.info(model.);
////            IBakedModel test = new BlockStonedPillar.ModelStonePillar(), )
//            BlockModel pillar = BlockModel.deserialize(readFile("models/block/stoned_pillar.json"));
//            inner = BlockModel.deserialize(readFile("models/block/stoned_pillar_inner.json"));

////            pillar.getElements().add(inner.getElements().get(0));
//
//            TankModel dm = new TankModel(model);
////            FaceBakery.
////            e.getModelLoader()
//
//
//            e.getModelRegistry().replace( new ModelResourceLocation("makomod:stoned_pillar#level=1"), dm);
            Minecraft mc = Minecraft.getInstance();
            ModelLoader modelbakery = new ModelLoader(mc.getResourceManager(), mc.getTextureMap(), mc.getBlockColors(), mc.getProfiler());

            Set<Map.Entry<ResourceLocation, Fluid>> fluidsEntries = ForgeRegistries.FLUIDS.getEntries();
            fluidsEntries.forEach( entry -> {
                LOGGER.info(entry);
                ResourceLocation rl = entry.getKey();

                Fluid fluid = entry.getValue();
                ResourceLocation fluidTex;
                if (rl.toString().matches(".*flowing.*"))
                    fluidTex = fluid.getAttributes().getFlowingTexture();
                else
                    fluidTex = fluid.getAttributes().getStillTexture();

                inner = BlockModel.deserialize(MakoMod.readFile("models/block/stoned_pillar_inner.json"));
                inner.textures.replace("0", fluidTex == null ? "minecraft:block/missingno" : fluidTex.toString());

                IBakedModel baked = inner.bake(modelbakery, mc.getTextureMap()::getSprite, ModelRotation.X0_Y0, net.minecraft.client.renderer.vertex.DefaultVertexFormats.BLOCK );
                innerMap.put(new ResourceLocation("makomod", rl.getPath()), baked);
            } );
            LOGGER.info(innerMap);
        }
    }
}
