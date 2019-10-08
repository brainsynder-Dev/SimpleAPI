package simple.brainsynder.nms.utilities;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import simple.brainsynder.utils.ReturnValue;

import java.util.Map;

public class JCommandListener implements Listener {
    private JCommand command;

    public JCommandListener(JCommand command){
        this.command = command;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onForcedJSONCommand(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage();
        String cmd = command;
        for (Map.Entry<String, ReturnValue<Player>> entry : this.command.getCommands().entrySet()) {
            String name = entry.getKey();
            if(name.equalsIgnoreCase(command)) {
                entry.getValue().run(event.getPlayer());
                event.setCancelled(true);
                cmd = name;
                break;
            }
        }
        this.command.removeCommand(cmd);
    }
}