package simple.brainsynder.nms.materials;

public enum StoneSlabType implements WrappedType {
    STONE,
    SANDSTONE,
    WOODEN,
    COBBLESTONE,
    BRICK,
    STONE_BRICK,
    NETHER_BRICK,
    QUARTZ;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public int getData() {
        return ordinal();
    }
}
