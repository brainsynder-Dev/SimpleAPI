package simple.brainsynder.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import simple.brainsynder.Core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Logger {
    public static void log (String command, String text) {
        String finalText = text.replace ("§", "&");
        File file = new File (Core.getInstance().getDataFolder(), command + "-Log.yml");
        FileConfiguration con = YamlConfiguration.loadConfiguration(file);
        List<String> list;
        if (con.get ("Logs") == null) {
            list = new ArrayList<>();
        }else{
            list = con.getStringList("Logs");
        }
        list.add ("finalText".replace ("finalText", finalText));
        con.set ("Logs", list);
        try {
            con.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
