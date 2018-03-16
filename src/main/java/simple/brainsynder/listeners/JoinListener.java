package simple.brainsynder.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import simple.brainsynder.Core;
import simple.brainsynder.files.Language;
import simple.brainsynder.utils.Perms;
import simple.brainsynder.utils.SpigotPluginHandler;

public class JoinListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    private void latePlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (Core.getLanguage().getBoolean(Language.CHECK_UPDATES)) {
            if (Perms.UPDATE.has(player)) {
                new Thread(() -> player.performCommand("simpleapi")).run();
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
