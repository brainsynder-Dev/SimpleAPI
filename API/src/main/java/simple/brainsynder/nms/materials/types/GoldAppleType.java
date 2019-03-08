package simple.brainsynder.nms.materials.types;

import simple.brainsynder.nms.materials.WrappedType;

public enum GoldAppleType implements WrappedType {
    REGULAR,
    ENCHANTED;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public int getData() {
        return ordinal();
    }
}
