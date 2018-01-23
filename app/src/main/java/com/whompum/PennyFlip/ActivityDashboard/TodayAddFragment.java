package com.whompum.PennyFlip.ActivityDashboard;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;

import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Transaction.Models.TransactionType;
import com.whompum.PennyFlip.Transaction.TransactionsCursorAdapter;

/**
 * Created by bryan on 1/12/2018.
 */

public class TodayAddFragment extends TodayFragment {


    {
        super.VALUE_TEXT_COLOR = R.color.light_green;
        super.TYPE = TransactionType.ADD;
    }

    public static final int ADD_LOADER_ID = 15;


    public static TodayFragment newInstance(final Bundle args){
        final TodayAddFragment fragment = new TodayAddFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected int getLoaderId() {
        return ADD_LOADER_ID;
    }



    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.setCursor(data);
        setTransactions(cursorAdapter.fromCursor());
    }



    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }



}
