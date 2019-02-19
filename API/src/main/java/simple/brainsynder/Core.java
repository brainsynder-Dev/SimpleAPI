package simple.brainsynder;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import simple.brainsynder.api.ItemMaker;
import simple.brainsynder.commands.CommandRegistry;
import simple.brainsynder.commands.list.*;
import simple.brainsynder.files.Language;
import simple.brainsynder.listeners.CommandListener;
import simple.brainsynder.listeners.JoinListener;
import simple.brainsynder.listeners.LightDetector;
import simple.brainsynder.listeners.TexturefindListener;
import simple.brainsynder.utils.LagCheck;
import simple.brainsynder.utils.Reflection;
import simple.brainsynder.utils.ServerVersion;
import simple.brainsynder.utils.SpigotPluginHandler;

import java.util.ArrayList;
import java.util.List;

public class Core extends JavaPlugin {
    private static Language language;
    private final List<String> supportedVersions = new ArrayList<>();
    private static Core instance;
    private LagCheck lagCheck;
    
    public static Core getInstance() {
        return instance;
    }
    
    @Override
    public void onEnable() {
        SpigotPluginHandler spigotPluginHandler = new SpigotPluginHandler(this, 24671, SpigotPluginHandler.MetricType.BSTATS);
        SpigotPluginHandler.registerPlugin(spigotPluginHandler);

        instance = this;

        fetchSupportedVersions();

        if (supportedVersions.isEmpty()) {

            return;
        }
        try {
            CommandRegistry<Core> registry = new CommandRegistry (this);
            registry.register(new CommandHistory());
            registry.register(new CommandParticle());
            registry.register(new CommandAction());
        } catch (Exception e) {
            e.printStackTrace();
        }
        language = new Language(this);
        language.loadDefaults();
        Reflection.init();
        new CommandSimpleAPI().registerCommand(this);
        if (!language.getBoolean(Language.API)) {
            new CommandTab().registerCommand(this);
            new CommandTitle().registerCommand(this);
            getServer().getPluginManager().registerEvents(new LightDetector(), this);
            getServer().getPluginManager().registerEvents(new TexturefindListener(), this);
            getServer().getPluginManager().registerEvents(new CommandListener(), this);
        }
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        lagCheck = new LagCheck();
        lagCheck.runTaskTimerAsynchronously(this, 0, 1);
    }
    
    private boolean isValid(String clazz) {
        Class clss = null;
        try {
            clss = Class.forName(clazz);
        } catch (ClassNotFoundException ignored) {
        }
        return clss != null;
    }
    
    public LagCheck getLagCheck() {
        return lagCheck;
    }
    
    public ItemMaker getLLDetector() {
        ItemMaker maker = new ItemMaker(Material.BONE);
        maker.enchant();
        maker.setName(language.getString(Language.LL_NAME, true));
        if (language.getStringList(Language.LL_LORE) != null)
            for (String s : language.getStringList(Language.LL_LORE))
                maker.addLoreLine(s);
        maker.setAmount(1);
        return maker;
    }
    
    public ItemMaker getTextureFinder() {
        ItemMaker maker = new ItemMaker(Material.BONE);
        maker.setName(language.getString(Language.TF_NAME, true));
        if (language.getStringList(Language.TF_LORE) != null)
            for (String s : language.getStringList(Language.TF_LORE))
                maker.addLoreLine(s);
        maker.setAmount(1);
        return maker;
    }
    
    public static Language getLanguage() {
        return language;
    }

    public ClassLoader getLoader () {
        return getClassLoader();
    }

    private void fetchSupportedVersions () {
        supportedVersions.clear();
        String current = Reflection.getVersion();
        boolean supported = false;
        String packageName = "simple.brainsynder.nms.<VER>.ActionMessage";
        for (ServerVersion version : ServerVersion.values()) {
            if (version.name().equals(current) && (!supported)) supported = true;
            try {
                Class<?> clazz = Class.forName(packageName.replace("<VER>", version.name()), false, getClassLoader());
                if (clazz != null) supportedVersions.add(version.name());
            }catch (Exception ignored){}
        }
        if (!supported) {
            try {
                Class<?> clazz = Class.forName(packageName.replace("<VER>", current), false, getClassLoader());
                if (clazz != null) supportedVersions.add(current);
            }catch (Exception ignored){}
        }

        if (!supportedVersions.isEmpty())
            getLogger().info("Found support for version(s): " + supportedVersions.toString());
    }

}
