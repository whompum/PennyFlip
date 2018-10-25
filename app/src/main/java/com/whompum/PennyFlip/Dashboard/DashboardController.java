package com.whompum.PennyFlip.Dashboard;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.whompum.PennyFlip.Money.LocalMoneyProvider;
import com.whompum.PennyFlip.Money.MoneyWriter;
import com.whompum.PennyFlip.Money.RoomMoneyWriterImpl;
import com.whompum.PennyFlip.Money.Source.NewSourceTotalConstraintException;
import com.whompum.PennyFlip.Time.UserStartDate;
import com.whompum.PennyFlip.DialogSourceChooser.SourceWrapper;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Money.Wallet;

public class DashboardController implements ActivityDashboardConsumer, Observer<Wallet>{

    private static DashboardController instance;

    private LocalMoneyProvider repo;

    private DashboardClient client;

    private MoneyWriter writer;


    public DashboardController(@NonNull final Context context){
        this(context, null);
    }

    /**
     *
     * @param context used to instantiate a Repo object
     * @param o Used to track changes occuring to the Wallet
     */
    public DashboardController(@NonNull final Context context, @Nullable LifecycleOwner o){
        UserStartDate.set(context); //Sets the user start date. If already set then it will skip
        repo = LocalMoneyProvider.obtain(context);
        this.writer = new RoomMoneyWriterImpl(context);

        if(o != null) {
            repo.getWallet().observe(o, this);
        }
    }

    @Override
    public void bindClient(@NonNull DashboardClient c) {
        this.client = c;
    }

    @Override
    public void bindWalletObserver(@NonNull final LifecycleOwner o){
        if(repo.getWallet().hasActiveObservers())
            repo.getWallet().removeObservers(o);

        repo.getWallet().observe(o, this);
    }

    public void saveTransaction(@NonNull final SourceWrapper w, @NonNull final Transaction t){
        /**
        //Update Wallet
        repo.updateWallet(t.getTransactionType(), t.getAmount());

        //Insert a new Source object; Implicity saves the Transaction
        if(w.getTag().equals(SourceWrapper.TAG.NEW)) repo.insertNew(w.getSource(), t);

        //Update sourceAmount. Implicity saves the transaction
        else repo.updateSourceAmount(t);
       **/


        if((w.getTag().equals(SourceWrapper.TAG.NEW))) {

            try{
                writer.saveSource(w.getSource());
            }catch (NewSourceTotalConstraintException e){
                w.getSource().setPennies(0L);
                writer.saveSource(w.getSource());
            }

        }

        writer.saveTransaction(t);

    }

    @Override
    public void onChanged(@Nullable Wallet wallet) {

        Log.i("WALLET_FIX", "onChanged(Wallet)#DashboardController Wallet is null: " + (wallet==null) );

        if(wallet != null)
            if(client != null)
                client.onWalletChanged(wallet.getValue());
    }
}
