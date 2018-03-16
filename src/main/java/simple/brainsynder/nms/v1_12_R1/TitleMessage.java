package simple.brainsynder.nms.v1_12_R1;

import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import simple.brainsynder.nms.key.TitleMaker;

public class TitleMessage extends TitleMaker {
    public TitleMessage() {
    }

    @Override
    public void sendMessage(Player var1, int var2, int var3, int var4, String var5, String var6) {
        EntityPlayer player = ((CraftPlayer)var1).getHandle();
        if (var5 != null) {
            IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + var5 + "\"}");
            PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, cbc, var2, var3, var4);
            player.playerConnection.sendPacket(titlePacket);
        }
        if (var6 != null) {
            IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + var6 + "\"}");
            PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, cbc, var2, var3, var4);
            player.playerConnection.sendPacket(subtitlePacket);
        }
    }
}
