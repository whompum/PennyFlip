package com.whompum.PennyFlip.DialogSourceChooser;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Money.Transaction.TransactionType;

/**
 * Created by bryan on 12/21/2017.
 */

public class SpendingSourceDialog extends SourceDialog {

    {
        HEADER_COLOR = R.color.light_red;
        FAB_SRC = R.drawable.ic_shape_minus_red;
        transactionType = TransactionType.SPEND;
    }


    public static final String TAG = "SpendingSourceDialog";

    public static SourceDialog newInstance(@NonNull final Bundle args){
        final SourceDialog dialog = new SpendingSourceDialog();

        args.putLong(TIMESTAMP_KEY, System.currentTimeMillis());

        dialog.setArguments(args);

    return dialog;
    }


    @Override
    protected SourceWrapperAdapter manifestAdapter() {
        this.sourceListAdapter = new SourceWrapperAdapter(getContext(), HEADER_COLOR, new AdapterSelecteable<SourceWrapper>());
        return sourceListAdapter;
    }

}
