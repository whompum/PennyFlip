package com.whompum.PennyFlip.Notes.Adapter;

import com.whompum.PennyFlip.Notes.Model.NoteWrapper;

public interface HolderProvider extends Provider{
    NoteWrapper getDataAt(final int position);
    void onItemClick(final int posClicked);
}
