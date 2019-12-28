package org.zleub.makomod.common.registry;

import net.minecraft.fluid.Fluid;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.common.Mod;
import org.zleub.makomod.common.fluids.FluidMako;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryFluid {
    @SubscribeEvent
    public static void registerFluids(RegistryEvent.Register<Fluid> event) {
        event.getRegistry().register(new ForgeFlowingFluid.Source(FluidMako.props).setRegistryName("makomod:mako_still"));
        event.getRegistry().register(new ForgeFlowingFluid.Flowing(FluidMako.props).setRegistryName("makomod:mako_flowing"));

//            event.getRegistry().addBucketForFluid(FluidRegistry.getFluid(TestFluid.name));

    }
}
