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

import com.whompum.PennyFlip.Money.LocalMoneyProvider;
import com.whompum.PennyFlip.Money.Transaction.DescendingSort;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Time.Timestamp;
import com.whompum.PennyFlip.Transactions.Data.ExpansionPredicate;
import com.whompum.PennyFlip.Transactions.Decoration.TimeLineDecorator;
import com.whompum.PennyFlip.Transactions.Adapter.TransactionListAdapter;
import com.whompum.PennyFlip.Transactions.Data.TransactionsGroupConverter;
import com.whompum.PennyFlip.Transactions.Decoration.TransactionStickyHeaders;

import java.util.Collections;
import java.util.List;

/**
 * Created by bryan on 12/30/2017.
 */

public class TransactionFragment extends Fragment implements Handler.Callback, Observer<List<Transaction>> {

    public static final String SOURCE_KEY = "source.ky";

    @LayoutRes
    private static final int LAYOUT_RES = R.layout.transaction_list;

    private TransactionListAdapter adapter;

    private Handler resultReceiver = new Handler(this);

    public static Fragment newInstance(@NonNull final Bundle args){
        final TransactionFragment fragment = new TransactionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String sourceId = getArguments().getString(SOURCE_KEY);

        if(sourceId == null) throw new IllegalStateException("We must have a Source ID");

        this.adapter = new TransactionListAdapter(getContext());

        LocalMoneyProvider.obtain(getContext())
                .fetchTransactions(resultReceiver, sourceId, null, null);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View layout = inflater.inflate(LAYOUT_RES, container, false);

       final RecyclerView transactionsList = layout.findViewById(R.id.id_global_list);
           transactionsList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
           transactionsList.setAdapter(adapter);
           transactionsList.addItemDecoration(new TransactionStickyHeaders(adapter));
           transactionsList.addItemDecoration(new TimeLineDecorator(getContext().getResources()));

    return layout;
    }

    @Override
    public boolean handleMessage(Message msg) {

        if( !(msg.obj instanceof LiveData) ) return true;

        ((LiveData<List<Transaction>>)msg.obj).observe(this, this);

        return true;
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
