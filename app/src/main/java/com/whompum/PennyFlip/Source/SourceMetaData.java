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
    private Timestamp creationDate;
    private Timestamp lastUpdate;
    private long id;

    public SourceMetaData(@NonNull final String sourceName,@NonNull final long pennies,
                          @NonNull final Timestamp creationDate, @NonNull final Timestamp lastUpdate,
                          @NonNull final long id){

        this.sourceName = sourceName;
        this.pennies = pennies;
        this.lastUpdate = lastUpdate;
        this.id = id;
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

    public String getCreationDate(){return PennyFlipTimeFormatter.simpleTime(creationDate);}

    public long getId(){
        return id;
    }
}
