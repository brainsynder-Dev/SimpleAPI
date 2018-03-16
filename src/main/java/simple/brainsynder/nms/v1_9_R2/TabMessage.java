package simple.brainsynder.nms.v1_9_R2;

import net.minecraft.server.v1_9_R2.IChatBaseComponent;
import net.minecraft.server.v1_9_R2.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import simple.brainsynder.nms.key.TabMaker;
import simple.brainsynder.utils.Reflection;

import java.lang.reflect.Field;

public class TabMessage extends TabMaker {
    @Override
    public void send(Player player) {
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + super.getHeader() + "\"}");
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter(cbc);
        if (!super.getFooter().equals("") || super.getFooter() != null) {
            try {
                IChatBaseComponent cbc1 = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + super.getFooter() + "\"}");
                Field field = packet.getClass().getDeclaredField("b");
                Reflection.setFieldAccessible(field);
                field.set(packet, cbc1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}
