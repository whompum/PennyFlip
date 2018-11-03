package com.whompum.PennyFlip.Money.Queries;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Queries.Deliverable;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;
import com.whompum.PennyFlip.Money.Queries.QueryHelper;
import com.whompum.PennyFlip.Money.Queries.Resolvers.Wallet.WalletObservableQueryResolver;
import com.whompum.PennyFlip.Money.Queries.Resolvers.Wallet.WalletQueryResolver;
import com.whompum.PennyFlip.Money.Wallet.Wallet;

public class WalletQueries {

    public Deliverable<Wallet> query(@NonNull final MoneyRequest request,
                                     @NonNull final MoneyDatabase database){
        return new QueryHelper<>(
                new WalletQueryResolver( database )
        ).query( request );
    }

    public Deliverable<LiveData<Wallet>> queryObservable(@NonNull final MoneyRequest request,
                                                 @NonNull final MoneyDatabase database){
        return new QueryHelper<>(
                new WalletObservableQueryResolver( database )
        ).query( request );
    }

}
