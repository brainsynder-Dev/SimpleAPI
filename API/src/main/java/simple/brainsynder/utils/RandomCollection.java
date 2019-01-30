package simple.brainsynder.utils;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class RandomCollection<E> {
    private final NavigableMap<Double, E> map;
    private final Random random;
    private double total;

    public RandomCollection() {
        this(new Random());
    }

    public RandomCollection(Random var1) {
        this.map = new TreeMap();
        this.total = 0.0D;
        this.random = var1;
    }

    public void add(double percent, E value) {
        if (percent > 0.0D) {
            this.total += percent;
            this.map.put(this.total, value);
        }
    }

    public E next() {
        double var1 = this.random.nextDouble() * this.total;
        return this.map.ceilingEntry(var1).getValue();
    }
}