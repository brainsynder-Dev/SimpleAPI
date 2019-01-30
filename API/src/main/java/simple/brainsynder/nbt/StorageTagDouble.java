package simple.brainsynder.nbt;

import simple.brainsynder.math.MathUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class StorageTagDouble extends StoragePrimitive {
    /**
     * The double value for the tag.
     */
    private double data;

    StorageTagDouble() {
    }

    public StorageTagDouble(double data) {
        this.data = data;
    }

    /**
     * Write the actual data contents of the tag, implemented in NBT extension classes
     */
    void write(DataOutput output) throws IOException {
        output.writeDouble(this.data);
    }

    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(128L);
        this.data = input.readDouble();
    }

    /**
     * Gets the type byte for the tag.
     */
    public byte getId() {
        return 6;
    }

    public String toString() {
        return this.data + "d";
    }

    /**
     * Creates a clone of the tag.
     */
    public StorageTagDouble copy() {
        return new StorageTagDouble(this.data);
    }

    public boolean equals(Object instance) {
        return super.equals(instance) && this.data == ((StorageTagDouble) instance).data;
    }

    public int hashCode() {
        long i = Double.doubleToLongBits(this.data);
        return super.hashCode() ^ (int) (i ^ i >>> 32);
    }

    public long getLong() {
        return (long) Math.floor(this.data);
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
        return this.data;
    }

    public float getFloat() {
        return (float) this.data;
    }
}
