package com.whompum.PennyFlip.Money.Source.Sorting;


import com.whompum.PennyFlip.Money.Source.Source;

import java.util.Comparator;

public class TitleAscendingComparator implements Comparator<Source> {
    @Override
    public int compare(Source o1, Source o2) {
        return o1.getTitle().compareTo(o2.getTitle());
    }
}
