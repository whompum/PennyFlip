package com.whompum.PennyFlip.Money.Queries.Resolvers.Wallet;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Contracts.Wallet.WalletDao;
import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;
import com.whompum.PennyFlip.Money.Queries.Resolvers.QueryResolver;
import com.whompum.PennyFlip.Money.Wallet.Wallet;

public class WalletQueryResolver extends QueryResolver<Wallet> {

    private WalletDao dao;

    public WalletQueryResolver(@NonNull final MoneyDatabase database){
        this.dao = database.getWalletAccessor();
    }

    @Override
    public Wallet resolve(@NonNull MoneyRequest moneyQuery) {
        return dao.fetchWallet();
    }
}
