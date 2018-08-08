package com.whompum.PennyFlip.Transactions.Adapter;

public interface ExpansionPredicate {

    boolean expand(final long startOfDay, final int position);

}
