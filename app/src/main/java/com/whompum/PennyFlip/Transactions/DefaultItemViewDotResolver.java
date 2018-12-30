package com.whompum.PennyFlip.Transactions;

import com.whompum.PennyFlip.Money.Transaction.TransactionType;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Transactions.Adapter.BackgroundResolver;

public class DefaultItemViewDotResolver implements BackgroundResolver {

    @Override
    public int getBackground(int transactionType) {
        if( transactionType == TransactionType.INCOME)
            return R.drawable.graphic_timeline_add;

        else if( transactionType == TransactionType.EXPENSE)
            return R.drawable.graphic_timeline_spend;

        return R.color.light_blue;
    }

}
