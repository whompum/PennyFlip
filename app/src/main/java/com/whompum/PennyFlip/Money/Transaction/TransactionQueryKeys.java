package com.whompum.PennyFlip.Money.Transaction;

import java.util.HashSet;

/**
 * {@link Transaction} parameter key class
 * That allows the user to choose one of the following keys
 */
public class TransactionQueryKeys{

    public static final int TIMERANGE = 1;
    public static final int SOURCE_ID = 2;
    public static final int PENNY_VALUE = 3;
    public static final int TRANSACTION_TYPE = 4;
    public static final int TRANSACTION_ID = 5;

    public static final HashSet<Integer> KEYS = new HashSet<>();

    static{

        KEYS.add(TIMERANGE);
        KEYS.add(SOURCE_ID);
        KEYS.add(PENNY_VALUE);
        KEYS.add(TRANSACTION_TYPE);
        KEYS.add(TRANSACTION_ID);

    }

}
