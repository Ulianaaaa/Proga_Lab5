package common.models;

import java.util.Comparator;

public class FlatComparator implements Comparator<Flat> {
    @Override
    public int compare(Flat f1, Flat f2) {
        return Integer.compare(f1.getId(), f2.getId()); // Сортировка по id
    }
}