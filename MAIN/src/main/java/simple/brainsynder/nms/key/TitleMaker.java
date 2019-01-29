package simple.brainsynder.nms.key;

import org.bukkit.entity.Player;
import simple.brainsynder.nms.ITitleMessage;

import java.util.Collection;

public class TitleMaker implements ITitleMessage {
    @Override
    public void sendMessage(Player var1, String var2, String var3) {
        sendMessage(var1, 5, var2, var3);
    }

    @Override
    public void sendMessage(Player var1, String var2) {
        sendMessage(var1, 5, var2);
    }

    @Override
    public void sendMessage(Player var1, int var2, int var3, int var4, String var5, String var6) {
        var1.sendMessage (var5 + " " + var6);
    }

    @Override
    public void sendMessage(Player var1, int var2, int var3, int var4, String var5) {
        sendMessage(var1, var3, var2, var4, var5, null);
    }

    @Override
    public void sendMessage(Player var1, int var2, String var5, String var6) {
        sendMessage(var1, var2, var2, var2, var5, var6);
    }

    @Override
    public void sendMessage(Player var1, int var2, String var5) {
        sendMessage(var1, var2, var2, var2, var5, null);
    }

    @Override
    public void sendMessage(Collection<? extends Player> var1, String var2, String var3) {
        for (Player player : var1)
            sendMessage(player, var2, var3);
    }

    @Override
    public void sendMessage(Collection<? extends Player> var1, String var2) {
        for (Player player : var1)
            sendMessage(player, var2);
    }

    @Override
    public void sendMessage(Collection<? extends Player> var1, int var2, int var3, int var4, String var5, String var6) {
        for (Player player : var1)
            sendMessage(player, var2, var3, var4, var5, var6);
    }

    @Override
    public void sendMessage(Collection<? extends Player> var1, int var2, int var3, int var4, String var5) {

        for (Player player : var1)
            sendMessage(player, var2, var3, var4, var5);
    }

    @Override
    public void sendMessage(Collection<? extends Player> var1, int var2, String var5, String var6) {

        for (Player player : var1)
            sendMessage(player, var2, var5, var6);
    }

    @Override
    public void sendMessage(Collection<? extends Player> var1, int var2, String var5) {

        for (Player player : var1)
            sendMessage(player, var2, var5);
    }
}
