package simple.brainsynder.nbt;

abstract class StoragePrimitive extends StorageBase {
    public abstract long getLong();

    public abstract int getInt();

    public abstract short getShort();

    public abstract byte getByte();

    public abstract double getDouble();

    public abstract float getFloat();
}
