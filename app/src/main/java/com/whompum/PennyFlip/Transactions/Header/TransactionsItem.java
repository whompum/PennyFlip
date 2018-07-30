package com.whompum.PennyFlip.Transactions.Models;

import android.support.annotation.NonNull;

/**
 * Created by bryan on 1/7/2018.
 */

public class TransactionsItem implements HeaderItem {

    private Transactions transactions;

    public TransactionsItem(@NonNull Transactions transactions){
        this.transactions = transactions;
    }

    public Transactions getTransactions(){
        return transactions;
    }


}
