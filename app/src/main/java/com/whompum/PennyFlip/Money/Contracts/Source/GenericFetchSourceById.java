package com.whompum.PennyFlip.Money.Contracts.Source;

import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Contracts.MoneyQueryBase;
import com.whompum.PennyFlip.Money.Contracts.MoneyDaoId;

/**
 * Fetch a single {@link Source} object via its title.
 * The need for this class is to allow a separate generified access point to the {@link Source}
 * data, based on its ID, than {@link SourceDao} since {@link SourceDao} and {@link MoneyQueryBase}
 * are guaranteed to return the same data types everytime, where as, if, for example,
 * {@link SourceDao} and {@link MoneyQueryBase} return a list of data, or a LiveData object containing a List,
 * this implementation may just want to return a single Source, or a LiveData source, minus the set
 * @param <T>
 */
public abstract class GenericFetchSourceById<T> implements MoneyDaoId<T> {

    @Override
    public <P> T fetchById(@NonNull final P id){
        if( (id instanceof String) )
            return null;

        return fetchById(id);
    }

    @Query("SELECT * FROM Source WHERE title = :title")
    abstract T fetchById(@NonNull final String title);

}
