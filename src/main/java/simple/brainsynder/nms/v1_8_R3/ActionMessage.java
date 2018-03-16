package simple.brainsynder.nms.v1_8_R3;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
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
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte)2);
        p.getHandle().playerConnection.sendPacket(ppoc);
    }
}
