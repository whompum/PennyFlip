package com.whompum.PennyFlip.ActivityDashboard;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.widget.Toast;

import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Transaction.Models.TransactionType;
import com.whompum.PennyFlip.Transaction.TransactionsCursorAdapter;

/**
 * Created by bryan on 1/12/2018.
 */

public class TodaySpendFragment extends TodayFragment {


    {
        super.VALUE_TEXT_COLOR = R.color.light_red;
        super.TYPE = TransactionType.SPEND;
    }


    public static final int SPEND_LOADER_ID = 16;

    public static TodayFragment newInstance(final Bundle args){
        final TodaySpendFragment fragment = new TodaySpendFragment();
        fragment.setArguments(args);

        return fragment;
    }



    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.setCursor(data);
        setTransactions(cursorAdapter.fromCursor());
    }



    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }



    @Override
    protected int getLoaderId() {
        return SPEND_LOADER_ID;
    }


}
