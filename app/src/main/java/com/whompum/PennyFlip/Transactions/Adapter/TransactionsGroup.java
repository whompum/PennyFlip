package com.whompum.PennyFlip.Transactions.Adapter;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.ListUtils.AdapterItem;
import com.whompum.PennyFlip.ListUtils.ExpandableGroup;
import com.whompum.PennyFlip.Transactions.Data.DescendingSort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TransactionsGroup implements ExpandableGroup, AdapterItem {

    private long millis;

    private Set<TransactionsContent> children;

    private boolean isExpanded = false;

    public TransactionsGroup(final long millis){
        this.millis = millis;
        children = new HashSet<>();
    }

    public void toggle(){
        isExpanded = !isExpanded;
    }

    public void addChild(@NonNull final TransactionsContent content){
        children.add(content);
    }

    public List<TransactionsContent> getChildren(){
        return getChildren(new DescendingSort());
    }

    public List<TransactionsContent> getChildren(@NonNull final Comparator<TransactionsContent> c){
        final List<TransactionsContent> data = new ArrayList<>(children);

        Collections.sort(data, c);
        return data;
    }

    public long getMillis() {
        return millis;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

}
