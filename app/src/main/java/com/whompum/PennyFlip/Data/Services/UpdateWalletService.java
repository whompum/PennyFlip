package com.whompum.PennyFlip.Data.Services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.Transaction.Models.TransactionType;

/**
 * Updates the Wallet total
 */

public class UpdateWalletService extends IntentService {

    public static final String name = "UpdateWallet";

    public static final String TRANSACTION_TYPE_KEY = "transactionType.ky";
    public static final String TRANSACTION_AMOUNT_KEY = "transactionAmount.ky";

    public UpdateWalletService(){
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if(intent == null)
            return;

        final int transactionType = intent.getIntExtra(TRANSACTION_TYPE_KEY, 0);
        final long amount = intent.getLongExtra(TRANSACTION_AMOUNT_KEY, 0L);

        if(transactionType == TransactionType.ADD)
            updateAddColumn(amount);

        else if(transactionType == TransactionType.SPEND)
            updateSpendColumn(amount);

    }

    private void updateAddColumn(final long amount){

    }

    private void updateSpendColumn(final long amount){

    }


}
