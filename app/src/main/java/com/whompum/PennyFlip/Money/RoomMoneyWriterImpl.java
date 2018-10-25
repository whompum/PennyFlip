package com.whompum.PennyFlip.Money;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Source.SourceDao;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Money.Transaction.TransactionType;
import com.whompum.PennyFlip.Money.Transaction.TransactionsDao;
import com.whompum.PennyFlip.Money.MoneyThreadWriter.ThreadWriterOperation;

/**
 * Writes data to a Room Database
 */
public class RoomMoneyWriterImpl implements MoneyWriter{

    //DAO for transaction objects
    private TransactionsDao transactionsDao;

    //DAO for Source objects
    private SourceDao sourceDao;

    //DAO for Wallet objects
    private WalletDao walletDao;

    public RoomMoneyWriterImpl(@NonNull final Context context){
        //Fetch the database containing the DAO's
        final MoneyDatabase database =
                Room.databaseBuilder(context, MoneyDatabase.class, MoneyDatabase.NAME).build();

        //Initialize all our DAO's using the MoneyDatabase implementation
        transactionsDao = database.getTransactionAccessor();
        sourceDao = database.getSourceAccessor();
        walletDao = database.getWalletAccessor();

    }


    @Override
    public void saveTransaction(@NonNull final Transaction transaction) {
        //Save the Transaction then update both the Source and Wallet

        new MoneyThreadWriter().doInBackground(new ThreadWriterOperation(){
            @Override
            void doOperation() {
                //After saving, update both the wallet and the Source
                transactionsDao.insert(transaction);
                sourceDao.addAmount(transaction);

                /*
                   Fetch the original wallet total, and then determine if the new wallet total
                   based on the amount and transaction type of the Transaction object.
                 */
                long newWalletTotal = fetchWallet().getValue();

                if(transaction.getTransactionType() == TransactionType.ADD)
                    newWalletTotal += transaction.getAmount();
                else if(transaction.getTransactionType() == TransactionType.SPEND)
                    newWalletTotal -= transaction.getAmount();

                walletDao.update(new Wallet(newWalletTotal));
            }
        });

    }

    @WorkerThread //TODO move to a read object and reference it.
    private Wallet fetchWallet(){
        return walletDao.fetch().getValue();
    }

    @Override
    public void saveSource(@NonNull Source source) {

    }

    @Override
    public void deleteSource(@NonNull String sourceId) {

    }

    @Override
    public void updateSourceTotal(@NonNull String sourceId, long amount) {

    }

    @Override
    public void updateWalletTotal(long amount) {

    }
}
