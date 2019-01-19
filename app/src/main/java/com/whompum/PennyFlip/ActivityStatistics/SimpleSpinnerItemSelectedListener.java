package com.whompum.PennyFlip.ActivityStatistics;

import android.view.View;
import android.widget.AdapterView;

public abstract class SimpleSpinnerItemSelectedListener implements AdapterView.OnItemSelectedListener {

    abstract void onItemSelected(final int pos);

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        onItemSelected( position );
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent){

    }
}
