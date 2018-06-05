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
import simple.brainsynder.utils.SpigotPluginHandler;

public class Core extends JavaPlugin {
    private static Language language;
    private static Core instance;
    private LagCheck lagCheck;
    
    public static Core getInstance() {
        return instance;
    }
    
    @Override
    public void onEnable() {
        SpigotPluginHandler spigotPluginHandler = new SpigotPluginHandler(this, 24671, SpigotPluginHandler.MetricType.BSTATS);
        SpigotPluginHandler.registerPlugin(spigotPluginHandler);
        if (!spigotPluginHandler.runTamperCheck("brainsynder", "SimpleAPI", "3.8-SNAPSHOT")) {
            setEnabled(false);
            return;
        }

        try {
            CommandRegistry registry = new CommandRegistry (this);
            registry.register(new CommandParticle ());
        } catch (Exception e) {
            e.printStackTrace();
        }
        instance = this;
        language = new Language(this);
        language.loadDefaults();
        Reflection.init();
        new CommandSimpleAPI().registerCommand(this);
        if (!language.getBoolean(Language.API)) {
            new CommandAction().registerCommand(this);
            new CommandTab().registerCommand(this);
            new CommandTitle().registerCommand(this);
            new CommandHistory().registerCommand(this);
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
}
