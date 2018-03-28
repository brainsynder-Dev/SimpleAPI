package simple.brainsynder.commands.list;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import simple.brainsynder.Core;
import simple.brainsynder.commands.BaseCommand;
import simple.brainsynder.files.Language;
import simple.brainsynder.nms.ITellraw;
import simple.brainsynder.utils.Perms;
import simple.brainsynder.utils.Reflection;
import simple.brainsynder.utils.SpigotPluginHandler;

import java.util.ArrayList;
import java.util.List;

public class CommandSimpleAPI extends BaseCommand {

    public CommandSimpleAPI() {
        super("simpleapi");
    }

    @Override
    public void consoleSendCommandEvent(CommandSender sender, String[] args) {
        sender.sendMessage("§bSimpleAPI §9>> §7Looking for updates...");
        try {
            List<Boolean> updates = new ArrayList<>();
            SpigotPluginHandler.updaterMap.forEach((plugin, handle) -> {
                if (handle.needsUpdate()) {
                    String downloadURL;
                    if (handle.getId() != 0) {
                        downloadURL = "https://spigotmc.org/resources/" + handle.getId() + "/";
                    } else {
                        downloadURL = "http://spigotmc.org/members/brainsynder.35575/";
                    }
                    sender.sendMessage("§bSimpleAPI §9>> §7An update was found for " + handle.getPlugin().getDescription().getName() + "!");
                    sender.sendMessage("§bSimpleAPI §9>> §7 Current Version: " + plugin.getDescription().getVersion());
                    sender.sendMessage("§bSimpleAPI §9>> §7 New Version: " + handle.getVersion());
                    sender.sendMessage("§bSimpleAPI §9>> §7 Download at: " + downloadURL);
                    updates.add(true);
                }
            });
            if (updates.isEmpty()) {
                sender.sendMessage("§bSimpleAPI §9>> §7All plugins using SimpleAPIs' updater are up to date");
            }
        }catch (Exception e) {
            sender.sendMessage("§bSimpleAPI §9>> §7All plugins using SimpleAPIs' updater are up to date");
        }
    }

    @Override
    public void playerSendCommandEvent(Player player, String[] args) {
        if (!Perms.UPDATE.has(player)) {
            player.sendMessage(Core.getLanguage().getString(Language.NOPERMISSION, true));
            return;
        }
        player.sendMessage("§bSimpleAPI §9>> §7Looking for updates...");
        try {
            List<Boolean> updates = new ArrayList<>();
            SpigotPluginHandler.updaterMap.forEach((plugin, handle) -> {
                PluginDescriptionFile pdf = handle.getPlugin().getDescription();
                ITellraw tellraw = Reflection.getTellraw("§9• §b" + pdf.getName() + " >> Hover for more info");
                tellraw.color(ChatColor.AQUA);
                if (handle.needsUpdate()) {
                    tellraw.tooltip(
                            "§7A new version of §b" + handle.getPlugin().getDescription().getName() + "§7 is Out!",
                            "§7Version §b" + handle.getVersion() + "§7, current version running is version §b" + handle.getPlugin().getDescription().getVersion(),
                            "§7Update has §b" + handle.getDownloads() + "§7 download(s)",
                            "§7It would be wise to check this update out.",
                            "§7Click this text to go to the spigot page.");
                    String downloadURL;
                    if (handle.getId() != 0) {
                        downloadURL = "https://spigotmc.org/resources/" + handle.getId() + "/";
                    } else {
                        downloadURL = "http://spigotmc.org/members/brainsynder.35575/";
                    }
                    tellraw.link(downloadURL);
                    tellraw.send(player);
                    updates.add(true);
                }
            });
            if (updates.isEmpty()) {
                player.sendMessage("§bSimpleAPI §9>> §7All plugins using SimpleAPIs' updater are up to date");
            }
        }catch (Exception e) {
            player.sendMessage("§bSimpleAPI §9>> §7All plugins using SimpleAPIs' updater are up to date");
        }
    }
}
