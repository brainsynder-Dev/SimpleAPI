package simple.brainsynder.nms.v1_9_R1;

import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.EnumParticle;
import net.minecraft.server.v1_9_R1.PacketPlayOutWorldParticles;
import simple.brainsynder.nms.IParticlePacket;

public class ParticlePacket implements IParticlePacket<EntityPlayer, EnumParticle> {
    @Override
    public void sendParticle(EntityPlayer player, EnumParticle enumParticle, float x, float y, float z, float offX, float offY, float offZ, float speed, int amount, boolean distance, int... data) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(enumParticle, distance, x, y, z, offX, offY, offZ, speed, amount, data);
        player.playerConnection.sendPacket(packet);
    }
}
