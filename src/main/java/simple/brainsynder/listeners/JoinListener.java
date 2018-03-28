package simple.brainsynder.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import simple.brainsynder.Core;
import simple.brainsynder.files.Language;
import simple.brainsynder.nms.ITellraw;
import simple.brainsynder.utils.Perms;
import simple.brainsynder.utils.Reflection;
import simple.brainsynder.utils.SpigotPluginHandler;

public class JoinListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    private void latePlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (Core.getLanguage().getBoolean(Language.CHECK_UPDATES)) {
            if (Perms.UPDATE.has(player)) {
                SpigotPluginHandler.updaterMap.forEach((plugin, handle) -> {
                    PluginDescriptionFile pdf = handle.getPlugin().getDescription();
                    ITellraw tellraw = Reflection.getTellraw("§9• §b" + pdf.getName() + " >> Hover for more info");
                    tellraw.color(ChatColor.AQUA);
                    if (handle.needsUpdate()) {
                        tellraw.tooltip(
                                "§7A new version of §b" + pdf.getName() + "§7 is Out!",
                                "§7Version §b" + handle.getVersion() + "§7, current version running is version §b" + pdf.getVersion(),
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
                    }

                });
            }
        }
    }

    @EventHandler
    private void onBreak(BlockBreakEvent e) {
        if (e.getBlock().hasMetadata("NoBlockBreak")) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    private void onDisable (PluginDisableEvent event){
        Plugin plugin = event.getPlugin();
        if (SpigotPluginHandler.updaterMap.containsKey(plugin)) {
            SpigotPluginHandler.updaterMap.remove(plugin);
            SpigotPluginHandler.verionMap.remove(plugin);
            SpigotPluginHandler.IdMap.remove(plugin);
            SpigotPluginHandler.authorMap.remove(plugin);
            SpigotPluginHandler.pluginNameMap.remove(plugin);
        }
    }
}
