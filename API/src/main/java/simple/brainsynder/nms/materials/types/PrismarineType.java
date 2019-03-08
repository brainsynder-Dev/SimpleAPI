package simple.brainsynder.nms.materials.types;

import simple.brainsynder.nms.materials.WrappedType;

public enum PrismarineType implements WrappedType {
    REGULAR,
    BRICKS,
    DARK;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public int getData() {
        return ordinal();
    }
}
