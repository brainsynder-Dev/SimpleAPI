package simple.brainsynder.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class StorageTagString extends StorageBase {
    /**
     * The string value for the tag (cannot be empty).
     */
    private String data;

    public StorageTagString() {
        this("");
    }

    public StorageTagString(String data) {
        Objects.requireNonNull(data, "Null string not allowed");
        this.data = data;
    }

    public static String configure(String data) {
        StringBuilder stringbuilder = new StringBuilder("\"");

        for (int i = 0; i < data.length(); ++i) {
            char c0 = data.charAt(i);

            if (c0 == '\\' || c0 == '"') {
                stringbuilder.append('\\');
            }

            stringbuilder.append(c0);
        }

        return stringbuilder.append('"').toString();
    }

    /**
     * Write the actual data contents of the tag, implemented in NBT extension classes
     */
    void write(DataOutput output) throws IOException {
        output.writeUTF(this.data);
    }

    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(288L);
        this.data = input.readUTF();
        sizeTracker.read((long) (16 * this.data.length()));
    }

    /**
     * Gets the type byte for the tag.
     */
    public byte getId() {
        return 8;
    }

    public String toString() {
        return configure(this.data);
    }

    /**
     * Creates a clone of the tag.
     */
    public StorageTagString copy() {
        return new StorageTagString(this.data);
    }

    /**
     * Return whether this compound has no tags.
     */
    public boolean hasNoTags() {
        return this.data.isEmpty();
    }

    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        } else {
            StorageTagString nbttagstring = (StorageTagString) o;
            return this.data == null && nbttagstring.data == null || Objects.equals(this.data, nbttagstring.data);
        }
    }

    public int hashCode() {
        return super.hashCode() ^ this.data.hashCode();
    }

    public String getString() {
        return this.data;
    }
}
