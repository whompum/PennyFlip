package com.whompum.PennyFlip.Money.Source;

import android.support.annotation.NonNull;
import android.util.Log;

import com.whompum.PennyFlip.Money.Source.Sorting.CreationDateAscendingComparator;
import com.whompum.PennyFlip.Money.Source.Sorting.CreationDateDescendingComparator;
import com.whompum.PennyFlip.Money.Source.Sorting.TitleAscendingComparator;
import com.whompum.PennyFlip.Money.Source.Sorting.TotalAscendingComparator;
import com.whompum.PennyFlip.Money.Source.Sorting.TotalDescendingComparator;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Source.Sorting.TitleDescendingComparator;

import java.util.Comparator;

/**
 * Created by bryan on 1/25/2018.
 */

public class SourceSortOrder {

    public static final int SORT_TITLE_DESC = 0;
    public static final int SORT_TITLE_ASC = 1;

    public static final int SORT_LAST_UPDATE_DESC = 2;
    public static final int SORT_LAST_UPDATE_ASC = 3;

    public static final int SORT_CREATION_DATE_DESC = 4;
    public static final int SORT_CREATION_DATE_ASC = 5;

    public static final int SORT_TOTAL_DESC = 6;
    public static final int SORT_TOTAL_ASC = 7;

    private int sortOrder;

    public SourceSortOrder(@NonNull int sortOrder){
        this.sortOrder = sortOrder;
    }

    public int getSortOrder(){
        return sortOrder;
    }

    public Comparator<Source> resolveSorter(){

        Log.i("SORTING", "SORT ORDER NUM: " + sortOrder);

        switch(sortOrder){
            case SORT_TITLE_DESC: return new TitleDescendingComparator();
            case SORT_TITLE_ASC: return new TitleAscendingComparator();
            case SORT_CREATION_DATE_DESC: return new CreationDateDescendingComparator();
            case SORT_CREATION_DATE_ASC: return new CreationDateAscendingComparator();
            case SORT_TOTAL_DESC: return new TotalDescendingComparator();
            case SORT_TOTAL_ASC: return new TotalAscendingComparator();
        }
        return new CreationDateDescendingComparator();
    }
}














