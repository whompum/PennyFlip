package com.whompum.PennyFlip.Money;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.whompum.PennyFlip.Money.Source.NewSourceTotalConstraintException;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Source.SourceDao;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Money.Transaction.TransactionDAO;
import com.whompum.PennyFlip.Money.Transaction.TransactionType;
import com.whompum.PennyFlip.Money.MoneyThread.MoneyThreadOperation;
import com.whompum.PennyFlip.Money.Wallet.Wallet;
import com.whompum.PennyFlip.Money.Wallet.WalletDao;

/**
 * Writes data to a Room Database
 */
public class RoomMoneyWriter implements MoneyWriter{

    //DAO for transaction objects
    private TransactionDAO transactionsDao;

    //DAO for Source objects
    private SourceDao sourceDao;

    //DAO for Wallet objects
    private WalletDao walletDao;

    public RoomMoneyWriter(@NonNull final Context context){
        //Fetch the database containing the DAO's
        final MoneyDatabase database =
                Room.databaseBuilder(context, MoneyDatabase.class, MoneyDatabase.NAME).build();

        //Initialize all our DAO's using the MoneyDatabase implementation
        transactionsDao = database.getTransactionAccessor();
        sourceDao = database.getSourceAccessor();
        walletDao = database.getWalletAccessor();

    }


    @Override
    public synchronized void saveTransaction(@NonNull final Transaction transaction) {
        //Save a transaction using an Operation object dedicated to Transaction saving

        new MoneyThread()
                .doInBackground(new SaveTransactionOperation(transaction));

    }

    @Override
    public synchronized void saveSource(@NonNull final Source source) {

        if(source.getPennies() > 0)
            throw new NewSourceTotalConstraintException();

        new MoneyThread().doInBackground(new MoneyThreadOperation() {
            @Override
            public void doOperation() {
                sourceDao.insert(source);
            }
        });
    }

    @Override
    public synchronized void deleteSource(@NonNull final String sourceId) {
        new MoneyThread().doInBackground(new MoneyThreadOperation() {
            @Override
            public void doOperation() {
                sourceDao.delete(sourceId);
            }
        });
    }


    /**
     * Utility operation that aggregates operations during a
     * {@link Transaction} write
     */
    private class SaveTransactionOperation extends MoneyThreadOperation {

        private Transaction transaction;

        private SaveTransactionOperation(@NonNull final Transaction transaction){
            this.transaction = transaction;
        }

        @WorkerThread
        @Override
        public synchronized void doOperation() {
            //After saving, update both the wallet and the Source
            transactionsDao.insert(transaction);
            sourceDao.addAmount(transaction);

                /*
                   Fetch the original wallet total, and then determine if the new wallet total
                   based on the amount and transaction type of the Transaction object.
                 */
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

}
