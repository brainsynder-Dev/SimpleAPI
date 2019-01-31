package simple.brainsynder.commands.list;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.scheduler.BukkitRunnable;
import simple.brainsynder.Core;
import simple.brainsynder.commands.BaseCommand;
import simple.brainsynder.files.Language;
import simple.brainsynder.nms.ITellraw;
import simple.brainsynder.utils.Perms;
import simple.brainsynder.utils.Reflection;
import simple.brainsynder.utils.SpigotPluginHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CommandSimpleAPI extends BaseCommand {

    public CommandSimpleAPI() {
        super("simpleapi");
    }

    @Override
    public void consoleSendCommandEvent(CommandSender sender, String[] args) {
        sender.sendMessage("§bSimpleAPI §9>> §7Looking for updates...");
        try {
            List<Boolean> updates = new ArrayList<>();
            CompletableFuture.runAsync(() -> {
                SpigotPluginHandler.updaterMap.forEach((plugin, handle) -> {
                    if (handle.needsUpdate()) {
                        String downloadURL;
                        if (handle.getId() != 0) {
                            downloadURL = "https://spigotmc.org/resources/" + handle.getId() + "/";
                        } else {
                            downloadURL = "https://spigotmc.org/";
                        }
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                sender.sendMessage("§bSimpleAPI §9>> §7An update was found for " + handle.getPlugin().getDescription().getName() + "!");
                                sender.sendMessage("§bSimpleAPI §9>> §7 Current Version: " + plugin.getDescription().getVersion());
                                sender.sendMessage("§bSimpleAPI §9>> §7 New Version: " + handle.getVersion());
                                sender.sendMessage("§bSimpleAPI §9>> §7 Download at: " + downloadURL);
                                updates.add(true);
                            }
                        }.runTask(Core.getInstance());
                    }
                });


            });
        } catch (Exception e) {
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
            SpigotPluginHandler.updaterMap.values().forEach(handler -> {
                PluginDescriptionFile pdf = handler.getPlugin().getDescription();
                ITellraw tellraw = Reflection.getTellraw("§9• §b" + pdf.getName() + " >> Hover for more info");
                tellraw.color(ChatColor.AQUA);
                String downloadURL = "http://spigotmc.org//";
                if (handler.getId() != 0) downloadURL = "https://spigotmc.org/resources/" + handler.getId() + "/";
                tellraw.link(downloadURL);

                CompletableFuture.runAsync(() -> {
                    handler.needsUpdate((needsUpdate, version) -> {
                        tellraw.tooltip(
                                "§7A new version of §b" + pdf.getName() + "§7 is Out!",
                                "§7Version §b" + version + "§7, current version running is version §b" + pdf.getVersion(),
                                "§7Update has §b" + handler.getDownloads() + "§7 download(s)",
                                "§7It would be wise to check this update out.",
                                "§7Click this text to go to the spigot page.");
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                updates.add(true);
                                tellraw.send(player);
                            }
                        }.runTask(Core.getInstance());
                    });
                });
            });
            if (updates.isEmpty()) {
                player.sendMessage("§bSimpleAPI §9>> §7All plugins using SimpleAPIs' updater are up to date");
            }
        } catch (Exception e) {
            player.sendMessage("§bSimpleAPI §9>> §7All plugins using SimpleAPIs' updater are up to date");
        }
    }
}
