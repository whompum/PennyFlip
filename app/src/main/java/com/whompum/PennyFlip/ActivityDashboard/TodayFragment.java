package com.whompum.PennyFlip.ActivityDashboard;


import android.database.Cursor;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.whompum.PennyFlip.Data.Loader.TransactionsLoader;
import com.whompum.PennyFlip.Data.Schemas.TransactionsSchema;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Time.MidnightTimestamp;
import com.whompum.PennyFlip.Transaction.Models.Transactions;
import com.whompum.PennyFlip.Transaction.TransactionsCursorAdapter;

import java.util.List;

import currencyedittext.whompum.com.currencyedittext.CurrencyEditText;

/**
 * Created by bryan on 1/12/2018.
 */

public abstract class TodayFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    /**
     * STATE DECLARATIONS
     *
     * LAYOUT: The layout resource File for this fragment
     * VALUE_TEXT_COLOR: The text color for todays Value (Set by children)
     * WHERE_ARGS_KEY: Key for the loader arguments 'Where arguments' args
     * WHERE_KEY: Key for the loader arguments 'Where' args
     * SORT_KEY: Key for the Loader arguments "sort order" args
     *
     * WHERE: Literal 'where' argument: Fetches arguments where timestamp is >= todays beginning timestamp (12:00 AM),
     *        And source type of the transaction equals the Fragments source type (Either spend/add/callibrate)
     *
     * sourceType: The type of source this fragments transactions represent (Spend / Add / Callibrabate)
     *
     * value: Displays the total amount for the day, added/spended
     *
     * transactionsList: RecyclerView container for todays transactions
     *
     * transactionsAdapter: RecView adapter; Converts Transactions objects into RecyclerView views
     *
     */

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_dasbhoard_summary;

    @ColorRes
    protected int VALUE_TEXT_COLOR = R.color.dark_grey;


    protected static final String WHERE_ARGS_KEY = "WhereArgs.key";
    protected static final String WHERE_KEY = "Where.key";
    protected static final String SORT_KEY = "sort.key";

    public static final String WHERE =  TransactionsSchema.TransactionTable.COL_TIMESTAMP + " >=?" + " AND "
            + TransactionsSchema.TransactionTable.COL_SOURCE_TYPE + " =?";


    protected int sourceType = Integer.MIN_VALUE;

    private CurrencyEditText value;

    protected RecyclerView transactionsList;
    private RecyclerView.Adapter transactionsAdapter;


    protected TransactionsCursorAdapter cursorAdapter = new TransactionsCursorAdapter(null);


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * NOTE because this confused me: I don't call getSupportLoaderManager because this fragment is a support fragment
         * in the same release as the support loader manager (v4). Thus its just a simple call to getLoaderManager()
         */
        getLoaderManager().initLoader(getLoaderId(), getloaderArguments(), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View layout = inflater.inflate(LAYOUT, container, false);


        this.transactionsList = layout.findViewById(R.id.id_today_transaction_list);
        this.transactionsList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        this.transactionsAdapter = new TodayTransactionAdapter(getContext());

        transactionsList.setAdapter(transactionsAdapter);

        this.value = layout.findViewById(R.id.tempValue);
             value.setTextColor(getColor(VALUE_TEXT_COLOR));


        transactionsAdapter.registerAdapterDataObserver(listObserver);

        //TRIFECTA of touch handling!

        layout.post(delegate);
        layout.setOnTouchListener(listener);

        transactionsList.requestDisallowInterceptTouchEvent(true);

    return layout;
    }

    final Rect delegateBoundary = new Rect();

    private final Runnable delegate = new Runnable() {
        @Override
        public void run() {

            if(getView() == null)
                return;

            delegateBoundary.set(getView().getLeft(), transactionsList.getTop(), getView().getRight(), getView().getBottom());

        }
    };



    View.OnTouchListener listener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {


            if(delegateBoundary.contains((int)event.getX(), (int)event.getY())){
                transactionsList.onTouchEvent(event);
                return true;
            }

            return false;
        }
    };

    private final RecyclerView.AdapterDataObserver listObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {

            final int itemCount = transactionsAdapter.getItemCount();

            //Use itemCount to change things
            //WTF IS THIS CLASS FOR AGAIN?
        }
    };

    /**
     * Just a utility method to return a color
     * @param colorRes The resource id for the Color to fetch
     * @return A resource-resolved Color
     */
    private int getColor(@ColorRes final int colorRes){

        if(Build.VERSION.SDK_INT >= 23)
            return getContext().getColor(colorRes);
        else
            return getContext().getResources().getColor(colorRes);

    }

    public void setTransactions(final List<Transactions> data){

        if(transactionsAdapter != null)
            ((TodayTransactionAdapter)transactionsAdapter).setDataSet(data);

        updateValue(data);
    }

    private Bundle getloaderArguments(){
        //Init the where args with a long value representing todays beginning, and the type we are which is ADD
        final String[] whereArgs = {String.valueOf(new MidnightTimestamp().getTodayMidnightMillis()), String.valueOf(sourceType)};

        final Bundle searchArgs = new Bundle();

        searchArgs.putString(WHERE_KEY, WHERE);
        searchArgs.putStringArray(WHERE_ARGS_KEY, whereArgs);
        searchArgs.putString(SORT_KEY, TransactionsSchema.SORT_BY_TIME_DESC);

        return searchArgs;
    }


    @Override
    public  Loader<Cursor> onCreateLoader(int id, Bundle args){
        final CursorLoader transactionLoader = new TransactionsLoader(getContext());
        transactionLoader.setSelection(args.getString(WHERE_KEY));
        transactionLoader.setSelectionArgs(args.getStringArray(WHERE_ARGS_KEY));
        transactionLoader.setSortOrder(args.getString(SORT_KEY));

        return transactionLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.setData(data);
        setTransactions(cursorAdapter.fromCursor());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**
     * Updates the Total Display value for Today's Transactions
     *
     *
     * @param transactions The current list of transactions
     */
    protected void updateValue(final List<Transactions> transactions){

        long total = 0L;

        for(Transactions t: transactions)
            total += t.getTransactionAmount();

        this.value.setText(String.valueOf(total));

    }

    protected abstract int getLoaderId();

    protected void setValue(final long pennies){
        setValue(String.valueOf(pennies));
    }

    protected void setValue(final String value){
        this.value.setText(value);
    }




}
