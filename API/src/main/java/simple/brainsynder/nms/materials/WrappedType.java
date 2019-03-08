package simple.brainsynder.nms.materials;

public interface WrappedType {
    String getName ();

    @Deprecated
    default String getLegacy () {
        return getName();
    }

    default int getData (boolean original) {
        return getData();
    }
    default int getData () {
        return getData(true);
    }
}
