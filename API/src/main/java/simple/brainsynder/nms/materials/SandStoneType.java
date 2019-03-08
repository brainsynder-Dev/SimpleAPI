package simple.brainsynder.nms.materials;

public enum SandStoneType {
    REGULAR,
    CHISELED,
    CUT("SMOOTH");

    private String legacy;
    SandStoneType () {
        legacy = name();
    }
    SandStoneType (String legacy) {
        this.legacy = legacy;
    }

    public String getName() {
        return name();
    }

    public int getData() {
        return ordinal();
    }
}
