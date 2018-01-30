package com.whompum.PennyFlip.DialogSourceChooser;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Source.SourceWrapper;
import com.whompum.PennyFlip.Transaction.Models.TransactionType;

import java.util.Iterator;

/**
 * Created by bryan on 12/21/2017.
 */

public class AddSourceDialog extends SourceDialog {

    {
        HEADER_COLOR = R.color.light_green;
        FAB_SRC = R.drawable.ic_shape_plus_green;
        transactionType = TransactionType.ADD;
    }

    public static final String TAG = "AddSourceDialog";

    public static SourceDialog newInstance(final Bundle args){
        SourceDialog sourceDialog = new AddSourceDialog();
        sourceDialog.setArguments(args);

    return sourceDialog;
    }


    @Override //Creates the adapter. Normally this guy would comb through a Database or file.
    protected SourceWrapperAdapter manifestAdapter() {
        this.sourceListAdapter = new SourceWrapperAdapter(getContext(), HEADER_COLOR, null);
    return sourceListAdapter;
    }


}
