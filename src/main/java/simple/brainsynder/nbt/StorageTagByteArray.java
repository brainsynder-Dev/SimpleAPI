package simple.brainsynder.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class StorageTagByteArray extends StorageBase {
    /**
     * The byte array stored in the tag.
     */
    private byte[] data;

    StorageTagByteArray() {
    }

    public StorageTagByteArray(byte[] data) {
        this.data = data;
    }

    public StorageTagByteArray(List<Byte> byteList) {
        this(toArray(byteList));
    }

    private static byte[] toArray(List<Byte> byteList) {
        byte[] abyte = new byte[byteList.size()];

        for (int i = 0; i < byteList.size(); ++i) {
            Byte obyte = byteList.get(i);
            abyte[i] = obyte == null ? 0 : obyte.byteValue();
        }

        return abyte;
    }

    /**
     * Write the actual data contents of the tag, implemented in NBT extension classes
     */
    void write(DataOutput output) throws IOException {
        output.writeInt(this.data.length);
        output.write(this.data);
    }

    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(192L);
        int i = input.readInt();
        sizeTracker.read((long) (8 * i));
        this.data = new byte[i];
        input.readFully(this.data);
    }

    /**
     * Gets the type byte for the tag.
     */
    public byte getId() {
        return 7;
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder("[B;");

        for (int i = 0; i < this.data.length; ++i) {
            if (i != 0) {
                stringbuilder.append(',');
            }

            stringbuilder.append((int) this.data[i]).append('B');
        }

        return stringbuilder.append(']').toString();
    }

    /**
     * Creates a clone of the tag.
     */
    public StorageBase copy() {
        byte[] abyte = new byte[this.data.length];
        System.arraycopy(this.data, 0, abyte, 0, this.data.length);
        return new StorageTagByteArray(abyte);
    }

    public boolean equals(Object instance) {
        return super.equals(instance) && Arrays.equals(this.data, ((StorageTagByteArray) instance).data);
    }

    public int hashCode() {
        return super.hashCode() ^ Arrays.hashCode(this.data);
    }

    public byte[] getByteArray() {
        return this.data;
    }
}
