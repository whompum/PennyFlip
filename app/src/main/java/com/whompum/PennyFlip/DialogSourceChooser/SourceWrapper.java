package com.whompum.PennyFlip.DialogSourceChooser;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Source.Source;

import java.io.Serializable;

public class SourceWrapper implements Serializable{

    public static final String EMPTY = "EMPTY";

    private TAG tag;
    private Source source;

    public SourceWrapper(@NonNull final Source source, @NonNull final TAG tag){
        this.source = source;
        this.tag = tag;
    }

    public TAG getTag() {
        return tag;
    }

    public String getSourceId() {
        if(source.getTitle().equals(""))
            return EMPTY;

        return source.getTitle();
    }

    public Source getSource() {
        return source;
    }

    public enum TAG {REGULAR, NEW}

}
