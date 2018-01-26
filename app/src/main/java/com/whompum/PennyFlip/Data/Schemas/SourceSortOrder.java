package com.whompum.PennyFlip.Data.Schemas;

import android.support.annotation.NonNull;

/**
 * Created by bryan on 1/25/2018.
 */

public class SourceSortOrder {

     //[columnName] + "ASC|DESC"

    private static final String TITLE = SourceSchema.SourceTable.COL_TITLE;
    private static final String LAST_UPDATE = SourceSchema.SourceTable.COL_LAST_UPDATE;
    private static final  String CREATION_DATE = SourceSchema.SourceTable.COL_CREATION_DATE;
    private static final String TOTAL = SourceSchema.SourceTable.COL_TOTAL;

    private static final String ASC = " ASC";
    private static final String DESC = " DESC";


    public static final SourceSortOrder TITLE_HIGH_TO_LOW = new SourceSortOrder(TITLE + DESC); //CHECK
    public static final SourceSortOrder TITLE_LOW_TO_HIGH = new SourceSortOrder(TITLE + ASC); //CHECK

    public static final SourceSortOrder LAST_UPDATE_HIGH_TO_LOW = new SourceSortOrder(LAST_UPDATE + DESC); // CHECK
    public static final SourceSortOrder LAST_UPDATE_LOW_TO_HIGH = new SourceSortOrder(LAST_UPDATE + ASC); // CHECK

    public static final SourceSortOrder CREATION_DATE_HIGH_TO_LOW = new SourceSortOrder(CREATION_DATE + DESC); // CHECK
    public static final SourceSortOrder CREATION_DATE_LOW_TO_HIGH = new SourceSortOrder(CREATION_DATE + ASC); // CHECK

    public static final SourceSortOrder TOTAL_HIGH_TO_LOW = new SourceSortOrder(TOTAL + DESC); //WHY DOESN"T THIS WORKEKFDFDSFSD!!
    public static final SourceSortOrder TOTAL_LOW_TO_HIGH = new SourceSortOrder(TOTAL + ASC); //WHY WONT THIS WORK!



    private String sortOrder;

    private SourceSortOrder(@NonNull String sortOrder){
        this.sortOrder = sortOrder;
    }

    public String getSortOrder(){
        return sortOrder;
    }

/**
 * -Title [a-z]
 --High to Low
 --Low to High

 -Last Update [millis] (DEFAULT)
 --Most Recent to Least Recent (DEFAULT)
 --Least Recent to Most Recent

 -Creation Date [millis]
 --Oldest to Newest
 --Newest to Oldest

 -Total [pennies]
 --Most Total to Least Total
 --Least Total to Most Total

 */
}














