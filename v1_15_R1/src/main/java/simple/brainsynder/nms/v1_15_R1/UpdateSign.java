package simple.brainsynder.nms.v1_15_R1;

import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.PacketPlayOutTileEntityData;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import simple.brainsynder.nms.IUpdateSign;

public class UpdateSign implements IUpdateSign {
    @Override
    public void changeLines(Location loc, Player player, String[] args) {
        BlockPosition pos = new BlockPosition(loc.getX(), loc.getY (), loc.getZ ());
        PacketPlayOutTileEntityData packet = new PacketPlayOutTileEntityData (pos, 9, makeNBT(args));
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }

    private NBTTagCompound makeNBT(String[] args) {
        NBTTagCompound nbtTagCompound = new NBTTagCompound ();
        for(int i = 0; i < 4; ++i) {
            nbtTagCompound.setString("Text" + (i + 1), args[i]);
        }
        return nbtTagCompound;
    }
}
