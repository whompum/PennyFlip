package com.whompum.PennyFlip.Money;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.whompum.PennyFlip.Money.Source.ObservableSourceAccessor;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Source.SourceDao;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Money.Transaction.TransactionsDao;
import com.whompum.PennyFlip.Money.Transaction.TransactionType;
import com.whompum.PennyFlip.Money.Wallet.Wallet;
import com.whompum.PennyFlip.Money.Wallet.WalletDao;


public class LocalMoneyProvider {

    private static LocalMoneyProvider instance;

    private WalletDao walletAccessor;

    private LiveData<Wallet> data;

    private SourceDao sourceAccessor;

    private TransactionsDao transactionAccessor;

    private MoneyDatabase database;

    private LocalMoneyProvider(@NonNull final Context context){

       database = Room.databaseBuilder(context, MoneyDatabase.class, MoneyDatabase.NAME).build();

        walletAccessor = database.getWalletAccessor();

        data = walletAccessor.fetchObservable();

        sourceAccessor = database.getSourceAccessor();
        transactionAccessor = database.getTransactionAccessor();
    }

    public synchronized static LocalMoneyProvider obtain(@NonNull final Context context){
        if(instance == null) instance = new LocalMoneyProvider(context.getApplicationContext());

        return instance;
    }

    public LiveData<Wallet> getWallet() {
        return data;
    }

    private long walletValue(){

        Log.i("WALLET_FIX", "walletValue()#LocalMoneyProvider Wallet value is null: " + (data.getValue() == null) );


        if(data.getValue() != null) {
            Log.i("WALLET_FIX", "onChanged(Wallet)#DashboardController Wallet value: " + (data.getValue().getValue()) );
            return data.getValue().getValue();
        }

        Log.i("WALLET_FIX", "walletValue()#LocalMoneyProvider Wallet value is null. Returning 0L: ");

        return 0L;
    }


    public void insertNewSource(@NonNull final Source source){

        new Thread(){
            @Override
            public void run() {
                sourceAccessor.insert(source);
            }
        }.start();

    }

    public void insertNewSource(@NonNull final Source s, @NonNull final Transaction t){

        new Thread(){
            @Override
            public void run() {

                s.setPennies(t.getAmount());
                sourceAccessor.insert(s);
                transactionAccessor.insert(t);

                updateWallet(t);
            }
        }.start();

    }

    public void insertTransaction(@NonNull final Transaction t){

        new Thread(){
            @Override
            public void run() {

                sourceAccessor.addAmount(t);
                transactionAccessor.insert(t);

                updateWallet(t);

            }
        }.start();

    }


    public void deleteSource(@NonNull final String sourceId){
        new Thread(){
            @Override
            public void run() {
                sourceAccessor.delete(sourceId);
            }
        }.start();
    }

    @WorkerThread
    private synchronized void updateWallet(@NonNull final Transaction transaction){


        long value = -1;

        if(transaction.getTransactionType() == TransactionType.ADD)
            value = walletValue()+transaction.getAmount();

        else if(transaction.getTransactionType() == TransactionType.SPEND) {
            final long newValue = walletValue() - transaction.getAmount();
            value = (newValue < 0L) ? 0L: newValue;
        }

        if(value == -1)
            return;

        final Wallet wallet = new Wallet(value);

        walletAccessor.update(wallet);
    }

    public void fetchTransactions(@NonNull final Handler client,
                                               @Nullable final String sourceTitle,
                                               @Nullable final Integer transactionType,
                                               @Nullable final TimeRange range){

        //Calls various methods depending on the permutation of parameters
        new Thread(){
            @Override
            public void run() {

                final Message m = Message.obtain(client);

                final boolean useSource = sourceTitle != null;
                final boolean useType = transactionType != null;
                final boolean useTime = range != null;

                if (useSource) {
                    if (useTime) { //Source + Time
                        m.obj = transactionAccessor.fetch(sourceTitle, range.getMillisFloor(), range.getMillisCiel());
                        client.sendMessage(m);
                        return;
                    } else { // Source Only
                        m.obj = transactionAccessor.fetch(sourceTitle);
                        client.sendMessage(m);
                        return;
                    }
                }

                if(useType) {
                    if (useTime) {//Fetch for TransactionType and TimeRange
                        m.obj = transactionAccessor.fetch(transactionType, range.getMillisFloor(), range.getMillisCiel());
                        client.sendMessage(m);
                        return;
                    } else { //Fetch for TransactionType only
                        m.obj = transactionAccessor.fetch(transactionType);
                        client.sendMessage(m);
                        return;
                    }
                }

                if(useTime){ //Fetch for TimeRange only
                    m.obj = transactionAccessor.fetch(range.getMillisFloor(), range.getMillisCiel());
                    client.sendMessage(m);
                    return;
                }

                    m.obj = transactionAccessor.fetch(); //Fetches all
                    client.sendMessage(m);
            }

        }.start();

    }


    public void fetchSources(@NonNull final Handler client,
                                          @Nullable final String sourceId,
                                          @Nullable final Integer transactionType,
                                          final boolean searchLike){

        new Thread(){
            @Override
            public void run() {

                final Message m = Message.obtain(client);

                final boolean useTitle = sourceId != null;
                final boolean useType = transactionType != null;

                //If we have a title
                //If we want to use a searchLike
                //If we want to use a Type
                //Use searchLike, and type
                //else use Title


                SourceDao a = sourceAccessor;

                if(useTitle){ //We're querying in some way based on a String query for sources id
                    if(searchLike){ //We want all similar sources to :title
                        if(useType){ //Similar Source titles OF transactionType
                            m.obj = a.fetchNonExact(sourceId, transactionType);
                            client.sendMessage(m);
                            return;
                        }else{//WE WANT TO SEARCH EVERY SOURCE BASED ON LIKE NAME
                            m.obj = a.fetchNonExact(sourceId);
                            client.sendMessage(m);
                            return;
                        }
                    }else if(useType){//We only want the source by the Title name
                        m.obj = a.fetch(sourceId, transactionType);
                        client.sendMessage(m);
                        return;
                    }else{
                        m.obj = a.fetch(sourceId);
                        client.sendMessage(m);
                        return;
                    }
                }

                if(useType){ //If we get this far, we only want Sources of this transaction type
                    m.obj = a.fetch(transactionType);
                    client.sendMessage(m);
                    return;
                }

                m.obj = a.fetch(); //If we get this far, we want EVERY SOURCE
                client.sendMessage(m);
            }
        }.start();

    }

    public synchronized void fetchObservableSources(@NonNull final Handler client,
                                          @Nullable final String sourceId,
                                          @Nullable final Integer transactionType,
                                          final boolean searchLike){

        new Thread(){
            @Override
            public void run() {

                final Message m = Message.obtain(client);

                final boolean useTitle = sourceId != null;
                final boolean useType = transactionType != null;

                //If we have a title
                //If we want to use a searchLike
                //If we want to use a Type
                //Use searchLike, and type
                //else use Title


                ObservableSourceAccessor a = database.getObservableSourceAccessor();

                if(useTitle){ //We're querying in some way based on a String query for sources id
                    if(searchLike){ //We want all similar sources to :title
                        if(useType){ //Similar Source titles OF transactionType
                            m.obj = a.fetchNonExact(sourceId, transactionType);
                            client.sendMessage(m);
                            return;
                        }else{//WE WANT TO SEARCH EVERY SOURCE BASED ON LIKE NAME
                            m.obj = a.fetchNonExact(sourceId);
                            client.sendMessage(m);
                            return;
                        }
                    }else if(useType){//We only want the source by the Title name
                        m.obj = a.fetch(sourceId, transactionType);
                        client.sendMessage(m);
                        return;
                    }else{
                        m.obj = a.fetchExact(sourceId);
                        client.sendMessage(m);
                        return;
                    }
                }

                if(useType){ //If we get this far, we only want Sources of this transaction type
                    m.obj = a.fetch(transactionType);
                    client.sendMessage(m);
                    return;
                }

                m.obj = a.fetch(); //If we get this far, we want EVERY SOURCE
                client.sendMessage(m);
            }
        }.start();

    }


}