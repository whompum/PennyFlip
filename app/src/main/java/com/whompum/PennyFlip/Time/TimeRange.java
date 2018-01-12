package com.whompum.PennyFlip.Time;

/**
 * Created by bryan on 1/1/2018.
 *
 * Class of predefined constant values
 * Stored in an object for ease of use
 *
 */

public class TimeRange {


    public static final String $ALL_TIME = "ALL TIME";
    public static final TimeRange ALL_TIME = new TimeRange($ALL_TIME);

    public static final String $TODAY = "TODAY";
    public static final TimeRange TODAY = new TimeRange($TODAY);

    public static final String $THIS_WEEK = "This Week";
    public static final TimeRange THIS_WEEK = new TimeRange($THIS_WEEK);

    public static final String $THIS_MONTH = "This Month";
    public static final TimeRange THIS_MONTH = new TimeRange($THIS_MONTH);

    public static final String $THIS_YEAR = "This Year";
    public static final TimeRange THIS_YEAR = new TimeRange($THIS_YEAR);


    private String timeRange = null;

    private TimeRange(final String timeRange){
        this.timeRange = timeRange;
    }

    public String getTimeRange(){
        return timeRange;
    }


    @Override
    public boolean equals(Object obj) {

        if( !(obj instanceof TimeRange) )
            return false;
        else if( ((TimeRange)obj).getTimeRange().equals(this.getTimeRange()) )
            return true;
    return false;
    }


}
