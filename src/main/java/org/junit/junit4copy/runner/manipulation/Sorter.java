package org.junit.junit4copy.runner.manipulation;

import org.junit.junit4copy.runner.Description;

import java.util.Comparator;

/**
 * Created by qinjw on 2017/10/15.
 */
public class Sorter implements Comparator<Description> {
    public static final Sorter NULL = new Sorter(new Comparator<Description>() {
        @Override
        public int compare(Description o1, Description o2) {
            return 0;
        }
    });

    private final Comparator<Description> comparator;

    public Sorter(Comparator<Description> comparator) {
        this.comparator = comparator;
    }

    public void apply(Object object) {
        if (object instanceof Sortable) {
            Sortable sortable = (Sortable) object;
            sortable.sort(this);
        }
    }

    @Override
    public int compare(Description o1, Description o2) {
        return 0;
    }
}
