package com.whompum.PennyFlip.Transactions;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.whompum.PennyFlip.ListUtils.ListFragment;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Transaction.DescendingSort;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Money.Transaction.TransactionType;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Transactions.Adapter.BackgroundResolver;
import com.whompum.PennyFlip.Transactions.Adapter.HeaderBackgroundResolver;
import com.whompum.PennyFlip.Transactions.Adapter.ViewHolder.HolderFactory;
import com.whompum.PennyFlip.Transactions.Decoration.TimeLineDecorator;
import com.whompum.PennyFlip.Transactions.Adapter.TransactionListAdapter;
import com.whompum.PennyFlip.Transactions.Decoration.TransactionStickyHeaders;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TransactionFragment extends ListFragment<Transaction> {

    public static final String EXPANSION_SNAPSHOT_KEY = "expansionSnapshot.ky";

    public static final String SOURCE_KEY = "source.ky";

    private TransactionListAdapter adapter;

    private BackgroundResolver dotBackgroundResolver = new DefaultItemViewDotResolver();

    private HeaderBackgroundResolver headerBackgroundResolver;

    private HolderFactory itemViewHolderFactory;

    public static ListFragment<Transaction> newInstance(@NonNull final Source source, @Nullable final Integer noDataResLayout){
        final TransactionFragment fragment = new TransactionFragment();

        final Bundle args = new Bundle();
        args.putSerializable( SOURCE_KEY, source );

        if( noDataResLayout != null )
            setNoDataLayoutArg( args, noDataResLayout );

        fragment.setArguments(args);
        return fragment;
    }

    public static ListFragment<Transaction> newInstance(){

        final TransactionFragment fragment = new TransactionFragment();

        fragment.setArguments( makeArguments( fragment.noDataResLayout ) );

        return fragment;
    }

    public static ListFragment<Transaction> newInstance(@Nullable final Integer noDataResLayout){

        final  TransactionFragment fragment = new TransactionFragment();

        if( noDataResLayout != null)
            fragment.setArguments( makeArguments( noDataResLayout ) );

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle args = getArguments();

        final Source source = (Source) args.getSerializable( SOURCE_KEY );
        
        if( savedInstanceState != null && savedInstanceState.getSerializable( EXPANSION_SNAPSHOT_KEY ) != null )
            adapter = new TransactionListAdapter(
                    (HashMap) savedInstanceState.getSerializable( EXPANSION_SNAPSHOT_KEY )
            );

        else adapter = new TransactionListAdapter();

        adapter.setItemDotBackgroundResolver( dotBackgroundResolver );

        if( headerBackgroundResolver == null )
            headerBackgroundResolver = new HeaderBackgroundResolver() {
            @Override
            public int getHeaderBackground() {

                if( source != null ) {
                    if (source.getTransactionType() == TransactionType.ADD)
                        return R.drawable.background_rounded_rect_green;

                    else if (source.getTransactionType() == TransactionType.SPEND)
                        return R.drawable.background_rounded_rect_right_red;
                }
                return R.drawable.background_rounded_rect_right_blue;
            }
        };

        adapter.setHeaderBackgroundResolver( headerBackgroundResolver );

        if( itemViewHolderFactory != null )
            adapter.setItemViewHolderFactory( itemViewHolderFactory );
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        list.setLayoutManager(
                new LinearLayoutManager( getContext(), LinearLayoutManager.VERTICAL, false )
        );

        list.setAdapter( adapter );

        final TransactionStickyHeaders stickyHeaders =
                new TransactionStickyHeaders( adapter, (ViewGroup) view.findViewById( R.id.id_global_container ) );

        list.addItemDecoration( stickyHeaders );

        stickyHeaders.setHeaderClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Header was clicked. Now we fetch the last header from the adapter

                final int lastHeaderPos = adapter.getLastHeaderItemPos(
                        list.getChildAdapterPosition( list.getChildAt( 0 ) )  //Adapter pos of children @ index 0
                );

                adapter.toggleGroup( lastHeaderPos );
            }
        });

        int timelineClrRes = R.color.light_blue;

        final Source source = getSource();

        if( source != null ) {

            if (source.getTransactionType() == TransactionType.ADD)
                timelineClrRes = R.color.dark_green;

            else if (source.getTransactionType() == TransactionType.SPEND)
                timelineClrRes = R.color.dark_red;
        }

        list.addItemDecoration(
                new TimeLineDecorator( getContext().getResources(), timelineClrRes )
        );

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable( EXPANSION_SNAPSHOT_KEY, adapter.getSnapshot() );
    }

    @Override
    protected void handleNewData(@NonNull Collection<Transaction> data) {
        if( data.size() > 0 )
            adapter.swapDataset( getSortedList( (List<Transaction>) data ) );
    }

    @Nullable
    private Source getSource(){
        final Serializable s = getArguments().getSerializable( SOURCE_KEY );

        return ( s != null ) ? (Source)s : null;
    }

    public void setItemDotBackgroundResolver(@NonNull final BackgroundResolver dotResolver){
        this.dotBackgroundResolver = dotResolver;

        if( adapter != null )
            adapter.setItemDotBackgroundResolver( dotBackgroundResolver );

    }

    public void setHeaderBackgroundResolver(@NonNull final HeaderBackgroundResolver backgroundResolver){
        this.headerBackgroundResolver = backgroundResolver;

        if( adapter != null )
            adapter.setHeaderBackgroundResolver( headerBackgroundResolver );

    }

    public void setItemViewHolderFactory(@NonNull HolderFactory factory){
        this.itemViewHolderFactory = factory;

        if( adapter != null )
            adapter.setItemViewHolderFactory( itemViewHolderFactory );

    }

    private List<Transaction> getSortedList(@NonNull final List<Transaction> data){

        Collections.sort( data, new DescendingSort() );

        return data;
    }

}
