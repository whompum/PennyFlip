package com.whompum.PennyFlip.Money.TEMP;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Contracts.MoneyDaoId;
import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Wallet.Wallet;

public class WalletObservableQueryHandler extends ItemQueryHandler<Wallet>{

    public WalletObservableQueryHandler(@NonNull MoneyDatabase database) {
        super(database);
    }

    @Override
    protected MoneyDaoId<Wallet> getDao() {
        return moneyDatabase.getWalletAccessor();
    }
}
