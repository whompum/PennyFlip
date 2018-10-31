package com.whompum.PennyFlip.Money.TEMP;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Contracts.MoneyDaoId;
import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Source.Source;

public class SourceObservableItemHandler extends ItemQueryHandler<LiveData<Source>> {

    public SourceObservableItemHandler(@NonNull MoneyDatabase database) {
        super(database);
    }

    @Override
    protected MoneyDaoId<LiveData<Source>> getDao() {
        return moneyDatabase.getObservableSourceAccessor();
    }
}
