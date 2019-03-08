package simple.brainsynder.nms.materials.types;

import simple.brainsynder.nms.materials.WrappedType;

public enum TallPlantType implements WrappedType {
    SUNFLOWER,
    LILAC,
    TALL_GRASS,
    LARGE_FERN,
    ROSE_BUSH,
    PEONY;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public int getData() {
        return ordinal();
    }
}
