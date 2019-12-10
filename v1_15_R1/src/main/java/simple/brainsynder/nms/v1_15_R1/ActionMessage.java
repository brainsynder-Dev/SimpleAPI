package simple.brainsynder.nms.v1_15_R1;

import net.minecraft.server.v1_15_R1.ChatMessageType;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import simple.brainsynder.events.ActionMessageEvent;
import simple.brainsynder.nms.key.ActionMaker;

public class ActionMessage extends ActionMaker {

    @Override
    public void sendMessage(Player player, String message) {
        ActionMessageEvent event = new ActionMessageEvent(player, message);
        Bukkit.getServer().getPluginManager().callEvent(event);
        CraftPlayer p = (CraftPlayer)event.getTarget();
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + event.getText() + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, ChatMessageType.GAME_INFO);
        p.getHandle().playerConnection.sendPacket(ppoc);
    }
}
