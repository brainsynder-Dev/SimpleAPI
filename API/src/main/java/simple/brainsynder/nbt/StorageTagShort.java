package simple.brainsynder.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class StorageTagShort extends StoragePrimitive {
    /**
     * The short value for the tag.
     */
    private short data;

    public StorageTagShort() {
    }

    public StorageTagShort(short data) {
        this.data = data;
    }

    /**
     * Write the actual data contents of the tag, implemented in NBT extension classes
     */
    void write(DataOutput output) throws IOException {
        output.writeShort(this.data);
    }

    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(80L);
        this.data = input.readShort();
    }

    /**
     * Gets the type byte for the tag.
     */
    public byte getId() {
        return 2;
    }

    public String toString() {
        return this.data + "s";
    }

    /**
     * Creates a clone of the tag.
     */
    public StorageTagShort copy() {
        return new StorageTagShort(this.data);
    }

    public boolean equals(Object instance) {
        return super.equals(instance) && this.data == ((StorageTagShort) instance).data;
    }

    public int hashCode() {
        return super.hashCode() ^ this.data;
    }

    public long getLong() {
        return (long) this.data;
    }

    public int getInt() {
        return this.data;
    }

    public short getShort() {
        return this.data;
    }

    public byte getByte() {
        return (byte) (this.data & 255);
    }

    public double getDouble() {
        return (double) this.data;
    }

    public float getFloat() {
        return (float) this.data;
    }
}
