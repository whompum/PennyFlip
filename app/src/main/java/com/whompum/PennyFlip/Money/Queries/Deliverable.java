package com.whompum.PennyFlip.Money.Queries;

import android.support.annotation.NonNull;

public class Deliverable<T> {

    private Responder<T> responder;
    private T data;

    private boolean hasDelivered = false;
    private boolean overrideNewResponder = true;

    public void attachResponder(@NonNull final Responder<T> responder){

        //Assign the Responder of data. IF responder isn't null, or responder can override original.
        if( ( hasResponder() && overrideNewResponder) ||
                (this.responder == null) )
            this.responder = responder;

        //Now we can check if we should deliver data or not
        //We only deliver from this method, IF a responder is added AFTER data has been found

        if( isWaitingToDeliver() ) //We can deliver
            deliver();

    }

    public void setData(@NonNull final T data){
        this.data = data;

        if( isWaitingToDeliver() && hasResponder() ) //Check if its waiting to deliver
            deliver();

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
