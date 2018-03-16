package simple.brainsynder.nms;

public interface IParticlePacket<V, T> {
    void sendParticle (V player, T enumParticle, float x, float y, float z, float offX, float offY, float offZ, float speed, int amount, boolean distance, int... data);
}
