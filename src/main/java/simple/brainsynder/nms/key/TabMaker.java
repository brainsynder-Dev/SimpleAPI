package simple.brainsynder.nms.key;

import org.bukkit.entity.Player;
import simple.brainsynder.nms.ITabMessage;

import java.util.Collection;

public class TabMaker implements ITabMessage {
    private String header = "";
    private String footer = "";

    public String getHeader () {
        return header;
    }

    public String getFooter() {
        return footer;
    }

    @Override
    public ITabMessage setHeader(String var1) {
        header = var1;
        return this;
    }

    @Override
    public ITabMessage setFooter(String var1) {
        footer = var1;
        return this;
    }

    @Override
    public ITabMessage setHeaderFooter(String var1, String var2) {
        header = var1;
        footer = var2;
        return this;
    }

    @Override
    public void send(Player player) {

    }

    @Override
    public void send(Iterable<Player> players) {
        for (Player player : players)
            send (player);
    }

    @Override
    public void send(Collection<? extends Player> players) {
        for (Player player : players)
            send (player);
    }
}
