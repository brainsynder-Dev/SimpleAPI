package simple.brainsynder.nbt;

import simple.brainsynder.math.MathUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class StorageTagFloat extends StoragePrimitive {
    /**
     * The float value for the tag.
     */
    private float data;

    StorageTagFloat() {
    }

    public StorageTagFloat(float data) {
        this.data = data;
    }

    /**
     * Write the actual data contents of the tag, implemented in NBT extension classes
     */
    void write(DataOutput output) throws IOException {
        output.writeFloat(this.data);
    }

    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(96L);
        this.data = input.readFloat();
    }

    /**
     * Gets the type byte for the tag.
     */
    public byte getId() {
        return 5;
    }

    public String toString() {
        return this.data + "f";
    }

    /**
     * Creates a clone of the tag.
     */
    public StorageTagFloat copy() {
        return new StorageTagFloat(this.data);
    }

    public boolean equals(Object instance) {
        return super.equals(instance) && this.data == ((StorageTagFloat) instance).data;
    }

    public int hashCode() {
        return super.hashCode() ^ Float.floatToIntBits(this.data);
    }

    public long getLong() {
        return (long) this.data;
    }

    public int getInt() {
        return MathUtils.floor(this.data);
    }

    public short getShort() {
        return (short) (MathUtils.floor(this.data) & 65535);
    }

    public byte getByte() {
        return (byte) (MathUtils.floor(this.data) & 255);
    }

    public double getDouble() {
        return (double) this.data;
    }

    public float getFloat() {
        return this.data;
    }
}
