package com.whompum.PennyFlip.Notes.Persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.whompum.PennyFlip.Notes.Model.Note;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class NotesDatabase extends RoomDatabase{

    public static final String NAME = "notes.db";

    public abstract NotesDao getDao();
}
