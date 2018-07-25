package com.whompum.PennyFlip.ActivityDashboard;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.whompum.PennyFlip.ActivityDashboard.Wallet.Persistence.Wallet;
import com.whompum.PennyFlip.ActivityDashboard.Wallet.WalletRepo;
import com.whompum.PennyFlip.Data.UserStartDate;
import com.whompum.PennyFlip.DialogSourceChooser.SourceWrapper;
import com.whompum.PennyFlip.Money.MoneyController;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Time.PennyFlipTimeFormatter;
import com.whompum.PennyFlip.Time.Timestamp;

public class DashboardController implements ActivityDashboardConsumer{

    private static DashboardController instance;

    private WalletRepo repo;

    private MoneyController moneyRepo;

    private DashboardClient client;

    private Context c;

    private DashboardController(@NonNull final Context context, @Nullable LifecycleOwner o){
        UserStartDate.set(context); //Sets the user start date. If already set then it will skip

        repo = WalletRepo.obtain(context);
        moneyRepo = MoneyController.obtain(context);
        if(o != null)
            repo.getData().observe(o, walletObserver);

        this.c = context;
    }

    public static DashboardController create(@NonNull final Context context, @Nullable LifecycleOwner o){
        if(instance == null)
            instance = new DashboardController(context, o);

        return instance;
    }

    public DashboardController bindClient(@Nullable DashboardClient c) {
        this.client = c;

     return this;
    }

    public void saveTransaction(@NonNull final SourceWrapper w, @NonNull final Transaction t){
        repo.updateWallet(t.getTransactionType(), t.getAmount());


        Log.i("TRANSACTIONS", "TRANSACTION TITLE: " + t.getTitle());
        Log.i("TRANSACTIONS", "TRANSACTION TIME: " + PennyFlipTimeFormatter.simpleTime(Timestamp.from(t.getTimestamp())));
        Log.i("TRANSACTIONS", "TRANSACTION PARENT SOURCE: " + t.getSourceId());
        Log.i("TRANSACTIONS", "TRANSACTION TYPE: " + t.getTransactionType());
        Log.i("TRANSACTIONS", "TRANSACTION ID: " + t.getId());



        final Source source = w.getSource();

        if(w.getTag().equals(SourceWrapper.TAG.NEW)) //If is a new SourceObject insert.
            MoneyController.obtain(c).insert(source);
        else //If isn't a new source object, simply update the Source total
            MoneyController.obtain(c).updateSourceAmount(t.getSourceId(), t.getAmount());

        MoneyController.obtain(c).insert(t);
    }

    private Observer<Wallet> walletObserver = new Observer<Wallet>() {
        @Override
        public void onChanged(@Nullable Wallet wallet) {
            if(wallet != null)
                client.onWalletChanged(wallet.getValue());
        }
    };


}
