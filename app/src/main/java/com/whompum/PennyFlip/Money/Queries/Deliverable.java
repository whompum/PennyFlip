package com.whompum.PennyFlip.Money.Queries;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.Money.OnCancelledResponder;

public class Deliverable<T> {

    private Responder<T> responder;
    private OnCancelledResponder cancelledResponder;

    private T data;

    private boolean hasDelivered = false;
    private boolean overrideNewResponder = true;

    public Deliverable<T> attachResponder(@NonNull final Responder<T> responder){

        //Assign the Responder of data. IF responder isn't null, or responder can override original.
        if( ( hasResponder() && overrideNewResponder) ||
                (this.responder == null) )
            this.responder = responder;

        //Now we can check if we should deliver data or not
        //We only deliver from this method, IF a responder is added AFTER data has been found

        if( isWaitingToDeliver() ) //We can deliver
            deliver();

        return this;
    }

    public Deliverable<T> attachCancelledResponder(@NonNull final OnCancelledResponder responder){
        cancelledResponder = responder;
        return this;
    }

    public void setData(@NonNull final T data){
        this.data = data;

        if( isWaitingToDeliver() && hasResponder() ) //Check if its waiting to deliver
            deliver();

    }

    public void setCancelledResponse(final int reason, @Nullable final String msg){
        if( cancelledResponder != null )
            cancelledResponder.onCancelledResponse(reason, msg);

        hasDelivered = true;
    }

    private void deliver(){
        responder.onActionResponse( data );
        hasDelivered = true;
    }

    private boolean isWaitingToDeliver(){
        return !hasDelivered && data != null;
    }

    private boolean hasResponder(){
        return this.responder != null;
    }

    public void setOverrideNewResponder(final boolean overrideNewResponder){
        this.overrideNewResponder = false;
    }

}
