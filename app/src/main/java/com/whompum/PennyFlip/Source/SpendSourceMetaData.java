package com.whompum.PennyFlip.Source;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Source.SourceMetaData;
import com.whompum.PennyFlip.Time.Timestamp;

/**
 * Created by bryan on 12/29/2017.
 */

public class SpendSourceMetaData extends SourceMetaData {

    private int purseValue;

    public static final int NO_PURSE = Integer.MIN_VALUE;

    public SpendSourceMetaData(final int purseValue, @NonNull final String sourceName,
                               @NonNull final long pennies, @NonNull Timestamp creationDate,
                               @NonNull final Timestamp lastUpdate,
                               final long id){
        super(sourceName, pennies, creationDate,  lastUpdate, id);
        this.purseValue = purseValue;
    }

    public void setPurseValue(final int purseValue){
        this.purseValue = purseValue;
    }

    public int getPurseValue(){
        return purseValue;
    }

}
