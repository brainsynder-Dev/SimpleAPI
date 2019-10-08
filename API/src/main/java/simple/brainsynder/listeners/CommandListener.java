package simple.brainsynder.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import simple.brainsynder.Core;
import simple.brainsynder.files.plugin_use.Language;
import simple.brainsynder.utils.plugin_use.Logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CommandListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void commandBlock(PlayerCommandPreprocessEvent e) {
        final Player p = e.getPlayer();
        if (!Core.getLanguage().getBoolean(Language.COMMAND_LOG)) {
            return;
        }
        String[] args = e.getMessage().split(" ");
        String command = args[0];
        if (command.startsWith("/")) {
            command = command.replaceFirst("/", "");
            if (Core.getLanguage().getStringList(Language.COMMAND_LOG).contains(command)) {
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                Calendar calendar = Calendar.getInstance();
                Logger.log("CommandLogs", "[" + format.format(calendar.getTime()) + "] " + p.getName() + " did the command { " + e.getMessage() + " }");
            }
        }
    }
}
