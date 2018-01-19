package com.whompum.PennyFlip.Data.Loader;

import android.content.Context;
import android.support.v4.content.CursorLoader;

import com.whompum.PennyFlip.Data.Schemas.TransactionsSchema;

/**
 * Created by bryan on 1/18/2018.
 */

public class TransactionsLoader extends CursorLoader {


    public TransactionsLoader(final Context context){
        super(context);

        setUri(TransactionsSchema.TransactionTable.URI);
        setProjection(TransactionsSchema.TransactionTable.COLUMNS_NO_ID);

    }


}
