package com.whompum.PennyFlip.Money.Writes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.whompum.PennyFlip.Money.DatabaseUtils;
import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.MoneyThread;
import com.whompum.PennyFlip.Money.Source.NewSourceTotalConstraintException;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Contracts.Source.SourceDao;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Money.Contracts.Transaction.TransactionDao;
import com.whompum.PennyFlip.Money.Transaction.TransactionType;
import com.whompum.PennyFlip.Money.MoneyThread.MoneyThreadOperation;
import com.whompum.PennyFlip.Money.Wallet.Wallet;
import com.whompum.PennyFlip.Money.Contracts.Wallet.WalletDao;

/**
 * Writes data to a Room Database
 */
public class RoomMoneyWriter implements MoneyWriter {


    //DAO for transaction objects
    private TransactionDao transactionsDao;

    //DAO for Source objects
    private SourceDao sourceDao;

    //DAO for Wallet objects
    private WalletDao walletDao;

    public RoomMoneyWriter(@NonNull final Context context){
        //Fetch the database containing the DAO's
        this( DatabaseUtils.getMoneyDatabase( context ) );
    }

    public RoomMoneyWriter(@NonNull final MoneyDatabase database){
        //Initialize all our DAO's using the MoneyDatabase implementation
        transactionsDao = database.getTransactionAccessor();
        sourceDao = database.getSourceAccessor();
        walletDao = database.getWalletAccessor();
    }


    @Override
    public synchronized void saveTransaction(@NonNull final Transaction transaction) {
        //Save a transaction using an Operation object dedicated to Transaction saving

        new MoneyThread()
                .doInBackground( new SaveTransactionOperation( transaction ) );

    }

    @Override
    public synchronized void saveSource(@NonNull final Source source) {

        /*
            A constraint is placed on new sources having a penny value
            because, according to our API definition, only a Transaction save can update the value
            of a source or wallet. This is done to make the code more cohesive.
         */
        if( source.getPennies() > 0 )
            throw new NewSourceTotalConstraintException();

        new MoneyThread().doInBackground(new MoneyThreadOperation() {
            @Override
            public void doOperation() {
                sourceDao.insert( source );
            }
        });
    }

    @Override
    public synchronized void deleteSource(@NonNull final String sourceId) {
        new MoneyThread().doInBackground(new MoneyThreadOperation() {
            @Override
            public void doOperation() {
                sourceDao.delete( sourceId );
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

            transactionsDao.insert( transaction );
            sourceDao.addAmount( transaction );

            //Fetch the original wallet total, and then determine if the new wallet total
            //based on the amount and transaction type of the Transaction object.

            final Wallet wallet;

            long newWalletTotal =
                    ( (wallet = walletDao.fetchWallet()) != null) ? wallet.getValue(): 0L;

            if( transaction.getTransactionType() == TransactionType.ADD )
                newWalletTotal += transaction.getAmount();

            else if( transaction.getTransactionType() == TransactionType.SPEND ) {
                //Check for negative values because we don't use sub-zero values for the wallet.
                long newValue = newWalletTotal - transaction.getAmount();
                newWalletTotal = (newValue > 0) ? newValue : 0L;
            }

            walletDao.update( new Wallet( newWalletTotal ) );

        }
    }

}
