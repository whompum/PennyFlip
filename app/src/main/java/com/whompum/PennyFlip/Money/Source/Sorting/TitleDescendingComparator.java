package com.whompum.PennyFlip.Money.Source.Sorting;

import com.whompum.PennyFlip.Money.Source.Source;

import java.util.Comparator;

public class TitleDescendingComparator implements Comparator<Source> {
    @Override
    public int compare(Source o1, Source o2) {
        return o2.getTitle().compareTo(o1.getTitle());
    }
}
