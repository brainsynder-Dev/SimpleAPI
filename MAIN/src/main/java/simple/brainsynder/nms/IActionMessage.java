package simple.brainsynder.nms;

import org.bukkit.entity.Player;

import java.util.Collection;

public interface IActionMessage{
    void sendMessage (Player player, String message);

    void sendMessage(Collection<? extends Player> players, String message);
}
