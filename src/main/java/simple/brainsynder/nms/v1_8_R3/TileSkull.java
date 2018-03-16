package simple.brainsynder.nms.v1_8_R3;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.TileEntitySkull;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import simple.brainsynder.nms.ITileEntitySkull;

public class TileSkull implements ITileEntitySkull {
    TileEntitySkull tileSkull;
    public TileSkull (Skull skull) {
        tileSkull = (TileEntitySkull) ((CraftWorld) skull.getWorld()).getHandle().getTileEntity(new BlockPosition(skull.getX(), skull.getY(), skull.getZ()));
    }
    
    @Override
    public GameProfile getGameProfile() {
        return tileSkull.getGameProfile();
    }
    
    @Override public void setGameProfile(GameProfile profile) {
        tileSkull.setGameProfile(profile);
    }
    
    @Override
    public int getRotation() {
        return tileSkull.getRotation ();
    }
    
    @Override public void setRotation(int rotation) {
        tileSkull.setRotation(rotation);
    }
}
