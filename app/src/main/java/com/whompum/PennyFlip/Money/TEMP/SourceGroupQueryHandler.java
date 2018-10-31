package com.whompum.PennyFlip.Money.TEMP;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Contracts.MoneyQueryBase;
import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Source.Source;

import java.util.List;

public class SourceGroupQueryHandler extends GroupQueryHandler<List<Source>> {

    public SourceGroupQueryHandler(@NonNull MoneyDatabase database) {
        super(database);
    }

    @Override
    protected MoneyQueryBase<List<Source>> getDao() {
        return moneyDatabase.getSourceAccessor();
    }
}
