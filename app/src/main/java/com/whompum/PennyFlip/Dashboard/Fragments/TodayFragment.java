package com.whompum.PennyFlip.Dashboard.Fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import currencyedittext.whompum.com.currencyedittext.CurrencyEditText;

public class TodayFragment extends Fragment implements CollectionQueryReceiver<Transaction> {

    public static final String TRANSACTION_TYPE_KEY = "transactionType.ky";

    @LayoutRes
    public static final int LAYOUT = R.layout.dashboard_today_layout;

    @BindView(R.id.id_global_total_display) protected CurrencyEditText value;

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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        final int transactionType = getArguments().getInt( TRANSACTION_TYPE_KEY );

        ((EditText)view.findViewById( R.id.id_global_total_display )).setTextColor(
                resolveColor( getContext(), transactionType )
        );

        getChildFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentAttached(FragmentManager fm, Fragment f, Context context) {
                super.onFragmentAttached(fm, f, context);
                observer.setAttached( true );
            }

            @Override
            public void onFragmentDetached(FragmentManager fm, Fragment f) {
                super.onFragmentDetached(fm, f);
                observer.setAttached( false );
            }
        }, true);

        commitChildFragment( getChildFragmentManager().beginTransaction() );
        
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    @Override
    public void onNoData() {

        if( getView() != null ) {
            if( observer.isAttached )
                getExistingFragment().onNoData();

        }else
            observer.subscribe(new FragmentStateOperation() {
                @Override
                public void onAttached() {
                    onNoData();
                }
            });

    }

    @Override
    public void display(@NonNull final Collection<Transaction> data) {

        if( getView() != null ) {

            updateValue( (List<Transaction>)data );

            if ( observer.isAttached )
                getExistingFragment().display(data);
        }else
            observer.subscribe(new FragmentStateOperation() {
                @Override
                public void onAttached() {
                    display( data );
                }
            });
    }

    private void commitChildFragment(@NonNull final FragmentTransaction fragTrans){

       fragTrans.replace(
               R.id.id_local_fragment_container,
               ( fragmentExists() ) ? getExistingFragment() : getNewFragment(),
               "TAG"
               );


        fragTrans.commit();

    }

    private boolean fragmentExists(){
        return getChildFragmentManager().findFragmentByTag("TAG") != null;
    }

    private ListFragment<Transaction> getNewFragment(){
        return TodayTransactionListFragment.newInstance( getArguments().getInt( TRANSACTION_TYPE_KEY ) );
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

    private int resolveColor(@NonNull final Context ctx, @ColorRes final int transactionType){

            int color;
            int colorId = ( transactionType == TransactionType.ADD ) ? R.color.dark_green : R.color.dark_red;

            if(Build.VERSION.SDK_INT >= 23)
                color = ctx.getColor( colorId );
            else
                color = ctx.getResources().getColor( colorId );

            return color;
    }


    private static class FragmentStateListener{

        private boolean isAttached = false;

        private HashSet<FragmentStateOperation> observers = new HashSet<>();

        public void setAttached(final boolean isAttached){
            this.isAttached = isAttached;

            final Iterator<FragmentStateOperation> i = observers.iterator();

            while( i.hasNext() )
                i.next().onAttached();

        }

        public void subscribe(@NonNull final  FragmentStateOperation o){
            observers.add( o );
        }
    }

    private interface FragmentStateOperation{
        void onAttached();
    }



}
