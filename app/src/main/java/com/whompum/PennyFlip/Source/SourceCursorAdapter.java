package com.whompum.PennyFlip.Source;

import android.database.Cursor;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.Data.Schemas.SourceSchema;
import com.whompum.PennyFlip.PennyFlipCursorAdapterImpl;
import com.whompum.PennyFlip.Time.Timestamp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bryan on 1/22/2018.
 */

public class SourceCursorAdapter extends PennyFlipCursorAdapterImpl<SourceMetaData> {

    protected static final String TITLE = SourceSchema.SourceTable.COL_TITLE;
    protected static final String TOTAL = SourceSchema.SourceTable.COL_TOTAL;
    protected static final String CREATION_DATE = SourceSchema.SourceTable.COL_CREATION_DATE;
    protected static final String LAST_UPDATE = SourceSchema.SourceTable.COL_LAST_UPDATE;
    protected static final String ID = SourceSchema.SourceTable._ID;



    public SourceCursorAdapter(@Nullable final Cursor cursor){
        super(cursor);
    }

    @Override
    public List<SourceMetaData> fromCursor() {

        final List<SourceMetaData> sourceData = new ArrayList<>();

        if(data != null){

            while (data.moveToNext()){

                final String title = data.getString(titleIndex());
                final long total = data.getLong(totalIndex());
                final long creationDate = data.getLong(creationDateIndex());
                final long lastUpdateDate = data.getLong(lastUpdateIndex());
                final long id = data.getLong(idIndex());

                sourceData.add(new SourceMetaData(title, total, Timestamp.from(creationDate), Timestamp.from(lastUpdateDate), id));

            }

        }


        return sourceData;
    }


    private int titleIndex(){
        if(data ==null) return -1;

     return data.getColumnIndex(TITLE);
    }


    private int totalIndex(){
        if(data == null) return -1;
    return data.getColumnIndex(TOTAL);
    }


    private int creationDateIndex(){
        if(data == null) return -1;

    return data.getColumnIndex(CREATION_DATE);
    }


    private int lastUpdateIndex(){
        if(data == null) return -1;
    return data.getColumnIndex(LAST_UPDATE);
    }

    private int idIndex(){
        if(data == null) return -1;
     return data.getColumnIndex(ID);
    }

}
