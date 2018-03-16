package simple.brainsynder.nms;

import org.bukkit.entity.Player;

import java.util.Collection;

public interface ITitleMessage {
    void sendMessage(Player var1, String var2, String var3);

    void sendMessage(Player var1, String var2);

    void sendMessage(Player var1, int var2, int var3, int var4, String var5, String var6);

    void sendMessage(Player var1, int var2, int var3, int var4, String var5);

    void sendMessage(Player var1, int var2, String var5, String var6);

    void sendMessage(Player var1, int var2, String var5);

    void sendMessage(Collection<? extends Player> var1, String var2, String var3);

    void sendMessage(Collection<? extends Player> var1, String var2);

    void sendMessage(Collection<? extends Player> var1, int var2, int var3, int var4, String var5, String var6);

    void sendMessage(Collection<? extends Player> var1, int var2, int var3, int var4, String var5);

    void sendMessage(Collection<? extends Player> var1, int var2, String var5, String var6);

    void sendMessage(Collection<? extends Player> var1, int var2, String var5);
}
