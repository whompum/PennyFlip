package com.whompum.PennyFlip.DialogSourceChooser;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Transactions.Models.TransactionType;

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

    public static SourceDialog newInstance(@NonNull final Bundle args){
        SourceDialog sourceDialog = new AddSourceDialog();

        args.putLong(TIMESTAMP_KEY, System.currentTimeMillis());

        sourceDialog.setArguments(args);

    return sourceDialog;
    }

    @Override
    protected SourceWrapperAdapter manifestAdapter() {
        this.sourceListAdapter = new SourceWrapperAdapter(getContext(), HEADER_COLOR, null);
    return sourceListAdapter;
    }


}
