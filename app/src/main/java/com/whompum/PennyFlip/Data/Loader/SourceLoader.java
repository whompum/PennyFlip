package com.whompum.PennyFlip.Data.Loader;

import android.content.Context;
import android.support.v4.content.CursorLoader;

import com.whompum.PennyFlip.Data.Schemas.SourceSchema;


/**
 * Created by bryan on 1/19/2018.
 */

public class SourceLoader extends CursorLoader {


    public SourceLoader(final Context context){
        super(context);

        setUri(SourceSchema.SourceTable.URI);
        setProjection(SourceSchema.SourceTable.COLUMNS_WITH_ID);
    }



}




















