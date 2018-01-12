package com.whompum.PennyFlip.Source;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Time.PennyFlipTimeFormatter;
import com.whompum.PennyFlip.Time.Timestamp;

/**
 * Created by bryan on 12/28/2017.
 */

public class SourceMetaData {

    private String sourceName;
    private long pennies;
    private Timestamp lastUpdate;

    public SourceMetaData(@NonNull final String sourceName,@NonNull final long pennies,@NonNull final Timestamp lastUpdate){
        this.sourceName = sourceName;
        this.pennies = pennies;
        this.lastUpdate = lastUpdate;
    }

    public String getSourceName(){
        return sourceName;
    }

    public long getPennies(){
        return pennies;
    }

    public String getLastUpdate(){
        return PennyFlipTimeFormatter.simpleTime(lastUpdate);
    }

}
