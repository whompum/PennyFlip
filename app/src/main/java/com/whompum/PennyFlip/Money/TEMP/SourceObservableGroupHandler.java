package com.whompum.PennyFlip.Money.TEMP;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Contracts.MoneyQueryBase;
import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Source.Source;

import java.util.List;

public class SourceObservableGroupHandler extends GroupQueryHandler<LiveData<List<Source>>> {
    public SourceObservableGroupHandler(@NonNull MoneyDatabase database) {
        super(database);
    }

    @Override
    protected MoneyQueryBase<LiveData<List<Source>>> getDao() {
        return moneyDatabase.getObservableSourceAccessor();
    }
}
