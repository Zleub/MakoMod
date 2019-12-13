package org.zleub.makomod;

import net.minecraft.util.math.BlockPos;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
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

    public static List<BlockPos> getKnightPos(BlockPos pos) {
        ArrayList<BlockPos> l = new ArrayList();

        l.add( pos.add(2, 0, -1) );
        l.add( pos.add(2, 0, 1) );
        l.add( pos.add(-2, 0, -1) );
        l.add( pos.add(-2, 0, 1) );
        l.add( pos.add(-1, 0, 2) );
        l.add( pos.add(1, 0, 2) );
        l.add( pos.add(-1, 0, -2) );
        l.add( pos.add(1, 0, -2) );

        return l;
    }
}
