package simple.brainsynder.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class StorageBase {
    public static final String[] NBT_TYPES = new String[]{"END", "BYTE", "SHORT", "INT", "LONG", "FLOAT", "DOUBLE", "BYTE[]", "STRING", "LIST", "COMPOUND", "INT[]", "LONG[]"};

    /**
     * Creates a new NBTBase object that corresponds with the passed in id.
     */
    protected static StorageBase createNewByType(byte id) {
        switch (id) {
            case 0:
                return new StorageTagEnd();

            case 1:
                return new StorageTagByte();

            case 2:
                return new StorageTagShort();

            case 3:
                return new StorageTagInt();

            case 4:
                return new StorageTagLong();

            case 5:
                return new StorageTagFloat();

            case 6:
                return new StorageTagDouble();

            case 7:
                return new StorageTagByteArray();

            case 8:
                return new StorageTagString();

            case 9:
                return new StorageTagList();

            case 10:
                return new StorageTagCompound();

            case 11:
                return new StorageTagIntArray();

            case 12:
                return new StorageTagLongArray();

            default:
                return null;
        }
    }

    public static String getName(int id) {
        switch (id) {
            case 0:
                return "TAG_End";

            case 1:
                return "TAG_Byte";

            case 2:
                return "TAG_Short";

            case 3:
                return "TAG_Int";

            case 4:
                return "TAG_Long";

            case 5:
                return "TAG_Float";

            case 6:
                return "TAG_Double";

            case 7:
                return "TAG_Byte_Array";

            case 8:
                return "TAG_String";

            case 9:
                return "TAG_List";

            case 10:
                return "TAG_Compound";

            case 11:
                return "TAG_Int_Array";

            case 12:
                return "TAG_Long_Array";

            case 99:
                return "Any Numeric Tag";

            default:
                return "UNKNOWN";
        }
    }

    /**
     * Write the actual data contents of the tag, implemented in NBT extension classes
     */
    abstract void write(DataOutput output) throws IOException;

    abstract void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException;

    public abstract String toString();

    /**
     * Gets the type byte for the tag.
     */
    public abstract byte getId();

    /**
     * Creates a clone of the tag.
     */
    public abstract StorageBase copy();

    /**
     * Return whether this compound has no tags.
     */
    public boolean hasNoTags() {
        return false;
    }

    public boolean equals(Object instance) {
        return instance instanceof StorageBase && this.getId() == ((StorageBase) instance).getId();
    }

    public int hashCode() {
        return this.getId();
    }

    protected String getString() {
        return this.toString();
    }
}
