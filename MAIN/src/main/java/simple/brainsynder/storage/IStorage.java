package simple.brainsynder.storage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public interface IStorage<E> {

    int getSize ();

    void add (E var);

    List<E> limitList(int var);

    E next ();

    E get (int var);

    boolean contains (E var);

    boolean hasNext ();

    boolean isEmpty ();

    void remove (E var);

    Iterator<E> iterator();

    IStorage<E> copy();

    E[] toArray ();

    <T> T[] toArray(T[] t);

    ArrayList<E> toArrayList ();

    Set<E> toSet ();
}
