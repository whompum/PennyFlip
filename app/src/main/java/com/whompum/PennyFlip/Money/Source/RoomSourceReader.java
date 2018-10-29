package com.whompum.PennyFlip.Money.Source;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.Money.Queries.MoneyDao;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyQuery;
import com.whompum.PennyFlip.Money.Queries.QueryReceiver;
import com.whompum.PennyFlip.Money.Queries.RoomMoneyReader;
import com.whompum.PennyFlip.Money.TESTMoneyDatabase;

import java.util.Collection;

public class RoomSourceReader extends RoomMoneyReader<Source, String> {

    public RoomSourceReader(@NonNull Context context,
                            @Nullable QueryReceiver<Collection<Source>> groupReceiver,
                            @Nullable QueryReceiver<Source> itemReceiver) {
        super(context, groupReceiver, itemReceiver);
    }

    @Override
    protected void call(@NonNull MoneyQuery query) {
        super.call(query);
    }

    @Override
    protected MoneyDao<Source, String> getDao(@NonNull TESTMoneyDatabase database) {
        return database.getSourceAccessor();
    }
}
