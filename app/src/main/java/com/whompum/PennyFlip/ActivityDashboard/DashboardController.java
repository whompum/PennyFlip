package com.whompum.PennyFlip.ActivityDashboard;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.ActivityDashboard.Wallet.Persistence.Wallet;
import com.whompum.PennyFlip.ActivityDashboard.Wallet.WalletRepo;
import com.whompum.PennyFlip.Data.Services.SaveTransactionsService;
import com.whompum.PennyFlip.Data.UserStartDate;
import com.whompum.PennyFlip.Money.Sources.SourceWrapper;

public class DashboardController implements ActivityDashboardConsumer{

    private static DashboardController instance;

    private WalletRepo repo;
    //Also contain references to SourceRepo as well

    private DashboardClient client;

    private Context c;

    private DashboardController(@NonNull final Context context, @Nullable LifecycleOwner o){
        UserStartDate.set(context); //Sets the user start date. If already set then it will skip

        repo = WalletRepo.obtain(context);

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

    public void saveTransaction(final int transType, final long amt, final SourceWrapper wrapper){
        repo.updateWallet(transType, amt);

        final Intent intent = new Intent(c, SaveTransactionsService.class);

        intent.putExtra(SaveTransactionsService.SOURCE_KEY, wrapper);
        intent.putExtra(SaveTransactionsService.TRANS_AMOUNT_KEY, amt);

        c.startService(intent);
    }

    private Observer<Wallet> walletObserver = new Observer<Wallet>() {
        @Override
        public void onChanged(@Nullable Wallet wallet) {
            if(wallet != null)
                client.onWalletChanged(wallet.getValue());
        }
    };


}
