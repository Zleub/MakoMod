package org.zleub.makomod;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
}
