package simple.brainsynder.nms.key;

import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simple.brainsynder.nms.ITellraw;
import simple.brainsynder.utils.MessagePart;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TellrawMaker implements ITellraw {
    protected final List<MessagePart> messageParts = new ArrayList<>();
    protected String jsonString = null;
    protected boolean dirty = false;

    public TellrawMaker (String start) {
        this.messageParts.add(new MessagePart(start));
    }

    public TellrawMaker color(ChatColor color) {
        if (!color.isColor()) {
            throw new IllegalArgumentException(color.name() + " is not a color");
        }
        latest().color = color;
        this.dirty = true;
        return this;
    }

    public TellrawMaker style(ChatColor[] styles) {
        for (ChatColor style : styles) {
            if (!style.isFormat()) {
                throw new IllegalArgumentException(style.name() + " is not a style");
            }
        }
        latest().styles = styles;
        this.dirty = true;
        return this;
    }

    public TellrawMaker file(String path) {
        onClick("open_file", path);
        return this;
    }

    public TellrawMaker link(String url) {
        onClick("open_url", url);
        return this;
    }

    public TellrawMaker suggest(String command) {
        onClick("suggest_command", command);
        return this;
    }

    public TellrawMaker command(String command) {
        onClick("run_command", command);
        return this;
    }

    public TellrawMaker achievementTooltip(String name) {
        onHover("show_achievement", "achievement." + name);
        return this;
    }

    public TellrawMaker itemTooltip(String itemJSON) {
        onHover("show_item", itemJSON);
        return this;
    }

    public TellrawMaker tooltip(List<String> lines) {
        return tooltip(lines.toArray(new String[lines.size()]));
    }

    public TellrawMaker tooltip(String... lines) {
        onHover("show_text", combineArray(0, "\n", lines));
        return this;
    }

    public TellrawMaker then(Object obj) {
        this.messageParts.add(new MessagePart(obj.toString()));
        this.dirty = true;
        return this;
    }

    public String toJSONString() {
        if ((!this.dirty) && (this.jsonString != null)) {
            return this.jsonString;
        }
        StringWriter string = new StringWriter();
        JsonWriter json = new JsonWriter(string);
        try {
            if (this.messageParts.size() == 1) {
                latest().writeJson(json);
            } else {
                json.beginObject().name("text").value("").name("extra").beginArray();
                for (MessagePart part : this.messageParts) {
                    part.writeJson(json);
                }
                json.endArray().endObject();
                json.close();
            }
        } catch (IOException e) {
        }
        this.jsonString = string.toString();
        this.dirty = false;
        return this.jsonString;
    }

    private MessagePart latest() {
        return this.messageParts.get(this.messageParts.size() - 1);
    }

    private String combineArray(int startIndex, String separator, String... stringArray) {
        return combineArray(startIndex, stringArray.length, separator, stringArray);
    }

    private String combineArray(int startIndex, int endIndex, String separator, String... stringArray) {
        if(stringArray != null && startIndex < endIndex) {
            StringBuilder builder = new StringBuilder();

            for(int i = startIndex; i < endIndex; ++i) {
                builder.append(ChatColor.translateAlternateColorCodes('&', stringArray[i]));
                builder.append(separator);
            }

            builder.delete(builder.length() - separator.length(), builder.length());
            return builder.toString();
        } else {
            return "";
        }
    }

    @Override
    public void send(CommandSender sender) {
        if (sender instanceof Player) {
            send((Player)sender);
            return;
        }

        StringBuilder builder = new StringBuilder();
        messageParts.forEach(part -> {
            if (part.color != null) builder.append(part.color);
            if (part.styles != null) {
                for (ChatColor style : part.styles) builder.append(style);
            }
            builder.append(part.text);
        });
        sender.sendMessage(builder.toString());
    }

    public void send(Iterable<Player> players) {
        for (Player player : players)
            send (player);
    }

    @Override
    public void send(Player player) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName () + " " + toJSONString());
    }

    public void send(Collection<? extends Player> players) {
        for (Player player : players)
            send (player);
    }

    private void onClick(String name, String data) {
        MessagePart latest = latest();
        latest.clickActionName = name;
        latest.clickActionData = data;
        this.dirty = true;
    }

    private void onHover(String name, String data) {
        MessagePart latest = latest();
        latest.hoverActionName = name;
        latest.hoverActionData = data;
        this.dirty = true;
    }
}
