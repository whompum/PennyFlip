package com.whompum.PennyFlip.Notes.Model;

import android.support.annotation.NonNull;

public class NoteWrapper {

    private Note note;

    private boolean expanded = false;

    public NoteWrapper(@NonNull final Note note){
        this.note = note;
    }

    public void toggle(){
        expanded = !expanded;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public Note getNote(){
        return this.note;
    }

}
