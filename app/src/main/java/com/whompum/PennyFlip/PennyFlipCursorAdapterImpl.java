package com.whompum.PennyFlip;

import android.database.Cursor;

import java.util.List;

/**
 * Created by bryan on 1/30/2018.
 */

public abstract class PennyFlipCursorAdapterImpl<T> implements PennyFlipCursorAdapter<T> {

    protected Cursor data;

    public PennyFlipCursorAdapterImpl(final Cursor data){
        this.data = data;
    }

    @Override
    public abstract List<T> fromCursor();

    @Override
    public void setData(Cursor data){
        this.data = data;
    }
}
