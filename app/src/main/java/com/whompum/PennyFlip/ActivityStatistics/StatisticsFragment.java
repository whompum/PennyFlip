package com.whompum.PennyFlip.ActivityStatistics;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.Money.TimeRange;

public class StatisticsFragment extends Fragment implements DateQueryable {

    public static Fragment newInstance(@Nullable final Bundle bundle){
        final StatisticsFragment fragment = new StatisticsFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setDate(@Nullable final TimeRange range) {

    }



}
