package simple.brainsynder.nms;

import org.bukkit.Color;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.storage.TriLoc;

public interface IParticleSender<T> {
    Object getPacket(ParticleMaker.Particle particle, TriLoc<Float> loc, TriLoc<Float> offset, float speed, int count, T data);


    class DustOptions {
        private Color color;
        private float size;

        public DustOptions (Color color, float size) {
            this.color = color;
            this.size = size;
        }

        public Color getColor() {
            return color;
        }

        public float getSize() {
            return size;
        }
    }
}
