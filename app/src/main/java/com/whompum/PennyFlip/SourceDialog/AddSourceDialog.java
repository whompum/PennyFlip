package com.whompum.PennyFlip.SourceDialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

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


    @Override
    protected void populate(@Nullable CharSequence popData) {
        final List<SourceWrapper> wrappers = new ArrayList<>();

        for(int i = 0; i < 15; i++){
            wrappers.add(new SourceWrapper("TESTING", SourceWrapper.TAG.BASE));
        }
        sourceList.setAdapter(new SourceWrapperAdapter(getContext(), HEADER_COLOR, wrappers));

        Log.i("test", "IS RECYLER NULL: " + String.valueOf(sourceList != null));

    }

    @Override
    protected void onDone() {
        //Puts the final Pieces on the TransactionBuilder object, and then sends it on its way to storage.
    }
}
