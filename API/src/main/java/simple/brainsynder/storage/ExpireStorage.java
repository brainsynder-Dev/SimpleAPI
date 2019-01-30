package simple.brainsynder.storage;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class ExpireStorage<E> implements IExpireStorage<E> {
    private ExpireHashMap<Integer, E> map = new ExpireHashMap<>();
    private int size = 0;
    private int current = -1;

    public ExpireStorage() {
        size = 0;
    }
    
    public ExpireStorage(Collection<E> list) {
        size = 0;
        for (E v : list) {
            add(v);
        }
    }
    
    public ExpireStorage(List<E> list) {
        size = 0;
        for (E v : list) {
            add(v);
        }
    }

    public ExpireStorage(E[] list) {
        size = 0;
        for (E v : list) {
            add(v);
        }
    }

    @Override public int getSize() {
        return map.values().size();
    }

    @Override public void add(E var) {
        add(var, 5, TimeUnit.MINUTES);
    }
    
    @Override public void add(E var, long time, TimeUnit unit) {
        map.put(size, var, time, unit);
        size++;
    }

    @Override public List<E> limitList(int var) {
        List<E> list = new ArrayList<>();
        if (var <= 0)
            return list;
        if (var > getSize())
            var = getSize();
        for (int i = 0; i < var; i++) {
            list.add(this.get(i));
        }

        return list;
    }

    @Override public E next() {
        current = (current + 1);
        return get(current);
    }

    @Override public E get(int var) {
        try {
            return map.get(var);
        }catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override public boolean contains(E var) {
        return map.containsValue(var);
    }

    @Override public boolean hasNext() {
        int con = current;
        con = (con + 1);
        return (map.containsKey(con));
    }

    @Override public boolean isEmpty() {
        return map.asMap().isEmpty();
    }

    @Override public void remove(E var) {
        for (int id : map.asMap().keySet()) {
            if (map.asMap().get(id) == var){
                map.removeKey(id);
            }
        }
    }

    @Override public Iterator<E> iterator() {
        return map.asMap().values().iterator();
    }

    @Override public IStorage<E> copy() {
        return new ExpireStorage(map.asMap().values());
    }

    @Override public E[] toArray() {
        Object[] array = new Object[getSize()];
        int i = 0;
        for (E e : map.asMap().values()) {
            array[i] = e;
            i++;
        }
        return (E[]) array;
    }

    public <T> T[] toArray(T[] t) {
        return map.asMap().values().toArray(t);
    }

    @Override public ArrayList<E> toArrayList() {
        ArrayList<E> list = new ArrayList<>(map.asMap().values());
        return list;
    }

    @Override public Set<E> toSet() {
        Set<E> set = new HashSet<>(map.asMap().values());
        return set;
    }
}
