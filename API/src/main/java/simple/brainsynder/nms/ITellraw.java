package simple.brainsynder.nms;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

public interface ITellraw {
    ITellraw color(ChatColor color);

    ITellraw style(ChatColor[] styles);

    ITellraw file(String path);

    ITellraw link(String url);

    ITellraw suggest(String command);

    ITellraw command(String command);

    ITellraw tooltip(List<String> lines);

    ITellraw tooltip(String... lines);

    ITellraw then(Object obj);

    String toJSONString();

    void send(CommandSender sender);

    void send(Player player);

    void send(Iterable<Player> players);

    void send(Collection<? extends Player> players);
}
