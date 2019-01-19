package com.whompum.PennyFlip.ActivityStatistics;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Time.Timestamp;


import java.util.ArrayList;
import java.util.List;

public class DateListAdapter extends ArrayAdapter<Long> {

    private static final Timestamp UTILITY = Timestamp.now();

    private List<Long> dataSet = new ArrayList<>( 1 );

    DateListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public Long getItem(int position) {

        if( dataSet == null || dataSet.size() <= position)
            return -1L;

        return dataSet.get( position );
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final View v = (convertView != null) ? convertView : LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.layout_statistics_spinner_view, parent, false);
        
        final TextView display = v.findViewById( R.id.id_global_title );

        UTILITY.set( dataSet.get( position ) );

        display.setText(
                UTILITY.getFormattedDate( Timestamp.FORMAT_MMMM_YYYY )
        );

        return v;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final View v = (convertView != null) ? convertView : LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.layout_statistics_spinner_item_view, parent, false);

        final TextView display = v.findViewById( R.id.id_global_title );

        UTILITY.set( dataSet.get( position ) );

        display.setText(
                UTILITY.getFormattedDate( Timestamp.FORMAT_MMMM_YYYY )
        );

        return v;
    }

    public void swapDataSet(final List<Long> dataSet){
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

}


