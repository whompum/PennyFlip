package com.whompum.PennyFlip.Dashboard;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.Data.UserStartDate;
import com.whompum.PennyFlip.DialogSourceChooser.SourceWrapper;
import com.whompum.PennyFlip.Money.MoneyController;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Money.Wallet;

public class DashboardController implements ActivityDashboardConsumer, Observer<Wallet>{

    private static DashboardController instance;

    private MoneyController repo;

    private DashboardClient client;

    /**
     *
     * @param context used to instantiate a Repo object
     * @param o Used to track changes occuring to the Wallet
     */
    private DashboardController(@NonNull final Context context, @Nullable LifecycleOwner o){
        UserStartDate.set(context); //Sets the user start date. If already set then it will skip
        repo = MoneyController.obtain(context);

        if(o != null)
            repo.getWallet().observe(o, this);
    }

    public static DashboardController create(@NonNull final Context context, @Nullable LifecycleOwner o){
        if(instance == null)
            instance = new DashboardController(context, o);

        return instance;
    }

    public DashboardController bindClient(@NonNull DashboardClient c) {
        this.client = c;

     return this;
    }

    public void saveTransaction(@NonNull final SourceWrapper w, @NonNull final Transaction t){
        //Update Wallet
        repo.updateWallet(t.getTransactionType(), t.getAmount());

        //Insert a new Source object; Implicity saves the Transaction
        if(w.getTag().equals(SourceWrapper.TAG.NEW)) repo.insertNew(w.getSource(), t);

        //Update sourceAmount. Implicity saves the transaction
        else repo.updateSourceAmount(t);

    }

    @Override
    public void onChanged(@Nullable Wallet wallet) {
        if(wallet != null)
            if(client != null)
                client.onWalletChanged(wallet.getValue());
    }
}
