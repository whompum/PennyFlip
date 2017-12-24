package com.whompum.PennyFlip.SourceDialog;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.animation.AnticipateInterpolator;

import com.whompum.PennyFlip.R;

/**
 * Created by bryan on 12/21/2017.
 */

public class SpendingSourceDialog extends SourceDialog {

    static{
        HEADER_COLOR = R.color.light_red;
        FAB_SRC = R.drawable.ic_shape_minus;
    }


    public SourceDialog newInstance(final Bundle args){
        final SourceDialog dialog = new SpendingSourceDialog();
        dialog.setArguments(args);

    return dialog;
    }


    @Override
    protected SourceWrapperAdapter manifestAdapter() {
        //Creates the Adapter specific for itself, and gives to the RecylerView
        //This method helps ensure that every child generates the same Adapter
        return null;
    }

    @Override
    protected void populate(@Nullable CharSequence popData) {
        //Populate RECYCLER VIEW
    }

    @Override
    protected void onDone() {
        super.onDone();
        //Puts the final Pieces on the TransactionBuilder object, and then sends it on its way to storage.
    }
}
