package com.whompum.PennyFlip.Money.Source.Sorting;

import com.whompum.PennyFlip.Money.Source.Source;

import java.util.Comparator;

public class TotalAscendingComparator implements Comparator<Source> {
    @Override
    public int compare(Source o1, Source o2) {
        return (int)(o1.getPennies() - o2.getPennies());
    }
}
