package com.whompum.PennyFlip.Dashboard.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.whompum.PennyFlip.Dashboard.Fragments.Adapter.TodayTransactionAdapter;
import com.whompum.PennyFlip.ListUtils.ListFragment;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.R;

import java.util.Collection;
import java.util.List;

public class TodayTransactionListFragment extends ListFragment<Transaction> {

    public static final String TRANSACTION_TYPE_KEY = "transasctionType.ky";

    private TodayTransactionAdapter adapter;

    public static ListFragment<Transaction> newInstance(@NonNull final Integer transactionType){

        final TodayTransactionListFragment fragment = new TodayTransactionListFragment();

        final Bundle args = new Bundle();

        args.putInt( TRANSACTION_TYPE_KEY, transactionType );

        ListFragment.setNoDataLayoutArg( args, R.layout.no_transactions_today_layout );

        fragment.setArguments( args );

      return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new TodayTransactionAdapter( getArguments().getInt( TRANSACTION_TYPE_KEY ) );
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        list.setLayoutManager(
                new LinearLayoutManager( getContext(), LinearLayoutManager.HORIZONTAL, false )
        );

        list.setAdapter( adapter );
    }

    @Override
    protected void handleNewData(@NonNull Collection<Transaction> data) {
         adapter.swapDataset( (List<Transaction>) data );
    }

}
