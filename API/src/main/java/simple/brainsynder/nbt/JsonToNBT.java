package simple.brainsynder.nbt;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.regex.Pattern;

public class JsonToNBT {
    private static final Pattern PATTERN = Pattern.compile("[-+]?(?:[0-9]+[.]|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?", 2);
    private static final Pattern PATTERN1 = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?d", 2);
    private static final Pattern PATTERN2 = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?f", 2);
    private static final Pattern PATTERN3 = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)b", 2);
    private static final Pattern PATTERN4 = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)l", 2);
    private static final Pattern PATTERN5 = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)s", 2);
    private static final Pattern PATTERN6 = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)");
    private final String text;
    private int place;

    private JsonToNBT(String s) {
        this.text = s;
    }

    public static StorageTagCompound getTagFromJson(String jsonString) throws NBTException {
        return (new JsonToNBT(jsonString)).toCompound();
    }

    private StorageTagCompound toCompound() throws NBTException {
        StorageTagCompound nbttagcompound = this.excapeFormat();
        this.movePlace();

        if (this.notValid()) {
            ++this.place;
            throw this.throwError("Trailing data found");
        } else {
            return nbttagcompound;
        }
    }

    private String formatString() throws NBTException {
        this.movePlace();

        if (!this.notValid()) {
            throw this.throwError("Expected key");
        } else {
            return this.getFirstChar() == '"' ? this.translate() : this.updatePlace();
        }
    }

    private NBTException throwError(String s) {
        return new NBTException(s, this.text, this.place);
    }

    protected StorageBase c() throws NBTException {
        this.movePlace();

        if (this.getFirstChar() == '"') {
            return new StorageTagString(this.translate());
        } else {
            String s = this.updatePlace();

            if (s.isEmpty()) {
                throw this.throwError("Expected value");
            } else {
                return this.c(s);
            }
        }
    }

    private StorageBase c(String value) {
        try {
            if (PATTERN2.matcher(value).matches()) {
                return new StorageTagFloat(Float.parseFloat(value.substring(0, value.length() - 1)));
            }

            if (PATTERN3.matcher(value).matches()) {
                return new StorageTagByte(Byte.parseByte(value.substring(0, value.length() - 1)));
            }

            if (PATTERN4.matcher(value).matches()) {
                return new StorageTagLong(Long.parseLong(value.substring(0, value.length() - 1)));
            }

            if (PATTERN5.matcher(value).matches()) {
                return new StorageTagShort(Short.parseShort(value.substring(0, value.length() - 1)));
            }

            if (PATTERN6.matcher(value).matches()) {
                return new StorageTagInt(Integer.parseInt(value));
            }

            if (PATTERN1.matcher(value).matches()) {
                return new StorageTagDouble(Double.parseDouble(value.substring(0, value.length() - 1)));
            }

            if (PATTERN.matcher(value).matches()) {
                return new StorageTagDouble(Double.parseDouble(value));
            }
        } catch (NumberFormatException ignored) {}

        return new StorageTagString(value);
    }

    private String translate() throws NBTException {
        int i = ++this.place;
        StringBuilder stringbuilder = null;
        boolean flag = false;

        while (this.notValid()) {
            char c0 = this.getCurrentChar();

            if (flag) {
                if (c0 != '\\' && c0 != '"') {
                    throw this.throwError("Invalid escape of '" + c0 + "'");
                }

                flag = false;
            } else {
                if (c0 == '\\') {
                    flag = true;

                    if (stringbuilder == null) {
                        stringbuilder = new StringBuilder(this.text.substring(i, this.place - 1));
                    }

                    continue;
                }

                if (c0 == '"') {
                    return stringbuilder == null ? this.text.substring(i, this.place - 1) : stringbuilder.toString();
                }
            }

            if (stringbuilder != null) {
                stringbuilder.append(c0);
            }
        }

        throw this.throwError("Missing termination quote");
    }

    private String updatePlace() {
        int i;

        for (i = this.place; this.notValid() && this.checkChar(this.getFirstChar()); ++this.place) {
        }

        return this.text.substring(i, this.place);
    }

    private StorageBase excape() throws NBTException {
        this.movePlace();

        if (!this.notValid()) {
            throw this.throwError("Expected value");
        } else {
            char c0 = this.getFirstChar();

            if (c0 == '{') {
                return this.excapeFormat();
            } else {
                return c0 == '[' ? this.fetchList() : this.c();
            }
        }
    }

    private StorageBase fetchList() throws NBTException {
        return this.checkLength(2) && this.getChar(1) != '"' && this.getChar(2) == ';' ? this.arrays() : this.list();
    }

    private StorageTagCompound excapeFormat() throws NBTException {
        this.validateChar('{');
        StorageTagCompound nbttagcompound = new StorageTagCompound();
        this.movePlace();

        while (this.notValid() && this.getFirstChar() != '}') {
            String s = this.formatString();

            if (s.isEmpty()) {
                throw this.throwError("Expected non-empty key");
            }

            this.validateChar(':');
            nbttagcompound.setTag(s, this.excape());

            if (!this.m()) {
                break;
            }

            if (!this.notValid()) {
                throw this.throwError("Expected key");
            }
        }

        this.validateChar('}');
        return nbttagcompound;
    }

    private StorageBase list() throws NBTException {
        this.validateChar('[');
        this.movePlace();

        if (!this.notValid()) {
            throw this.throwError("Expected value");
        } else {
            StorageTagList nbttaglist = new StorageTagList();
            int i = -1;

            while (this.getFirstChar() != ']') {
                StorageBase nbtbase = this.excape();
                int j = nbtbase.getId();

                if (i < 0) {
                    i = j;
                } else if (j != i) {
                    throw this.throwError("Unable to insert " + StorageBase.getName(j) + " into ListTag of type " + StorageBase.getName(i));
                }

                nbttaglist.appendTag(nbtbase);

                if (!this.m()) {
                    break;
                }

                if (!this.notValid()) {
                    throw this.throwError("Expected value");
                }
            }

            this.validateChar(']');
            return nbttaglist;
        }
    }

    private StorageBase arrays() throws NBTException {
        this.validateChar('[');
        char c0 = this.getCurrentChar();
        this.getCurrentChar();
        this.movePlace();

        if (!this.notValid()) {
            throw this.throwError("Expected value");
        } else if (c0 == 'B') {
            return new StorageTagByteArray(this.a((byte) 7, (byte) 1));
        } else if (c0 == 'L') {
            return new StorageTagLongArray(this.a((byte) 12, (byte) 4));
        } else if (c0 == 'I') {
            return new StorageTagIntArray(this.a((byte) 11, (byte) 3));
        } else {
            throw this.throwError("Invalid array type '" + c0 + "' found");
        }
    }

    private <T extends Number> List<T> a(byte b, byte b1) throws NBTException {
        List<T> list = Lists.newArrayList();

        while (true) {
            if (this.getFirstChar() != ']') {
                StorageBase nbtbase = this.excape();
                int i = nbtbase.getId();

                if (i != b1) {
                    throw this.throwError("Unable to insert " + StorageBase.getName(i) + " into " + StorageBase.getName(b));
                }

                if (b1 == 1) {
                    list.add((T) Byte.valueOf(((StoragePrimitive) nbtbase).getByte()));
                } else if (b1 == 4) {
                    list.add((T) Long.valueOf(((StoragePrimitive) nbtbase).getLong()));
                } else {
                    list.add((T) Integer.valueOf(((StoragePrimitive) nbtbase).getInt()));
                }

                if (this.m()) {
                    if (!this.notValid()) {
                        throw this.throwError("Expected value");
                    }

                    continue;
                }
            }

            this.validateChar(']');
            return list;
        }
    }

    private void movePlace() {
        while (this.notValid() && Character.isWhitespace(this.getFirstChar())) {
            ++this.place;
        }
    }

    private boolean m() {
        this.movePlace();

        if (this.notValid() && this.getFirstChar() == ',') {
            ++this.place;
            this.movePlace();
            return true;
        } else {
            return false;
        }
    }

    private void validateChar(char c) throws NBTException {
        this.movePlace();
        boolean flag = this.notValid();

        if (flag && this.getFirstChar() == c) {
            ++this.place;
        } else {
            throw new NBTException("Expected '" + c + "' but got '" + (flag ? this.getFirstChar() : "<EOF>") + "'", this.text, this.place + 1);
        }
    }

    private boolean checkChar(char c) {
        return c >= '0' && c <= '9' || c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c == '_' || c == '-' || c == '.' || c == '+';
    }

    private boolean checkLength(int i) {
        return this.place + i < this.text.length();
    }

    private boolean notValid() {
        return this.checkLength(0);
    }

    private char getChar(int i) {
        return this.text.charAt(this.place + i);
    }

    private char getFirstChar() {
        return this.getChar(0);
    }

    private char getCurrentChar() {
        return this.text.charAt(this.place++);
    }
}
