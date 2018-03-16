package simple.brainsynder.nms.key;

import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import simple.brainsynder.nms.IUpdateSign;

public class DefaultSignUpdater implements IUpdateSign {
    @Override
    public void changeLines(Location loc, Player player, String[] args) {
        Sign sign = (Sign) loc.getBlock().getState();
        sign.setLine(0, args[0]);
        sign.setLine(1, args[1]);
        sign.setLine(2, args[2]);
        sign.setLine(3, args[3]);
        sign.update();
    }
}
