package simple.brainsynder.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObjectPager<T> extends ArrayList<T> {

    private int pageSize;

    public ObjectPager(int pageSize) {
        this(pageSize, new ArrayList<>());
    }

    @SafeVarargs
    public ObjectPager(int pageSize, T... objects) {
        this(pageSize, Arrays.asList(objects));
    }

    public ObjectPager(int pageSize, List<T> objects) {
        this.pageSize = pageSize;
        addAll(objects);
    }

    public int pageSize() {
        return pageSize;
    }

    public int totalPages() {
        return (((int) Math.ceil((double) size() / pageSize)));
    }

    public boolean exists(int page) {
        page = (page - 1);
        return !(page < 0) && page < totalPages();
    }

    public List<T> getPage(int page) {
        page = (page - 1);
        if(page < 0 || page >= totalPages()) throw new IndexOutOfBoundsException("Index: " + page + ", Size: " + totalPages());

        List<T> objects = new ArrayList<>();

        int min = page * pageSize;
        int max = ((page * pageSize) + pageSize);

        if(max > size()) max = size();

        for(int i = min; max > i; i++)
            objects.add(get(i));

        return objects;
    }

}
