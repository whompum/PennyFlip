package com.whompum.PennyFlip.Sources;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Time.PennyFlipTimeFormatter;
import com.whompum.PennyFlip.Time.Timestamp;

/**
 * Created by bryan on 12/28/2017.
 */

public class SourceMetaData implements Parcelable{

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
        this.creationDate = creationDate;
        this.lastUpdate = lastUpdate;
        this.id = id;
    }


    public SourceMetaData(@NonNull final Parcel parcel){
        this(parcel.readString(), //Source name
                parcel.readLong(), //Amount
                Timestamp.from(parcel.readLong()), // Creation date
                Timestamp.from(parcel.readLong()), // Last Update
                parcel.readLong()); //ID
    }

    public boolean hasLastUpdate(){
        return lastUpdate.millis() != 0;
    }

    public String getSourceName(){
        return sourceName;
    }

    public long getPennies(){
        return pennies;
    }

    public String getLastUpdateSimpleTime(){
        return PennyFlipTimeFormatter.simpleTime(lastUpdate);
    }

    public String getLastUpdateSimpleDate(){
        return PennyFlipTimeFormatter.simpleDate(lastUpdate);
    }

    public long getLastUpdateMillis(){
        return lastUpdate.millis();
    }

    public String getCreationDateSimpleTime(){
        return PennyFlipTimeFormatter.simpleTime(creationDate);
    }

    public String getCreationDateSimpleDate(){
        return PennyFlipTimeFormatter.simpleDate(creationDate);
    }

    public long getCreationDateMillis(){
        return creationDate.millis();
    }


    public long getId(){
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        /**
         * name: String, pennies: long, creationDate: long, lastUpdate: long, id: long
         */

        dest.writeString(sourceName);
        dest.writeLong(pennies);
        dest.writeLong(creationDate.millis());
        dest.writeLong(lastUpdate.millis());
        dest.writeLong(id);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){

        @Override
        public Object createFromParcel(Parcel source) {
            return new SourceMetaData(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new Object[0];
        }
    };


}










