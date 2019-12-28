package org.zleub.makomod.client.models;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelRotation;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.zleub.makomod.Utils;
import org.zleub.makomod.common.blocks.BlockStonedTank;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static net.minecraft.world.biome.Biome.LOGGER;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RegistryModels {
    public static Map<ResourceLocation, IBakedModel> innerMap = new HashMap<ResourceLocation, IBakedModel>();

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
                LOGGER.info(((i + 1) * ratio));
                String json = Utils.readFile("models/block/template/stoned_tank_inner.json")
                        .replace("((height))", String.valueOf(((i + 1) * ratio)))
                        .replace("((r_height))", String.valueOf(16 - ((i + 1) * ratio)));
                BlockModel inner = BlockModel.deserialize(json);
                inner.textures.replace("fluid", fluidTex == null ? "minecraft:block/missingno" : fluidTex.toString());

                IBakedModel baked = inner.bake(modelbakery, mc.getTextureMap()::getSprite, ModelRotation.X0_Y0, net.minecraft.client.renderer.vertex.DefaultVertexFormats.BLOCK);
                ResourceLocation location = new ResourceLocation("makomod", "stoned_tank_inner_" + rl.getPath() + "_level" + i);
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
}
