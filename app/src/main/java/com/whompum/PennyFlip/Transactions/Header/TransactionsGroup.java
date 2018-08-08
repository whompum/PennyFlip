package com.whompum.PennyFlip.Transactions.Header;

import com.whompum.PennyFlip.ListUtils.AdapterItem;
import com.whompum.PennyFlip.ListUtils.ExpandableGroup;

public class TransactionsGroup implements ExpandableGroup, AdapterItem {

    private long millis;
    private int groupPosition;

    public TransactionsGroup(final long millis){
        this.millis = millis;
    }

    public TransactionsGroup(final long millis, int groupPosition) {
        this.millis = millis;
        this.groupPosition = groupPosition;
    }

    @Override
    public void setPosition(int position) {
        this.groupPosition = position;
    }

    @Override
    public int getPosition() {
        return groupPosition;
    }

    public long getMillis() {
        return millis;
    }
}
