package com.whompum.PennyFlip.Money.Source;

public class NewSourceTotalConstraintException extends RuntimeException {
    public NewSourceTotalConstraintException(){
        super("A new source object must have a total equal to zero");
    }
}
