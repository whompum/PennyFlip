package com.whompum.PennyFlip.Money.Statistics;

import java.util.HashSet;

public class StatisticQueryKeys {

    public static final int TRANSACTION_TYPE = 1; //Search by type of Source it is

    public static final HashSet<Integer> KEYS = new HashSet<>(5);

    static {
        KEYS.add( TRANSACTION_TYPE );
    }

}
