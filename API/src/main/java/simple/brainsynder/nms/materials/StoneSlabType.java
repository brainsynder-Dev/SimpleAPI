package simple.brainsynder.nms.materials;

public enum StoneSlabType {
    STONE,
    SANDSTONE,
    WOODEN,
    COBBLESTONE,
    BRICK,
    STONE_BRICK,
    NETHER_BRICK,
    QUARTZ;

    public String getName() {
        return name();
    }

    public int getData() {
        return ordinal();
    }
}
