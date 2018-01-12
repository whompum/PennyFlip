package com.whompum.PennyFlip.Source;


import android.support.annotation.IntRange;

/**
 * Created by bryan on 1/1/2018.
 */

public class SourceStatistic {

    private long timeRangePennies;

    public SourceStatistic( @IntRange(from = 0) final long timeRangePennies){
        this.timeRangePennies = timeRangePennies;
    }

    public long getTimeRangePennies(){
        return timeRangePennies;
    }

}
