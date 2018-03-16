package simple.brainsynder.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class StorageTagLongArray extends StorageBase {
    private long[] longArray;

    StorageTagLongArray() {
    }

    public StorageTagLongArray(long[] longs) {
        this.longArray = longs;
    }

    public StorageTagLongArray(List<Long> longList) {
        this(toArray(longList));
    }

    private static long[] toArray(List<Long> longList) {
        long[] along = new long[longList.size()];

        for (int i = 0; i < longList.size(); ++i) {
            Long olong = longList.get(i);
            along[i] = olong == null ? 0L : olong.longValue();
        }

        return along;
    }

    /**
     * Write the actual data contents of the tag, implemented in NBT extension classes
     */
    void write(DataOutput output) throws IOException {
        output.writeInt(this.longArray.length);

        for (long i : this.longArray) {
            output.writeLong(i);
        }
    }

    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(192L);
        int i = input.readInt();
        sizeTracker.read((long) (64 * i));
        this.longArray = new long[i];

        for (int j = 0; j < i; ++j) {
            this.longArray[j] = input.readLong();
        }
    }

    /**
     * Gets the type byte for the tag.
     */
    public byte getId() {
        return 12;
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder("[L;");

        for (int i = 0; i < this.longArray.length; ++i) {
            if (i != 0) {
                stringbuilder.append(',');
            }

            stringbuilder.append(this.longArray[i]).append('L');
        }

        return stringbuilder.append(']').toString();
    }

    /**
     * Creates a clone of the tag.
     */
    public StorageTagLongArray copy() {
        long[] along = new long[this.longArray.length];
        System.arraycopy(this.longArray, 0, along, 0, this.longArray.length);
        return new StorageTagLongArray(along);
    }

    public boolean equals(Object instance) {
        return super.equals(instance) && Arrays.equals(this.longArray, ((StorageTagLongArray) instance).longArray);
    }

    public int hashCode() {
        return super.hashCode() ^ Arrays.hashCode(this.longArray);
    }
}
