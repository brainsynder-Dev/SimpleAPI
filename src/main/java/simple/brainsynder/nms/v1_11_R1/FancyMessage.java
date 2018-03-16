/*
 * Copyright (c) created class file on: 2015.
 *
 * All rights reserved.
 *
 * If you are reading this you are in violation of this copyright.
 *
 * Copyright owner: brainsynder/Magnus498
 */

package simple.brainsynder.nms.v1_11_R1;

import net.minecraft.server.v1_11_R1.IChatBaseComponent;
import net.minecraft.server.v1_11_R1.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import simple.brainsynder.nms.key.TellrawMaker;

public class FancyMessage extends TellrawMaker{
    
    public FancyMessage (String start) {
        super(start);
    }
    
    public void send(Player player) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(toJSONString())));
    }
}