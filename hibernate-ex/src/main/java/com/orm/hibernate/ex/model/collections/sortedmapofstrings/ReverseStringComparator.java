package com.orm.hibernate.ex.model.collections.sortedmapofstrings;

import java.util.Comparator;

public class ReverseStringComparator implements Comparator<String> {
    @Override
    public int compare(String a, String b) {
        return b.compareTo(a); // Inverse sorting
    }
}