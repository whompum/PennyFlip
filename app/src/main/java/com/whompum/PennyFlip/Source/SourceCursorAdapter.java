package com.whompum.PennyFlip.Source;

import android.database.Cursor;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.whompum.PennyFlip.Data.Schemas.SourceSchema;
import com.whompum.PennyFlip.PennyFlipCursorAdapter;
import com.whompum.PennyFlip.Time.Timestamp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bryan on 1/22/2018.
 */

public class SourceCursorAdapter implements PennyFlipCursorAdapter<SourceMetaData> {

    protected static final String TITLE = SourceSchema.SourceTable.COL_TITLE;
    protected static final String TOTAL = SourceSchema.SourceTable.COL_TITLE;
    protected static final String CREATION_DATE = SourceSchema.SourceTable.COL_CREATION_DATE;
    protected static final String LAST_UPDATE = SourceSchema.SourceTable.COL_LAST_UPDATE;
    protected static final String ID = SourceSchema.SourceTable._ID;


    private Cursor cursor;


    public SourceCursorAdapter(@Nullable final Cursor cursor){
        this.cursor = cursor;
    }

    @Override
    public List<SourceMetaData> fromCursor() {

        final List<SourceMetaData> sourceData = new ArrayList<>();

        if(cursor != null){

            while (cursor.moveToNext()){

                final String title = cursor.getString(titleIndex());
                final long total = cursor.getLong(totalIndex());
                final long creationDate = cursor.getLong(creationDateIndex());
                final long lastUpdateDate = cursor.getLong(lastUpdateIndex());
                final long id = cursor.getLong(idIndex());

                Log.i("SOURCE_DATA", "TYPE: " + String.valueOf(cursor.getInt(cursor.getColumnIndex(SourceSchema.SourceTable.COL_TYPE))));

                sourceData.add(new SourceMetaData(title, total, Timestamp.from(creationDate), Timestamp.from(lastUpdateDate), id));

            }

        }


        return sourceData;
    }

    @Override
    public void setCursor(@Nullable Cursor cursor) {
        this.cursor = cursor;
    }


    private int titleIndex(){
        if(cursor==null) return -1;

     return cursor.getColumnIndex(TITLE);
    }


    private int totalIndex(){
        if(cursor == null) return -1;
    return cursor.getColumnIndex(TOTAL);
    }


    private int creationDateIndex(){
        if(cursor == null) return -1;

    return cursor.getColumnIndex(CREATION_DATE);
    }


    private int lastUpdateIndex(){
        if(cursor == null) return -1;
    return cursor.getColumnIndex(LAST_UPDATE);
    }

    private int idIndex(){
        if(cursor == null) return -1;
     return cursor.getColumnIndex(ID);
    }

}
