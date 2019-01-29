package simple.brainsynder.storage;

public class TriLoc<T> {
    private T x, y, z;

    public TriLoc (T x, T y, T z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public T getX() {
        return x;
    }

    public T getY() {
        return y;
    }

    public T getZ() {
        return z;
    }
}
