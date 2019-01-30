package simple.brainsynder.storage;

import java.util.concurrent.TimeUnit;

public interface IExpireStorage<E> extends IStorage<E> {
    void add(E var, long time, TimeUnit unit);
}
