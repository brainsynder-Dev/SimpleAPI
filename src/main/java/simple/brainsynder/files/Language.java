package simple.brainsynder.files;

import simple.brainsynder.Core;

import java.util.Arrays;

public class Language extends FileMaker{
    private Core _core;
    public static String NOPERMISSION = "No-Permission";
    public static String NOT_SUPPORTED = "Version-Not-Supported";
    public static String MISSINGPLAYER = "Player-Not-Found";
    public static String LL_NAME = "Light-Level-Item-Name";
    public static String LL_LORE = "Light-Level-Item-Lore";
    public static String TF_NAME = "Texture-Finder-Item-Name";
    public static String TF_LORE = "Texture-Finder-Item-Lore";
    public static String LOG_COMMANDS = "Log-Plugin-Commands";
    public static String NO_TEAM = "Scoreboard-Team-Unknown";
    public static String CHECK_UPDATES = "Check-For-Updates-On-Join";
    public static String API = "API-Only";
    public static String NO_RECIPES = "NO-RECIPES";
    public static String NO_PARTICLE = "Particle-Not-Found";
    public static String INVALID_NUMBER = "Invalid-Number";
    public static String MISSING_OFFSETS = "Missing-Offset-Values";
    public static String COMMAND_LOG = "Commands-To-Log.list";
    public static String COMMAND_LOG_ENABLED = "Commands-To-Log.Enabled";

    public Language (Core core) {
        super(core, "Language.yml");
        _core = core;
    }

    public void loadDefaults () {
        set(true, NOPERMISSION, "&cYou do not have permission.");
        set(true, NOT_SUPPORTED, "&cThis servers version is not supported.");
        set(true, MISSINGPLAYER, "&cPlayer is not found");
        set(true, LL_NAME, "&eLight Level Detector");
        set(true, LL_LORE, Arrays.asList(
                "Right click the top of",
                "a block to get the light",
                "level and see if mobs will spawn."
        ));
        set(true, TF_NAME, "&eSkull Texture Getter");
        set(true, TF_LORE, Arrays.asList(
                "Get the link of where",
                "the texture fpr the skulls",
                "skin is located, for you to download"
        ));
        set(true, COMMAND_LOG_ENABLED, true);
        set(true, CHECK_UPDATES, true, "Do you want the plugin to check for updates when", "a user logs onto the server (If they have permission)");
        set(true, API, false, "Do you want  the plugin to only be an API", "for the linked plugins? No new commands or Recipes and such");
        set(true, NO_RECIPES, false, "Do you want the Custom Recipes to be disabled?");
        set(true, COMMAND_LOG, Arrays.asList(
                "say",
                "ban"
        ));
        set(true, LOG_COMMANDS, true, "Do you want a log file to be generated for the action, titlemaker, or tab commands");
        set(true, NO_TEAM, "&cThe scoreboard team %team% was not found.");
        set(true, NO_PARTICLE, "&cParticle not found.");
        set(true, INVALID_NUMBER, "&cInvalid number format error");
        set(true, MISSING_OFFSETS, "&cMust have 3 offset values.");
    }
}
