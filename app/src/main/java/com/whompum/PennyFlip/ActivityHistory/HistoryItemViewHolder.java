package com.whompum.PennyFlip.ActivityHistory;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Transactions.Adapter.TransactionsContent;
import com.whompum.PennyFlip.Transactions.Adapter.ViewHolder.TransactionHolder;

public class HistoryItemViewHolder extends TransactionHolder {

    private TextView sourceTV;

    public HistoryItemViewHolder(View layout) {
        super(layout);

        sourceTV = layout.findViewById( R.id.id_local_source_title );

    }

    @Override
    public void bind(@NonNull TransactionsContent item) {
        super.bind(item);

        sourceTV.setText( item.getTransaction().getSourceId() );
    }
}
