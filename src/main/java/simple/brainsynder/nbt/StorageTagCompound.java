package simple.brainsynder.nbt;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import simple.brainsynder.exceptions.SimpleAPIException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class StorageTagCompound extends StorageBase {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Pattern PATTERN = Pattern.compile("[A-Za-z0-9._+-]+");
    private final Map<String, StorageBase> tagMap = Maps.newHashMap();

    private static void writeEntry(String name, StorageBase data, DataOutput output) throws IOException {
        output.writeByte(data.getId());

        if (data.getId() != 0) {
            output.writeUTF(name);
            data.write(output);
        }
    }

    private static byte readType(DataInput input, NBTSizeTracker sizeTracker) throws IOException {
        return input.readByte();
    }

    private static String readKey(DataInput input, NBTSizeTracker sizeTracker) throws IOException {
        return input.readUTF();
    }

    static StorageBase readNBT(byte id, String key, DataInput input, int depth, NBTSizeTracker sizeTracker) {
        StorageBase nbtbase = StorageBase.createNewByType(id);

        try {
            nbtbase.read(input, depth, sizeTracker);
            return nbtbase;
        } catch (IOException ioexception) {
            throw new SimpleAPIException(ioexception);
        }
    }

    protected static String match(String s) {
        return PATTERN.matcher(s).matches() ? s : StorageTagString.configure(s);
    }

    static void escape(String s, StringBuffer sb) {
        for (int i = 0; i < s.length(); ++i) {
            char ch = s.charAt(i);
            switch (ch) {
                case '\b':
                    sb.append("\\b");
                    continue;
                case '\n':
                    sb.append("\\n");
                    continue;
                case '\f':
                    sb.append("\\f");
                    continue;
                case '\r':
                    sb.append("\\r");
                    continue;
                case '"':
                    sb.append("\\\"");
                    continue;
                case '/':
                    sb.append("\\/");
                    continue;
                case '\\':
                    sb.append("\\\\");
                    continue;
            }

            if (ch >= 0 && ch <= 31 || ch >= 127 && ch <= 159 || ch >= 8192 && ch <= 8447) {
                String ss = Integer.toHexString(ch);
                sb.append("\\u");

                for (int k = 0; k < 4 - ss.length(); ++k) {
                    sb.append('0');
                }

                sb.append(ss.toUpperCase());
            } else {
                sb.append(ch);
            }
        }

    }

    /**
     * Write the actual data contents of the tag, implemented in NBT extension classes
     */
    void write(DataOutput output) throws IOException {
        for (String s : this.tagMap.keySet()) {
            StorageBase nbtbase = this.tagMap.get(s);
            writeEntry(s, nbtbase, output);
        }

        output.writeByte(0);
    }

    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(384L);

        if (depth > 512) {
            throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
        } else {
            this.tagMap.clear();
            byte b0;

            while ((b0 = readType(input, sizeTracker)) != 0) {
                String s = readKey(input, sizeTracker);
                sizeTracker.read((long) (224 + 16 * s.length()));
                StorageBase nbtbase = readNBT(b0, s, input, depth + 1, sizeTracker);

                if (this.tagMap.put(s, nbtbase) != null) {
                    sizeTracker.read(288L);
                }
            }
        }
    }

    public Set<String> getKeySet() {
        return this.tagMap.keySet();
    }

    /**
     * Gets the type byte for the tag.
     */
    public byte getId() {
        return 10;
    }

    public int getSize() {
        return this.tagMap.size();
    }

    /**
     * Stores the given tag into the map with the given string key. This is mostly used to store tag lists.
     */
    public void setTag(String key, StorageBase value) {
        this.tagMap.put(key, value);
    }

    /**
     * Stores a new NBTTagByte with the given byte value into the map with the given string key.
     */
    public void setByte(String key, byte value) {
        this.tagMap.put(key, new StorageTagByte(value));
    }

    /**
     * Stores a new NBTTagShort with the given short value into the map with the given string key.
     */
    public void setShort(String key, short value) {
        this.tagMap.put(key, new StorageTagShort(value));
    }

    /**
     * Stores a new NBTTagInt with the given integer value into the map with the given string key.
     */
    public void setInteger(String key, int value) {
        this.tagMap.put(key, new StorageTagInt(value));
    }

    /**
     * Stores a new NBTTagLong with the given long value into the map with the given string key.
     */
    public void setLong(String key, long value) {
        this.tagMap.put(key, new StorageTagLong(value));
    }

    public void setUniqueId(String key, UUID value) {
        this.setLong(key + "Most", value.getMostSignificantBits());
        this.setLong(key + "Least", value.getLeastSignificantBits());
    }

    public UUID getUniqueId(String key) {
        return new UUID(this.getLong(key + "Most"), this.getLong(key + "Least"));
    }

    public boolean hasUniqueId(String key) {
        return this.hasKey(key + "Most", 99) && this.hasKey(key + "Least", 99);
    }

    /**
     * Stores a new NBTTagFloat with the given float value into the map with the given string key.
     */
    public void setFloat(String key, float value) {
        this.tagMap.put(key, new StorageTagFloat(value));
    }

    /**
     * Stores a new NBTTagDouble with the given double value into the map with the given string key.
     */
    public void setDouble(String key, double value) {
        this.tagMap.put(key, new StorageTagDouble(value));
    }

    /**
     * Stores a new NBTTagString with the given string value into the map with the given string key.
     */
    public void setString(String key, String value) {
        this.tagMap.put(key, new StorageTagString(value));
    }

    /**
     * Stores a new NBTTagByteArray with the given array as data into the map with the given string key.
     */
    public void setByteArray(String key, byte[] value) {
        this.tagMap.put(key, new StorageTagByteArray(value));
    }

    /**
     * Stores a new NBTTagIntArray with the given array as data into the map with the given string key.
     */
    public void setIntArray(String key, int[] value) {
        this.tagMap.put(key, new StorageTagIntArray(value));
    }

    /**
     * Stores the given boolean value as a NBTTagByte, storing 1 for true and 0 for false, using the given string key.
     */
    public void setBoolean(String key, boolean value) {
        tagMap.put(key, new StorageTagByte((byte) ((value) ? 1 : 0)));
    }

    /**
     * gets a generic tag with the specified name
     */
    public StorageBase getTag(String key) {
        return this.tagMap.get(key);
    }

    /**
     * Gets the ID byte for the given tag key
     */
    public byte getTagId(String key) {
        StorageBase nbtbase = this.tagMap.get(key);
        return nbtbase == null ? 0 : nbtbase.getId();
    }

    /**
     * Returns whether the given string has been previously stored as a key in the map.
     */
    public boolean hasKey(String key) {
        return this.tagMap.containsKey(key);
    }

    /**
     * Returns whether the given string has been previously stored as a key in this tag compound as a particular type,
     * denoted by a parameter in the form of an ordinal. If the provided ordinal is 99, this method will match tag types
     * representing numbers.
     */
    public boolean hasKey(String key, int type) {
        int i = this.getTagId(key);

        if (i == type) {
            return true;
        } else if (type != 99) {
            return false;
        } else {
            return i == 1 || i == 2 || i == 3 || i == 4 || i == 5 || i == 6;
        }
    }

    /**
     * Retrieves a byte value using the specified key, or 0 if no such key was stored.
     */
    public byte getByte(String key) {
        try {
            if (this.hasKey(key, 99)) {
                return ((StoragePrimitive) this.tagMap.get(key)).getByte();
            }
        } catch (ClassCastException ignored) {
        }

        return 0;
    }

    /**
     * Retrieves a short value using the specified key, or 0 if no such key was stored.
     */
    public short getShort(String key) {
        try {
            if (this.hasKey(key, 99)) {
                return ((StoragePrimitive) this.tagMap.get(key)).getShort();
            }
        } catch (ClassCastException ignored) {
        }

        return 0;
    }

    /**
     * Retrieves an integer value using the specified key, or 0 if no such key was stored.
     */
    public int getInteger(String key) {
        try {
            if (this.hasKey(key, 99)) {
                return ((StoragePrimitive) this.tagMap.get(key)).getInt();
            }
        } catch (ClassCastException ignored) {
        }

        return 0;
    }

    /**
     * Retrieves a long value using the specified key, or 0 if no such key was stored.
     */
    public long getLong(String key) {
        try {
            if (this.hasKey(key, 99)) {
                return ((StoragePrimitive) this.tagMap.get(key)).getLong();
            }
        } catch (ClassCastException ignored) {
        }

        return 0L;
    }

    /**
     * Retrieves a float value using the specified key, or 0 if no such key was stored.
     */
    public float getFloat(String key) {
        try {
            if (this.hasKey(key, 99)) {
                return ((StoragePrimitive) this.tagMap.get(key)).getFloat();
            }
        } catch (ClassCastException ignored) {
        }

        return 0.0F;
    }

    /**
     * Retrieves a double value using the specified key, or 0 if no such key was stored.
     */
    public double getDouble(String key) {
        try {
            if (this.hasKey(key, 99)) {
                return ((StoragePrimitive) this.tagMap.get(key)).getDouble();
            }
        } catch (ClassCastException ignored) {
        }

        return 0.0D;
    }

    /**
     * Retrieves a string value using the specified key, or an empty string if no such key was stored.
     */
    public String getString(String key) {
        try {
            if (this.hasKey(key, 8)) {
                return this.tagMap.get(key).getString();
            }
        } catch (ClassCastException ignored) {
        }
        return "";
    }

    public String getValue(String key) {
        try {
            if (this.hasKey(key)) {
                return fetchValue(tagMap.get(key));
            }
        } catch (ClassCastException ignored) {
        }
        return "";
    }

    private String fetchValue(StorageBase base) {
        if (base instanceof StorageTagByte) {
            byte tagByte = ((StorageTagByte) base).getByte();
            if ((tagByte == 0) || (tagByte == 1))
                return String.valueOf(tagByte == 1);
            return String.valueOf(tagByte);
        }
        if (base instanceof StorageTagByteArray)
            return Arrays.toString(((StorageTagByteArray) base).getByteArray());
        if (base instanceof StorageTagDouble)
            return String.valueOf(((StorageTagDouble) base).getDouble());
        if (base instanceof StorageTagFloat)
            return String.valueOf(((StorageTagFloat) base).getFloat());
        if (base instanceof StorageTagInt)
            return String.valueOf(((StorageTagInt) base).getInt());
        if (base instanceof StorageTagIntArray)
            return Arrays.toString(((StorageTagIntArray) base).getIntArray());
        if (base instanceof StorageTagLong)
            return String.valueOf(((StorageTagLong) base).getLong());
        if (base instanceof StorageTagShort)
            return String.valueOf(((StorageTagShort) base).getShort());
        if (base instanceof StorageTagString)
            return String.valueOf(base.getString());
        if (base instanceof StorageTagList) {
            StorageTagList list = (StorageTagList) base;
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < list.tagCount(); i++) {
                builder.append(fetchValue(list.get(i)));
            }
            return builder.toString();
        }

        return base.getString();
    }

    /**
     * Retrieves a byte array using the specified key, or a zero-length array if no such key was stored.
     */
    public byte[] getByteArray(String key) {
        try {
            if (this.hasKey(key, 7)) {
                return ((StorageTagByteArray) this.tagMap.get(key)).getByteArray();
            }
        } catch (ClassCastException ignored) {
        }

        return new byte[0];
    }

    /**
     * Retrieves an int array using the specified key, or a zero-length array if no such key was stored.
     */
    public int[] getIntArray(String key) {
        try {
            if (this.hasKey(key, 11)) {
                return ((StorageTagIntArray) this.tagMap.get(key)).getIntArray();
            }
        } catch (ClassCastException ignored) {
        }

        return new int[0];
    }

    /**
     * Create a crash report which indicates a NBT read error.
     */

    /**
     * Retrieves a NBTTagCompound subtag matching the specified key, or a new empty NBTTagCompound if no such key was
     * stored.
     */
    public StorageTagCompound getCompoundTag(String key) {
        try {
            if (this.hasKey(key, 10)) {
                return (StorageTagCompound) this.tagMap.get(key);
            }
        } catch (ClassCastException ignored) {
        }

        return new StorageTagCompound();
    }

    /**
     * Gets the NBTTagList object with the given name.
     */
    public StorageTagList getTagList(String key, int type) {
        try {
            if (this.getTagId(key) == 9) {
                StorageTagList nbttaglist = (StorageTagList) this.tagMap.get(key);

                if (!nbttaglist.hasNoTags() && nbttaglist.getTagType() != type) {
                    return new StorageTagList();
                }

                return nbttaglist;
            }
        } catch (ClassCastException ignored) {
        }

        return new StorageTagList();
    }

    /**
     * Retrieves a boolean value using the specified key, or false if no such key was stored. This uses the getByte
     * method.
     */
    public boolean getBoolean(String key) {
        try {
            if (this.hasKey(key, 99)) {
                return (((StorageTagByte)this.tagMap.get(key)).getByte() == 1);
            }
        } catch (ClassCastException ignored) {}
        return false;
    }

    /**
     * Remove the specified tag.
     */
    public void removeTag(String key) {
        this.tagMap.remove(key);
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder("{");
        Collection<String> collection = this.tagMap.keySet();

        if (LOGGER.isDebugEnabled()) {
            List<String> list = Lists.newArrayList(this.tagMap.keySet());
            Collections.sort(list);
            collection = list;
        }

        for (String s : collection) {
            if (stringbuilder.length() != 1) {
                stringbuilder.append(',');
            }

            stringbuilder.append(match(s)).append(':').append(this.tagMap.get(s));
        }

        return stringbuilder.append('}').toString();
    }

    /**
     * Return whether this compound has no tags.
     */
    public boolean hasNoTags() {
        return this.tagMap.isEmpty();
    }

    /**
     * Creates a clone of the tag.
     */
    public StorageTagCompound copy() {
        StorageTagCompound nbttagcompound = new StorageTagCompound();

        for (String s : this.tagMap.keySet()) {
            nbttagcompound.setTag(s, this.tagMap.get(s).copy());
        }

        return nbttagcompound;
    }

    public boolean equals(Object instance) {
        return super.equals(instance) && Objects.equals(this.tagMap.entrySet(), ((StorageTagCompound) instance).tagMap.entrySet());
    }

    public int hashCode() {
        return super.hashCode() ^ this.tagMap.hashCode();
    }

    /**
     * Merges this NBTTagCompound with the given compound. Any sub-compounds are merged using the same methods, other
     * types of tags are overwritten from the given compound.
     */
    public void merge(StorageTagCompound other) {
        for (String s : other.tagMap.keySet()) {
            StorageBase nbtbase = other.tagMap.get(s);

            if (nbtbase.getId() == 10) {
                if (this.hasKey(s, 10)) {
                    StorageTagCompound nbttagcompound = this.getCompoundTag(s);
                    nbttagcompound.merge((StorageTagCompound) nbtbase);
                } else {
                    this.setTag(s, nbtbase.copy());
                }
            } else {
                this.setTag(s, nbtbase.copy());
            }
        }
    }
}
