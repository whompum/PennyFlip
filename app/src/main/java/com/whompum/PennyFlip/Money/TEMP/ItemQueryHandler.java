package com.whompum.PennyFlip.Money.TEMP;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Contracts.MoneyDaoId;
import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;

import java.util.Objects;

public abstract class ItemQueryHandler<T> extends RoomQueryHandler<T> {

    public ItemQueryHandler(@NonNull MoneyDatabase database) {
        super(database);
    }

    @Override
    protected void internalQuery(@NonNull MoneyRequest query) {
        if( query.containsNonNullQueryAtKey(MoneyDaoId.ID_KEY) ) {
            onDone(getDao().fetchById(Objects.requireNonNull(query.getQueryParameter(MoneyDaoId.ID_KEY)).get()));
        }else onDone(BAD_QUERY_PARAMETER, "The parameter was null");
    }

    protected abstract MoneyDaoId<T> getDao();
}
