package com.whompum.PennyFlip.Money.TEMP;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Contracts.MoneyQueryBase;
import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;

public abstract class GroupQueryHandler<T> extends RoomQueryHandler<T> {

    public GroupQueryHandler(@NonNull MoneyDatabase database) {
        super(database);
    }

    @Override
    protected void internalQuery(@NonNull MoneyRequest query) {

        final T data = getDao().fetchAll();

        if(data == null)
            onDone( NULL_DATA_QUERY, "The data was null" );

        else onDone( data );

    }

    protected abstract MoneyQueryBase<T> getDao();

}
