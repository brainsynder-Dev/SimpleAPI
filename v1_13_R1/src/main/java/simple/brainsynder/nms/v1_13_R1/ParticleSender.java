package simple.brainsynder.nms.v1_13_R1;

import net.minecraft.server.v1_13_R1.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_13_R1.ParticleParam;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_13_R1.CraftParticle;
import org.bukkit.inventory.ItemStack;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.nms.IParticleSender;
import simple.brainsynder.storage.TriLoc;

public class ParticleSender implements IParticleSender<Object> {
    @Override
    public Object getPacket(ParticleMaker.Particle particle, TriLoc<Float> loc, TriLoc<Float> offset, float speed, int count, Object data) {
        if (!particle.isCompatable()) return null;
        Object target = null;
        if (data instanceof ItemStack) {
            ItemStack item = (ItemStack) data;
            if (item.getType().isBlock() && (particle.name().contains("BLOCK"))) {
                target = Bukkit.createBlockData(item.getType());
            } else {
                target = item;
            }
        }

        if (data instanceof DustOptions) {
            DustOptions dustOptions = (DustOptions) data;
            target  = new Particle.DustOptions(dustOptions.getColor(), dustOptions.getSize());
        }

        ParticleParam param = CraftParticle.toNMS(Particle.valueOf(particle.name()), target);
        return new PacketPlayOutWorldParticles(param, true, loc.getX(), loc.getY(), loc.getZ(), offset.getX(), offset.getY(), offset.getZ(), speed, count);
    }
}
