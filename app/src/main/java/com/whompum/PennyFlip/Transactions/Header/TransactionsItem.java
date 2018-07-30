package com.whompum.PennyFlip.Transactions.Header;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Transaction.Transaction;

/**
 * Created by bryan on 1/7/2018.
 */

public class TransactionsItem implements HeaderItem {

    private Transaction transaction;

    public TransactionsItem(@NonNull Transaction transaction){
        this.transaction = transaction;
    }

    public Transaction getTransactions(){
        return transaction;
    }


}
