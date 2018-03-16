package simple.brainsynder.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Lore<T> implements Collection<T> {
    private List<T> list = new ArrayList<>();

    @Override
    public int size() {
        return list.size ();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean add(T t) {
        return list.add(t);
    }

    public void add(int i, T t) {
        list.add (i, t);
    }

    public T get (int i) {
        return list.get (i);
    }

    @Override
    public boolean remove(Object o) {
        return list.remove (o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return list.addAll (c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return list.removeAll (c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public void clear() {
        list.clear ();
    }

    @Override
    public boolean equals(Object o) {
        return list.equals(o);
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }

    public Lore<T> fromList (List<T> list) {
        this.list = list;
        return this;
    }

    public List<T> toList () {
        return list;
    }
}
