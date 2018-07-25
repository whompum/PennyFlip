package com.whompum.PennyFlip.Money;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.whompum.PennyFlip.ActivityDashboard.Wallet.Persistence.Wallet;
import com.whompum.PennyFlip.ActivityDashboard.Wallet.Persistence.WalletDao;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Source.SourceDao;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Money.Transaction.TransactionDao;
import com.whompum.PennyFlip.Transactions.Models.TransactionType;

import java.util.List;


public class MoneyController {

    private static MoneyController instance;

    private WalletDao walletAccessor;

    private SourceDao sourceAccessor;

    private TransactionDao transactionAccessor;

    private MoneyController(@NonNull final Context context){

        MoneyDatabase database =
                Room.databaseBuilder(context, MoneyDatabase.class, MoneyDatabase.NAME).build();

        walletAccessor = database.getWalletAccessor();

        sourceAccessor = database.getSourceAccessor();
        transactionAccessor = database.getTransactionAccessor();
    }

    public synchronized static MoneyController obtain(@NonNull final Context context){
        if(instance == null) instance = new MoneyController(context.getApplicationContext());

        return instance;
    }

    public void observeTotal(@NonNull final LifecycleOwner owner, @NonNull final WalletListener l){

        walletAccessor.get()
                .observe(owner, new Observer<Wallet>() {
                    @Override
                    public void onChanged(@Nullable Wallet wallet) {
                        if(wallet != null)
                            l.onNewTotal(wallet.getValue());
                    }
                });
    }

    public void unObserveTotal(@NonNull final LifecycleOwner owner){
        if(walletAccessor.get().hasActiveObservers())
            walletAccessor.get().removeObservers(owner);
    }

    public void updateWallet(final int type, final long amt){

        synchronized (this) {
            long newValue = -1;

            final Wallet w = walletAccessor.get().getValue();

            if (type == TransactionType.ADD && w != null)
                newValue = w.getValue() + amt;

            if (type == TransactionType.SPEND && w != null) {

                newValue = w.getValue() - amt;

                if (newValue < 0)
                    newValue = 0L;
            }

            if (newValue == -1)
                return;

            final Wallet temp = new Wallet();

            temp.setValue(newValue);

            updateWallet(temp);
        }
    }

    private void updateWallet(@NonNull final Wallet wallet){
        new Thread(){
            @Override
            public void run() {
                walletAccessor.update(wallet);
            }
        }.start();
    }

    public void insert(@NonNull final Source source){
        new Thread(){
            @Override
            public void run() {
                sourceAccessor.insert(source);
            }
        }.start();
    }

    public void insert(@NonNull final Transaction transaction){
        new Thread(){
            @Override
            public void run() {
                transactionAccessor.insert(transaction);
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

    public void updateSourceAmount(@NonNull final String sourceId, final long pennies){
        new Thread(){
            @Override
            public void run() {
                sourceAccessor.addAmount(sourceId, pennies);
            }
        }.start();
    }

    public synchronized void fetchTransactions(@NonNull final Handler client,
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


    public synchronized void fetchSources(@NonNull final Handler client,
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

                if(useTitle){ //We're querying in some way based on a String query for sources id
                    if(searchLike){ //We want all similar sources to :title
                        if(useType){ //Similar Source titles OF transactionType
                            Log.i("SOURCE_DIALOG", "FETCHING FOR LIKE: " + sourceId);
                            m.obj = sourceAccessor.fetchNonExact(sourceId, transactionType);
                            client.sendMessage(m);
                            return;
                        }else{//WE WANT TO SEARCH EVERY SOURCE BASED ON LIKE NAME
                            m.obj = sourceAccessor.fetchNonExact(sourceId);
                            client.sendMessage(m);
                            return;
                        }
                    }else if(useType){//We only want the source by the Title name
                        m.obj = sourceAccessor.fetch(sourceId, transactionType);
                        client.sendMessage(m);
                        return;
                    }else{
                        m.obj = sourceAccessor.fetch(sourceId);
                        client.sendMessage(m);
                        return;
                    }
                }

                if(useType){ //If we get this far, we only want Sources of this transaction type
                    m.obj = sourceAccessor.fetch(transactionType);
                    client.sendMessage(m);
                    return;
                }

                m.obj = sourceAccessor.fetch(); //If we get this far, we want EVERY SOURCE
                client.sendMessage(m);
            }
        }.start();

    }

    public List<Source> toSourceList(@NonNull final LiveData<List<Source>> data){
        return data.getValue();
    }

}
















