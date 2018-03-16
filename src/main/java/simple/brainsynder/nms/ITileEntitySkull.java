package simple.brainsynder.nms;

import com.mojang.authlib.GameProfile;

public interface ITileEntitySkull {
    GameProfile getGameProfile ();

    void setGameProfile (GameProfile profile);

    int getRotation ();
    
    void setRotation (int rotation);
}
