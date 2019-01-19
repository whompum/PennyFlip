package com.whompum.PennyFlip.Money.Queries.Resolvers.Statistics;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;
import com.whompum.PennyFlip.Money.Queries.Resolvers.QueryResolver;
import com.whompum.PennyFlip.Money.Statistics.TransactionStatistics;
import com.whompum.PennyFlip.Money.Statistics.TransactionStatisticsDao;

import static com.whompum.PennyFlip.Money.Statistics.StatisticQueryKeys.TRANSACTION_TYPE;

public class TransactionStatistcsTypeResolver extends QueryResolver<TransactionStatistics> {

    private TransactionStatisticsDao dao;

    public TransactionStatistcsTypeResolver(@NonNull final MoneyDatabase database) {
        this.dao = database.getStatisticsDao();
    }

    @Override
    public TransactionStatistics resolve(@NonNull MoneyRequest moneyQuery) {

        if( moneyQuery.containsNonNullQueryAtKey( TRANSACTION_TYPE ) &&
                moneyQuery.isQueryOfType( Integer.class, TRANSACTION_TYPE ) ){

            final int type = (int)moneyQuery.getQueryParameter( TRANSACTION_TYPE ).get();

            return dao.fetchByType( type );
        }

        return null;
    }
}
