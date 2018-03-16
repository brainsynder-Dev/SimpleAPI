package simple.brainsynder.nms;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface IUpdateSign {
    void changeLines(Location loc, Player player, String[] args);
}
