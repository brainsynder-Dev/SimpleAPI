package simple.brainsynder.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class StorageTagIntArray extends StorageBase {
    /**
     * The array of saved integers
     */
    private int[] intArray;

    StorageTagIntArray() {
    }

    public StorageTagIntArray(int[] ints) {
        this.intArray = ints;
    }

    public StorageTagIntArray(List<Integer> integerList) {
        this(toArray(integerList));
    }

    private static int[] toArray(List<Integer> integerList) {
        int[] aint = new int[integerList.size()];

        for (int i = 0; i < integerList.size(); ++i) {
            Integer integer = integerList.get(i);
            aint[i] = integer == null ? 0 : integer.intValue();
        }

        return aint;
    }

    /**
     * Write the actual data contents of the tag, implemented in NBT extension classes
     */
    void write(DataOutput output) throws IOException {
        output.writeInt(this.intArray.length);

        for (int i : this.intArray) {
            output.writeInt(i);
        }
    }

    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(192L);
        int i = input.readInt();
        sizeTracker.read((long) (32 * i));
        this.intArray = new int[i];

        for (int j = 0; j < i; ++j) {
            this.intArray[j] = input.readInt();
        }
    }

    /**
     * Gets the type byte for the tag.
     */
    public byte getId() {
        return 11;
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder("[I;");

        for (int i = 0; i < this.intArray.length; ++i) {
            if (i != 0) {
                stringbuilder.append(',');
            }

            stringbuilder.append(this.intArray[i]);
        }

        return stringbuilder.append(']').toString();
    }

    /**
     * Creates a clone of the tag.
     */
    public StorageTagIntArray copy() {
        int[] aint = new int[this.intArray.length];
        System.arraycopy(this.intArray, 0, aint, 0, this.intArray.length);
        return new StorageTagIntArray(aint);
    }

    public boolean equals(Object instance) {
        return super.equals(instance) && Arrays.equals(this.intArray, ((StorageTagIntArray) instance).intArray);
    }

    public int hashCode() {
        return super.hashCode() ^ Arrays.hashCode(this.intArray);
    }

    public int[] getIntArray() {
        return this.intArray;
    }
}
