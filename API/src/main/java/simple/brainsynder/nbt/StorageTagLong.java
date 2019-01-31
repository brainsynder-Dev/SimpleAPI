package simple.brainsynder.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class StorageTagLong extends StoragePrimitive {
    /**
     * The long value for the tag.
     */
    private long data;

    StorageTagLong() {
    }

    public StorageTagLong(long data) {
        this.data = data;
    }

    /**
     * Write the actual data contents of the tag, implemented in NBT extension classes
     */
    void write(DataOutput output) throws IOException {
        output.writeLong(this.data);
    }

    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(128L);
        this.data = input.readLong();
    }

    /**
     * Gets the type byte for the tag.
     */
    public byte getId() {
        return 4;
    }

    public String toString() {
        return this.data + "L";
    }

    /**
     * Creates a clone of the tag.
     */
    public StorageTagLong copy() {
        return new StorageTagLong(this.data);
    }

    public boolean equals(Object instance) {
        return super.equals(instance) && this.data == ((StorageTagLong) instance).data;
    }

    public int hashCode() {
        return super.hashCode() ^ (int) (this.data ^ this.data >>> 32);
    }

    public long getLong() {
        return this.data;
    }

    public int getInt() {
        return (int) (this.data & -1L);
    }

    public short getShort() {
        return (short) ((int) (this.data & 65535L));
    }

    public byte getByte() {
        return (byte) ((int) (this.data & 255L));
    }

    public double getDouble() {
        return (double) this.data;
    }

    public float getFloat() {
        return (float) this.data;
    }
}
