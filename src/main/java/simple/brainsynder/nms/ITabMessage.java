package simple.brainsynder.nms;

import org.bukkit.entity.Player;

import java.util.Collection;

public interface ITabMessage {
    ITabMessage setHeader (String var1);

    ITabMessage setFooter (String var1);

    ITabMessage setHeaderFooter (String var1, String var2);

    void send(Player player);

    void send(Iterable<Player> players);

    void send(Collection<? extends Player> players);
}
