package simple.brainsynder.nms.key;

import org.bukkit.inventory.ItemStack;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.nms.IParticleSender;
import simple.brainsynder.storage.TriLoc;
import simple.brainsynder.utils.Reflection;
import simple.brainsynder.utils.ServerVersion;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ParticleSender implements IParticleSender<Object> {
    private Constructor<?> packetConstructor = null;
    private boolean newParticlePacketConstructor = false;
    private Class<?> enumParticle = null;

    public ParticleSender () {
        try {
            Class<?> packetClass = Reflection.getNmsClass("PacketPlayOutWorldParticles");
            if (ServerVersion.getVersion().getIntVersion() < ServerVersion.v1_8_R3.getIntVersion()) {
                packetConstructor = packetClass.getConstructor(
                        String.class,
                        Float.TYPE,
                        Float.TYPE,
                        Float.TYPE,
                        Float.TYPE,
                        Float.TYPE,
                        Float.TYPE,
                        Float.TYPE,
                        Integer.TYPE);
            } else {
                newParticlePacketConstructor = true;
                enumParticle = Reflection.getNmsClass("EnumParticle");
                packetConstructor = packetClass.getDeclaredConstructor(
                        enumParticle,
                        Boolean.TYPE,
                        Float.TYPE,
                        Float.TYPE,
                        Float.TYPE,
                        Float.TYPE,
                        Float.TYPE,
                        Float.TYPE,
                        Float.TYPE,
                        Integer.TYPE,
                        int[].class);
            }
        } catch (Exception ex) {}
    }

    @Override
    public Object getPacket(ParticleMaker.Particle type, TriLoc<Float> loc, TriLoc<Float> offset, float speed, int count, Object data) {
        int[] inner = new int[0];
        float offsetX = offset.getX(),
                offsetY = offset.getY(),
                offsetZ = offset.getZ();

        if ((type == ParticleMaker.Particle.ITEM_CRACK)
                || (type == ParticleMaker.Particle.BLOCK_CRACK)
                || (type == ParticleMaker.Particle.ITEM_TAKE)
                || (type == ParticleMaker.Particle.BLOCK_DUST)) {
            if (data == null) {
                inner = new int[] {1,0};
            }else if (data instanceof ItemStack){
                ItemStack stack = (ItemStack)data;
                inner = new int[] {stack.getType().getId(), stack.getDurability()};
            }else if (data instanceof int[]){
                inner = (int[])data;
            }
        }
        if (data instanceof DustOptions) {
            DustOptions dust = (DustOptions) data;
            offsetX=getColor(dust.getColor().getRed());
            offsetY=getColor(dust.getColor().getGreen());
            offsetZ=getColor(dust.getColor().getBlue());
        }

        if (!type.isCompatable()) return null;
        try {
            Object packet;
            if (newParticlePacketConstructor) {
                Object particleType = enumParticle.getEnumConstants()[type.getId()];
                packet = packetConstructor.newInstance(
                        particleType,
                        true,
                        (float) loc.getX(),
                        (float) loc.getY(),
                        (float) loc.getZ(),
                        (float) offsetX,
                        (float) offsetY,
                        (float) offsetZ,
                        (float) speed,
                        count,
                        inner);
            } else {
                packet = packetConstructor.newInstance(
                        type.getName(),
                        (float) loc.getX(),
                        (float) loc.getY(),
                        (float) loc.getZ(),
                        (float) offsetX,
                        (float) offsetY,
                        (float) offsetZ,
                        (float) speed,
                        count);
            }
            return packet;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException ignored) {}
        return null;
    }

    private float getColor(double value) {
        if (value <= 0.0F) {
            value = -1.0F;
        }

        return ((float) value) / 255.0F;
    }

}
