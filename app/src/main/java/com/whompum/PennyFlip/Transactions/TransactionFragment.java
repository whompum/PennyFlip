package com.whompum.PennyFlip.Transactions;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whompum.PennyFlip.Money.DatabaseUtils;
import com.whompum.PennyFlip.Money.Queries.Deliverable;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;
import com.whompum.PennyFlip.Money.Queries.Responder;
import com.whompum.PennyFlip.Money.Queries.TransactionQueries;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Transaction.DescendingSort;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Money.Transaction.TransactionQueryBuilder;
import com.whompum.PennyFlip.Money.Transaction.TransactionQueryKeys;
import com.whompum.PennyFlip.Money.Transaction.TransactionType;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Time.Timestamp;
import com.whompum.PennyFlip.Transactions.Adapter.TypeBasedTransactionListAdapter;
import com.whompum.PennyFlip.Transactions.Data.ExpansionPredicate;
import com.whompum.PennyFlip.Transactions.Decoration.TimeLineDecorator;
import com.whompum.PennyFlip.Transactions.Adapter.TransactionListAdapter;
import com.whompum.PennyFlip.Transactions.Data.TransactionsGroupConverter;
import com.whompum.PennyFlip.Transactions.Decoration.TransactionStickyHeaders;

import java.util.Collections;
import java.util.List;

public class TransactionFragment extends Fragment implements Observer<List<Transaction>> {

    public static final String SOURCE_KEY = "source.ky";

    @LayoutRes
    private static final int LAYOUT_RES = R.layout.transaction_list;

    private TransactionListAdapter adapter;

    private Source source;

    public static Fragment newInstance(@NonNull final Source source){
        final TransactionFragment fragment = new TransactionFragment();

        final Bundle args = new Bundle();
        args.putSerializable( SOURCE_KEY, source );

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle args = getArguments();

        if( (source = (Source) args.getSerializable( SOURCE_KEY )) == null )
            throw new IllegalArgumentException("Source musn't be null");


        this.adapter = new TypeBasedTransactionListAdapter( source.getTransactionType() );

        //Fetch transactions data
        final MoneyRequest request = new TransactionQueryBuilder()
                .setQueryParameter(TransactionQueryKeys.SOURCE_ID, source.getTitle() )
                .getQuery();

        final Deliverable<LiveData<List<Transaction>>> deliverable = new TransactionQueries()
                .queryObservableObservableGroup( request, DatabaseUtils.getMoneyDatabase( getContext() ) );

        deliverable.attachResponder(new Responder<LiveData<List<Transaction>>>() {
            @Override
            public void onActionResponse(@NonNull LiveData<List<Transaction>> data) {
                data.observe( TransactionFragment.this, TransactionFragment.this );
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View layout = inflater.inflate( LAYOUT_RES, container, false );

       final RecyclerView transactionsList = layout.findViewById( R.id.id_global_list );

           transactionsList.setLayoutManager(
                   new LinearLayoutManager( getContext(), LinearLayoutManager.VERTICAL, false )
           );

           transactionsList.setAdapter( adapter );

           transactionsList.addItemDecoration( new TransactionStickyHeaders( adapter ) );

           int timelineClrRes = -1;

           if( source.getTransactionType() == TransactionType.ADD )
               timelineClrRes = R.color.dark_green;

           else if( source.getTransactionType() == TransactionType.SPEND )
               timelineClrRes = R.color.dark_red;

           transactionsList.addItemDecoration(
                   new TimeLineDecorator( getContext().getResources(), timelineClrRes )
           );

    return layout;
    }

    @Override
    public void onChanged(@Nullable List<Transaction> transactions) {

        if(transactions != null && transactions.size() > 0) {

            Collections.sort(transactions, new DescendingSort());

            final long now = Timestamp.now().getStartOfDay();

            adapter.swapDataset(TransactionsGroupConverter.fromTransactions(transactions, new ExpansionPredicate() {
                @Override
                public boolean expand(long startOfDay, final int position) {
                    return now == startOfDay || position == 0; //If today, or on first header.
                }
            }));

            toggleNoTransactionsDisplay(false);

        }else{
            toggleNoTransactionsDisplay(true);
        }
    }


    private void toggleNoTransactionsDisplay(final boolean toggle){

        if(getView() != null && !toggle)
            getView().findViewById(R.id.no_transactions_container).setVisibility(View.INVISIBLE);

        if(getView() != null && toggle)
            getView().findViewById(R.id.no_transactions_container).setVisibility(View.VISIBLE);
    }

}
