package com.whompum.PennyFlip.Money.Transaction;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Source.SourceDao;
import com.whompum.PennyFlip.Money.Wallet.Wallet;
import com.whompum.PennyFlip.Money.Wallet.WalletDao;
import com.whompum.PennyFlip.Money.Writes.RoomMoneyWriter;

public class RoomTransactionWriter extends RoomMoneyWriter<Transaction> {

    private SourceDao sourceDao;
    private WalletDao walletDao;

    public RoomTransactionWriter(@NonNull MoneyDatabase db) {
        super(db.getTransactionAccessor());
        sourceDao = db.getSourceAccessor();
        walletDao = db.getWalletAccessor();
    }

    @Override
    public void internalSave(@NonNull Transaction transaction) {
        //Save to Transaction
        //Update source total
        //Update Wallet

        super.internalSave(transaction); //Save the Transaction item first

        if(sourceDao != null)
            sourceDao.addAmount(transaction);


        //TODO move `walletDao.fetch()` to a query object and handle it there
        long newWalletTotal = walletDao.fetch().getValue();

        if(transaction.getTransactionType() == TransactionType.ADD)
            newWalletTotal += transaction.getAmount();

        else if(transaction.getTransactionType() == TransactionType.SPEND) {
            //Check for negative values because we don't use sub-zero values for the wallet.
            long newValue = newWalletTotal - transaction.getAmount();
            newWalletTotal = (newValue > 0) ? newValue : 0L;
        }

        walletDao.update(new Wallet(newWalletTotal));


    }
}
