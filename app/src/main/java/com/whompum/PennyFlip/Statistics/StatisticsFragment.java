package com.whompum.PennyFlip.Statistics;

import android.content.Context;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Sources.SourceStatistic;
import com.whompum.PennyFlip.Time.TimeRange;
import com.whompum.PennyFlip.Widgets.TotalView;

import currencyedittext.whompum.com.currencyedittext.CurrencyEditText;

/**
 * Created by bryan on 12/31/2017.
 */

public class StatisticsFragment extends Fragment implements View.OnClickListener, Populator<SourceStatistic>{

    public static final String SOURCE_PENNY_KEY = "SourcePennyKey";

    private TextView timePeriodLabel;
    private CurrencyEditText timePeriodTotal;
    private AppCompatImageButton timePeriodIcon;

    private TotalView valueCircle;

    private TextView statMessage;

    private CurrencyEditText sourceTotal;

    private StatisticsServer<TimeRange> statisticsServer;

    private TimeRange timeRange = TimeRange.ALL_TIME;

    private SourceStatistic data = null;

    private boolean pendingData = false;

    public static Fragment newInstance(final long sourcePennies){
        return newInstance(sourcePennies, null);
    }

    public static Fragment newInstance(final long sourcePennies, final StatisticsServer<TimeRange> server){
        final Fragment fragment = new StatisticsFragment();
        ((StatisticsFragment)fragment).registerStatisticsServer(server);

        final Bundle args = new Bundle();
        args.putLong(SOURCE_PENNY_KEY, sourcePennies);

        fragment.setArguments(args);

        return fragment;
    }

    public void registerStatisticsServer(final StatisticsServer<TimeRange> server){

        if(statisticsServer != null)
            unRegisterStatisticsServer();

        if(server!=null)
        this.statisticsServer = server;

    }

    public void unRegisterStatisticsServer(){
        this.statisticsServer = null;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View layout = inflater.inflate(R.layout.layout_statistics, container, false);

       timePeriodLabel = layout.findViewById(R.id.id_source_stats_time_period_label);
       timePeriodTotal = layout.findViewById(R.id.id_source_stats_time_period_total);
       timePeriodIcon = layout.findViewById(R.id.id_source_stats_time_period_icon);

       valueCircle = layout.findViewById(R.id.id_source_stats_value);

       statMessage = layout.findViewById(R.id.id_source_stats_summary);

       sourceTotal = layout.findViewById(R.id.id_source_stats_source_total);

       if(pendingData)
           updateData();

       final long sourcePennies = getArguments().getLong(SOURCE_PENNY_KEY);

       sourceTotal.setText(String.valueOf(sourcePennies));

    return layout;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            this.statisticsServer = (StatisticsServer) context;
        }catch (ClassCastException e){
            e.printStackTrace();
        }

    }

    private void requestDataForTimeRange(final TimeRange timeRange){
        if(statisticsServer != null)
            statisticsServer.onDataRequested(timeRange);
    }


    private void updateTimeRange(final TimeRange timeRange){
        this.timeRange = timeRange;
    }

    private void updateMessage(final int valuePercent){
        final StringBuilder builder = new StringBuilder();



    }

    @Override
    public void populate(SourceStatistic sourceStatistic) {
        //Recieve information from server

        if(data == null & !pendingData) {
            data = sourceStatistic;
            pendingData = true;
            return;
        }

        else{
            data = sourceStatistic;
            updateData();
        }

    }


    private void updateData(){
        valueCircle.computeAndSetValue(getArguments().getLong(SOURCE_PENNY_KEY), data.getTimeRangePennies());
        this.timePeriodTotal.setText(String.valueOf(data.getTimeRangePennies()));
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == timePeriodIcon.getId())
            s();
    }

    private void s(){

    }



    public interface StatisticsServer<TimeRange>{ //Used when data is request from a server
        void onDataRequested(final TimeRange o);
    }



}





















