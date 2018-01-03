package com.whompum.PennyFlip.DialogSourceChooser;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.R;

/**
 * Created by bryan on 12/21/2017.
 */

public class SpendingSourceDialog extends SourceDialog {

    {
        HEADER_COLOR = R.color.light_red;
        FAB_SRC = R.drawable.ic_shape_minus_red;
    }


    public static final String TAG = "SpendingSourceDialog";

    public static SourceDialog newInstance(final Bundle args){
        final SourceDialog dialog = new SpendingSourceDialog();
        dialog.setArguments(args);

    return dialog;
    }


    @Override
    protected SourceWrapperAdapter manifestAdapter() {
        this.sourceListAdapter = new SourceWrapperAdapter(getContext(), HEADER_COLOR, new AdapterSelecteable<SourceWrapper>());

        return sourceListAdapter;
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