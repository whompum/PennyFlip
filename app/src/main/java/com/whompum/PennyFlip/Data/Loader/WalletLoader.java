package com.whompum.PennyFlip.Data.Loader;

import android.content.Context;
import android.support.v4.content.CursorLoader;

import com.whompum.PennyFlip.Data.Schemas.WalletSchema;

/**
 * Created by bryan on 1/17/2018.
 */

public class WalletLoader extends CursorLoader {


    public WalletLoader(final Context context){
        super(context);

        setUri(WalletSchema.Wallet.URI);
        setProjection(WalletSchema.Wallet.COLUMNS_NO_ID);
    }



}
