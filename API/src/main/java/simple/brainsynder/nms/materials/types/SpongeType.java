package simple.brainsynder.nms.materials.types;

import simple.brainsynder.nms.materials.WrappedType;

public enum SpongeType implements WrappedType {
    SPONGE,
    WET_SPONGE;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public int getData() {
        return ordinal();
    }
}
