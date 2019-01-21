package com.whompum.PennyFlip.Dashboard.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.whompum.PennyFlip.ListUtils.CollectionQueryReceiver;
import com.whompum.PennyFlip.ListUtils.ListFragment;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Money.Transaction.TransactionType;
import com.whompum.PennyFlip.R;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import currencyedittext.whompum.com.currencyedittext.CurrencyEditText;

public class TodayFragment extends Fragment implements CollectionQueryReceiver<Transaction> {

    public static final String TRANSACTION_TYPE_KEY = "transactionType.ky";

    @LayoutRes
    public static final int LAYOUT = R.layout.dashboard_today_layout;

    @BindView(R.id.id_global_total_display) protected CurrencyEditText value;

    @BindColor(R.color.dark_green) public int clrAdd;
    @BindColor(R.color.dark_red) public int clrSpend;

    private Unbinder unbinder;

    private FragmentStateListener observer = new FragmentStateListener();

    public static TodayFragment newInstance(@NonNull final Integer transactionType){
        final TodayFragment fragment = new TodayFragment();

        final Bundle args = new Bundle();
        args.putInt( TRANSACTION_TYPE_KEY, transactionType );

        fragment.setArguments( args );

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View layout = inflater.inflate( LAYOUT, container, false );

        unbinder = ButterKnife.bind( this, layout );

    return layout;
    }

    @SuppressLint("CommitTransaction")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        final int transactionType = getArguments().getInt( TRANSACTION_TYPE_KEY );

        ((EditText)view.findViewById( R.id.id_global_total_display )).setTextColor(
                resolveColor( transactionType )
        );

        final FragmentManager fragmentManager = getChildFragmentManager();

        if( !fragmentExists() ){
            fragmentManager.registerFragmentLifecycleCallbacks( observer, true );
            commitChildFragment( fragmentManager.beginTransaction() );
        }
        
    }

    @Override
    public void onStop() {
        super.onStop();
        observer.clearSubscriptions();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    @Override
    public void onNoData() {

        if( getExistingFragment() != null )
            getExistingFragment().onNoData();
        else
            observer.subscribe( () -> getExistingFragment().onNoData() );


    }

    @Override
    public void display(@NonNull final Collection<Transaction> data) {

        if( getView() != null )
            updateValue( (List<Transaction>)data );

        if( getExistingFragment() != null )
            getExistingFragment().display( data );

        else{
            observer.subscribe( () -> getExistingFragment().display( data ) );
        }

    }

    private void commitChildFragment(@NonNull final FragmentTransaction fragTrans){
        commitChildFragment( fragTrans, getNewFragment() );
    }

    private void commitChildFragment(@NonNull final FragmentTransaction fragTrans, @NonNull final Fragment frag){
        fragTrans.add(
                R.id.id_local_fragment_container,
                frag,
                "TAG"
        );
                
        fragTrans.commit();
    }

    private boolean fragmentExists(){
        return getChildFragmentManager().findFragmentByTag("TAG") != null;
    }

    private ListFragment<Transaction> getNewFragment(){
        final ListFragment<Transaction> fragment =
                TodayTransactionListFragment.newInstance( getArguments().getInt( TRANSACTION_TYPE_KEY ) );
        
        return fragment;
    }

    @Nullable
    private ListFragment<Transaction> getExistingFragment(){
        return (ListFragment<Transaction>) getChildFragmentManager().findFragmentByTag("TAG");
    }


    /**
     * Updates the Total Display value for Today's Transactions
     *
     * @param transactions The current list of transactions
     */
    protected void updateValue(final List<Transaction> transactions){

        long total = 0L;

        for( Transaction t: transactions )
            total += t.getAmount();

        this.value.setText( String.valueOf( total ) );

    }

    private int resolveColor(@ColorRes final int transactionType){

        if( transactionType == TransactionType.INCOME)
            return clrAdd;

        else if( transactionType == TransactionType.EXPENSE)
            return clrSpend;

        return 0;
    }


    private static class FragmentStateListener extends FragmentManager.FragmentLifecycleCallbacks{

        private HashSet<FragmentStateOperation> observers = new HashSet<>();

        @Override
        public void onFragmentViewCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull View v, @Nullable Bundle savedInstanceState) {
            notifyViewCreated();
        }

        public void notifyViewCreated(){

            final Iterator<FragmentStateOperation> i = observers.iterator();

            while( i.hasNext() )
                i.next().onAttached();

        }

        public void subscribe(@NonNull final FragmentStateOperation o){
            observers.add( o );
        }
        public void clearSubscriptions(){ observers.clear(); }
    }

    private interface FragmentStateOperation{
        void onAttached();
    }



}
