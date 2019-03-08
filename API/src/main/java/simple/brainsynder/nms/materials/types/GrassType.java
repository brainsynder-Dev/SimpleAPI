package simple.brainsynder.nms.materials.types;

import simple.brainsynder.nms.materials.WrappedType;

public enum GrassType implements WrappedType {
    DEAD,
    NORMAL,
    FERN_LIKE;

    private String legacy = "";
    GrassType() {}
    GrassType(String legacy) {
        this.legacy = legacy;
    }

    @Override
    public String getLegacy() {
        return legacy;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public int getData() {
        return ordinal();
    }
}
