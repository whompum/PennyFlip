package com.whompum.PennyFlip.Dashboard;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.Money.DatabaseUtils;
import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;
import com.whompum.PennyFlip.Money.Queries.Responder;
import com.whompum.PennyFlip.Money.Queries.TransactionQueries;
import com.whompum.PennyFlip.Money.Queries.WalletQueries;
import com.whompum.PennyFlip.Money.Queries.WalletRequestBuilder;
import com.whompum.PennyFlip.Money.TimeRange;
import com.whompum.PennyFlip.Money.Transaction.DescendingSort;
import com.whompum.PennyFlip.Money.Transaction.TransactionQueryKeys;
import com.whompum.PennyFlip.Money.Transaction.TransactionType;
import com.whompum.PennyFlip.Money.Writes.MoneyWriter;
import com.whompum.PennyFlip.Money.Writes.RoomMoneyWriter;
import com.whompum.PennyFlip.Money.Source.NewSourceTotalConstraintException;
import com.whompum.PennyFlip.Time.Timestamp;
import com.whompum.PennyFlip.Time.UserStartDate;
import com.whompum.PennyFlip.DialogSourceChooser.SourceWrapper;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Money.Wallet.Wallet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DashboardController implements ActivityDashboardConsumer {

    private DashboardClient client;

    private MoneyWriter writer;
    private MoneyDatabase database;

    private Wallet wallet;

    private TimeRange timeRange;

    /**
     *
     * @param context used to instantiate a Repo object
     */
    public DashboardController(@NonNull final Context context, @NonNull final DashboardClient client){
        UserStartDate.set(context); //Sets the user start date. If already set then it will skip

        this.timeRange = getTimeRange();

        this.client = client;

        this.database = DatabaseUtils.getMoneyDatabase( context );

        this.writer = new RoomMoneyWriter( database );

        query();

    }

    private void query(){

        new WalletQueries()
                .queryObservable( new WalletRequestBuilder().getQuery(), database )
                .attachResponder(new Responder<LiveData<Wallet>>() {
                    @Override
                    public void onActionResponse(@NonNull LiveData<Wallet> data) {
                        data.observe( client.getLifecycleOwner(), walletObserver );
                    }
                });

        queryTransactions();

    }


    private void queryTransactions(){
        final MoneyRequest.QueryBuilder request = new MoneyRequest.QueryBuilder( TransactionQueryKeys.KEYS );
        request.setQueryParameter(  TransactionQueryKeys.TIMERANGE, timeRange );

        new TransactionQueries()
                .queryObservableObservableGroup( request.getQuery(), database )
                .attachResponder(new Responder<LiveData<List<Transaction>>>() {
                    @Override
                    public void onActionResponse(@NonNull LiveData<List<Transaction>> data) {
                        data.observe( client.getLifecycleOwner(), transactionObserver );
                    }
                });
    }

    private TimeRange getTimeRange(){

        final long today = Timestamp.now().getStartOfDay();
        final long tomorrow = Timestamp.fromProjection( 1 ).getStartOfDay();

      return new TimeRange( today, tomorrow );
    }

    @Override
    public boolean isTimerangeOutdated() {

        final Timestamp current = Timestamp.now();

        if( current.getMillis() > timeRange.getMillisCiel() ){  //Re-query in-case of day change
            timeRange = getTimeRange();
            queryTransactions();
            return true;
        }

        return false;
    }

    @Override
    public long newWalletWithTransaction(@NonNull Transaction transaction) {
        return wallet.getValue() - transaction.getAmount();
    }

    @Override
    public void saveTransaction(@NonNull final SourceWrapper w, @NonNull final Transaction t){

        //Save the source first, if its new
        if( ( w.getTag().equals( SourceWrapper.TAG.NEW ) ) ) {

            try{
                writer.saveSourceAndTransaction( w.getSource(), t );
            }catch ( NewSourceTotalConstraintException e ){
                w.getSource().setPennies( 0L );
                writer.saveSourceAndTransaction( w.getSource(), t );
            }

        }else
            writer.saveTransaction( t );
    }

    private Observer<Wallet> walletObserver = new Observer<Wallet>() {
        @Override
        public void onChanged(@Nullable Wallet wallet) {
            if( wallet != null ) {
                DashboardController.this.wallet = wallet;

                if( client != null )
                    client.onWalletChanged( wallet.getValue() );
            }
        }
    };

    private Observer<List<Transaction>> transactionObserver = new Observer<List<Transaction>>() {
        @Override
        public void onChanged(@Nullable List<Transaction> transactions) {

            final List<Transaction> addData = new ArrayList<>();
            final List<Transaction> spendData = new ArrayList<>();

            if( transactions != null )
                for( Transaction t: transactions ) {
                    if (t.getTransactionType() == TransactionType.INCOME)
                        addData.add(t);
                    else if (t.getTransactionType() == TransactionType.EXPENSE)
                        spendData.add(t);
                }

            Collections.sort( addData, new DescendingSort() );
            Collections.sort( spendData, new DescendingSort() );

            client.handleAddTransactions( addData );
            client.handleSpendTransactions( spendData );

        }
    };

}
