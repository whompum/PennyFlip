package com.whompum.PennyFlip.Money.Transaction;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;
import com.whompum.PennyFlip.Money.TimeRange;

import java.util.HashSet;

import static com.whompum.PennyFlip.Money.Transaction.TransactionQueryKeys.TRANSACTION_TYPE;
import static com.whompum.PennyFlip.Money.Transaction.TransactionQueryKeys.PENNY_VALUE;
import static com.whompum.PennyFlip.Money.Transaction.TransactionQueryKeys.TIMERANGE;
import static com.whompum.PennyFlip.Money.Transaction.TransactionQueryKeys.SOURCE_ID;

/**
 * Construct a Transaction query object that uses simple
 * conditional statements so that it can only add values when the data is mapped
 * to a proper type. E.g. {@link TransactionQueryKeys.TIMERANGE} should map to a {@link TimeRange} object
 */
public class TransactionQueryBuilder extends MoneyRequest.QueryBuilder {

    public TransactionQueryBuilder() {
        super(TransactionQueryKeys.KEYS);
    }

    @Override
    public <T> MoneyRequest.QueryBuilder setQueryParameter(@NonNull Integer key, @NonNull T value) {

        if( (key == TRANSACTION_TYPE && value instanceof Integer) || (key == PENNY_VALUE /*Add PennyRange object*/) ||
                (key == TIMERANGE && value instanceof TimeRange) || (key == SOURCE_ID && value instanceof String) )
            return super.setQueryParameter(key, value);

        return this;
    }
}
