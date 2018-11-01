package com.whompum.PennyFlip.Money.Queries.Resolvers;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Contracts.Transaction.TransactionDao;
import com.whompum.PennyFlip.Money.Contracts.Transaction.TransactionQueryDao;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;
import com.whompum.PennyFlip.Money.Queries.Resolvers.GroupQueryResolver;

import static com.whompum.PennyFlip.Money.Transaction.TransactionQueryKeys.*;

public class TransactionQueryResolver<T> extends GroupQueryResolver<T> {

    public TransactionQueryResolver( @NonNull TransactionDao<T> server ) {
        super( server );
    }

    @Override
    public T resolve(@NonNull MoneyRequest moneyQuery ) {

        //Do typical checking of values and then call the right methods

        final TransactionQueryDao<T> internalServer = (TransactionQueryDao<T>) server;

        final boolean useSourceTitle = moneyQuery.containsNonNullQueryAtKey( SOURCE_ID );
        final boolean useTimeRange = moneyQuery.containsNonNullQueryAtKey( TIMERANGE );
        final boolean useType = moneyQuery.containsNonNullQueryAtKey( TRANSACTION_TYPE );
        final boolean usePennyValue = moneyQuery.containsNonNullQueryAtKey( PENNY_VALUE ); //Support en futuro


        return super.resolve( moneyQuery );
    }
}
