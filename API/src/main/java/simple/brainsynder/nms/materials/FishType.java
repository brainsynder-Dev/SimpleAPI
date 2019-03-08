package simple.brainsynder.nms.materials;

public enum FishType implements WrappedType {
    REGULAR,
    SALMON,
    CLOWNFISH,
    PUFFERFISH;


    @Override
    public String getName() {
        return name();
    }

    @Override
    public int getData() {
        return ordinal();
    }
}
