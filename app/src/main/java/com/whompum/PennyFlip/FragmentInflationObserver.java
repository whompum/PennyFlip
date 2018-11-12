package com.whompum.PennyFlip;

import android.support.annotation.NonNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class FragmentInflationObserver implements InflationObserver{

    private Set<InflationOperation> subscribers;

    public FragmentInflationObserver() {
        this.subscribers = new HashSet<>();
    }

    @Override
    public boolean subscribe(@NonNull InflationOperation operation) {
        return subscribers.add( operation );
    }

    @Override
    public void onViewInflated() {
        final Iterator<InflationOperation> subscriberIterator
                = subscribers.iterator();

        while( subscriberIterator.hasNext() ){
            subscriberIterator.next()
                    .onInflated();
        }

    }

}
