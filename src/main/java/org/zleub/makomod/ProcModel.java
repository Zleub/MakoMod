package org.zleub.makomod;

import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

// org.zleub.makomod.common generated variables name
// r_height (stand for reverse height, generated alongside height as 16 - height)
// filename

public class ProcModel {
    interface ModelProperties {
        Collection<Integer> height();
        Collection<String> fluid();
    }

    public Map<ResourceLocation, String> generated = new HashMap<>();

    public ProcModel(String filename, ModelProperties props) {
        String json = Utils.readFile(filename);

        Logger.getGlobal().info(filename);
        Logger.getGlobal().info(props.height().toString());
        Logger.getGlobal().info(props.fluid().toString());

        System.exit(1);
    }

    public void register(ModelBakeEvent e) {

    }
}
