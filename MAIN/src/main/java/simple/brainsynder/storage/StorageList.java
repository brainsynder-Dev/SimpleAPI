package simple.brainsynder.storage;

import java.util.*;

public class StorageList<E> implements IStorage<E> {
    private ArrayList<E> list = new ArrayList<>();
    private int size = 0;
    private int current = -1;

    public StorageList() {
        size = 0;
    }
    
    public StorageList(Collection<E> list) {
        size = 0;
        for (E v : list) {
            add(v);
        }
    }
    
    public StorageList(List<E> list) {
        size = 0;
        for (E v : list) {
            add(v);
        }
    }

    public StorageList(E[] list) {
        size = 0;
        for (E v : list) {
            add(v);
        }
    }

    @Override public int getSize() {
        return list.size();
    }

    @Override public void add(E var) {
        list.add(var);
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
            return this.list.get(var);
        }catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override public boolean contains(E var) {
        return list.contains(var);
    }

    @Override public boolean hasNext() {
        int con = current;
        con = (con + 1);
        return (get(con) != null);
    }

    @Override public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override public void remove(E var) {
        list.remove(var);
    }

    @Override public Iterator<E> iterator() {
        return list.iterator();
    }

    @Override public IStorage<E> copy() {
        return new StorageList<>(list);
    }

    @Override public E[] toArray() {
        Object[] array = new Object[getSize()];
        int i = 0;
        for (E e : list) {
            array[i] = e;
            i++;
        }
        return (E[]) array;
    }

    public <T> T[] toArray(T[] t) {
        return list.toArray(t);
    }

    @Override public ArrayList<E> toArrayList() {
        return list;
    }

    @Override public Set<E> toSet() {
        Set<E> set = new HashSet<>(list);
        return set;
    }
}
