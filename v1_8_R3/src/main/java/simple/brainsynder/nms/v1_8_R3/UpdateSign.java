package simple.brainsynder.nms.v1_8_R3;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutUpdateSign;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import simple.brainsynder.nms.IUpdateSign;

public class UpdateSign implements IUpdateSign {
    @Override
    public void changeLines(Location loc, Player player, String[] args) {
        World mcWorld = ((CraftWorld)loc.getWorld()).getHandle();
        BlockPosition pos = new BlockPosition(loc.getX(), loc.getY (), loc.getZ ());
        PacketPlayOutUpdateSign packet = new PacketPlayOutUpdateSign(mcWorld, pos, new IChatBaseComponent[] {
                IChatBaseComponent.ChatSerializer.a("{'text':'" + args[0] + "'}"),
                IChatBaseComponent.ChatSerializer.a("{'text':'" + args[1] + "'}"),
                IChatBaseComponent.ChatSerializer.a("{'text':'" + args[2] + "'}"),
                IChatBaseComponent.ChatSerializer.a("{'text':'" + args[3] + "'}"),
        });
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }
}
