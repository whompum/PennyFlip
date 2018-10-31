package com.whompum.PennyFlip.Money.TEMP;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Contracts.MoneyDaoId;
import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Source.Source;

public class SourceItemQueryHandler extends ItemQueryHandler<Source> {

    public SourceItemQueryHandler(@NonNull MoneyDatabase database) {
        super(database);
    }

    @Override
    protected MoneyDaoId<Source> getDao() {
        return moneyDatabase.getSourceAccessor();
    }
}
