package org.zleub.makomod.common.registry;

import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import static net.minecraft.item.Items.BUCKET;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryItem {
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new BucketItem(
                RegistryObject.of(new ResourceLocation("makomod:mako_still"), ForgeRegistries.FLUIDS),
                (new Item.Properties()).containerItem(BUCKET).maxStackSize(1).group(ItemGroup.MISC)).setRegistryName("makomod:mako_bucket")
        );
    }
}
