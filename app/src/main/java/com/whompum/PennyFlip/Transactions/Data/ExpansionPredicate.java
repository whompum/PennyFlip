package com.whompum.PennyFlip.Transactions.Data;

//Determines whether to auto expand adapter items
public interface ExpansionPredicate {
    boolean expand(final long startOfDay, final int position);
}
