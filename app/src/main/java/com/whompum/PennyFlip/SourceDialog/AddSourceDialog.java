package com.whompum.PennyFlip.SourceDialog;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.animation.AnticipateInterpolator;

import com.whompum.PennyFlip.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bryan on 12/21/2017.
 */

public class AddSourceDialog extends SourceDialog {

    static{
        HEADER_COLOR = R.color.light_green;
        FAB_SRC = R.drawable.ic_shape_plus;
    }

    public static SourceDialog newInstance(final Bundle args){
        SourceDialog sourceDialog = new AddSourceDialog();
        sourceDialog.setArguments(args);

    return sourceDialog;
    }


    @Override //Creates the adapter. Normally this guy would comb through a Database or file.
    protected SourceWrapperAdapter manifestAdapter() {
        this.sourceListAdapter = new SourceWrapperAdapter(getContext(), HEADER_COLOR, new AdapterSelecteable<SourceWrapper>());

    return sourceListAdapter;
    }

    @Override
    protected void populate(@Nullable CharSequence popData) {

    }

    @Override
    protected void onDone() {
        super.onDone();
        //Puts the final Pieces on the TransactionBuilder object, and then sends it on its way to storage.
    }
}
