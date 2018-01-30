package com.whompum.PennyFlip;

import android.database.Cursor;

import java.util.List;

/**
 * Created by bryan on 1/22/2018.
 */

public interface PennyFlipCursorAdapter<D> {
    List<D> fromCursor();
    void setData(final Cursor data);
}
