package simple.brainsynder.nms.materials;

public enum FishType {
    REGULAR,
    SALMON,
    CLOWNFISH,
    PUFFERFISH;


    public String getName() {
        return name();
    }

    public int getData() {
        return ordinal();
    }
}
