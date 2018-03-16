package simple.brainsynder.nms.key;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import simple.brainsynder.events.ActionMessageEvent;
import simple.brainsynder.nms.IActionMessage;

import java.util.Collection;

public class ActionMaker implements IActionMessage {
    @Override
    public void sendMessage(Collection<? extends Player> players, String message) {
        for (Player player : players)
            sendMessage(player, message);
    }

    @Override
    public void sendMessage(Player player, String message) {
        ActionMessageEvent event = new ActionMessageEvent(player, message);
        Bukkit.getServer().getPluginManager().callEvent(event);
        event.getTarget().sendMessage (ChatColor.translateAlternateColorCodes('&', event.getText()));
    }
}
