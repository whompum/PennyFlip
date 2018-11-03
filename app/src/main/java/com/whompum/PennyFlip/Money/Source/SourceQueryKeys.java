package com.whompum.PennyFlip.Money.Source;

import java.util.HashSet;

public class SourceQueryKeys{

    public static final int TITLE = 1; //By title only
    public static final int LIKE_TITLE = 2;
    public static final int TIMERANGE = 3; //Search by timerange
    public static final int TRANSACTION_TYPE = 4; //Search by type of Source it is
    public static final int AMOUNT = 5; //Search by type of Source it is

    public static final HashSet<Integer> KEYS = new HashSet<>(5);

    static {

        KEYS.add( TITLE );
        KEYS.add( LIKE_TITLE );
        KEYS.add( TIMERANGE );
        KEYS.add( TRANSACTION_TYPE );
        KEYS.add( AMOUNT );

    }

}
