package simple.brainsynder.nms.materials.types;

import simple.brainsynder.nms.materials.WrappedType;

public enum MonsterEggType implements WrappedType {
    STONE,
    COBBLESTONE,
    STONE_BRICKS,
    MOSSY_STONE_BRICKS,
    CRACKED_STONE_BRICKS,
    CHISELED_STONE_BRICKS;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public int getData() {
        return ordinal();
    }
}
