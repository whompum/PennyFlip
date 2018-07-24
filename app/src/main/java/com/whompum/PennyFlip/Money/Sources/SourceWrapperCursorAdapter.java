package com.whompum.PennyFlip.Source;

import android.database.Cursor;

import com.whompum.PennyFlip.Data.Schemas.SourceSchema;
import com.whompum.PennyFlip.DialogSourceChooser.AdapterSelecteable;
import com.whompum.PennyFlip.PennyFlipCursorAdapterImpl;

import java.util.List;

/**
 * Created by bryan on 1/30/2018.
 */

public class SourceWrapperCursorAdapter extends PennyFlipCursorAdapterImpl<SourceWrapper> {




//(String title, final SourceWrapper.TAG tag, final int sourceType, final long sourceId, final long originalAmount
    public SourceWrapperCursorAdapter(Cursor data) {
        super(data);
    }

    @Override
    public List<SourceWrapper> fromCursor() {

        /**
         * TITLE
         * sourceType
         * sourceId
         * amount
         */

        final List<SourceWrapper> wrappers = new AdapterSelecteable<>();

        while(data.moveToNext()){

            final long id = data.getLong(data.getColumnIndex(SourceSchema.SourceTable._ID));
            final String title = data.getString(data.getColumnIndex(SourceSchema.SourceTable.COL_TITLE));
            final int type = data.getInt(data.getColumnIndex(SourceSchema.SourceTable.COL_TYPE));
            final long amount = data.getLong(data.getColumnIndex(SourceSchema.SourceTable.COL_TOTAL));

            wrappers.add(new SourceWrapper(title, SourceWrapper.TAG.REGULAR, type, id, amount));
        }


    return wrappers;
    }

}
