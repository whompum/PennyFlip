package com.whompum.PennyFlip.Notes.Persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Notes.Model.Note;

import java.util.List;

@Dao
public abstract class NotesDao {

    @Query("SELECT * FROM Note WHERE sourceId = :sourceId")
    abstract List<Note> fetch(@NonNull final String sourceId);

    @Query("SELECT * FROM  Note WHERE id = :id")
    abstract Note fetch(final int id);

    @Query("DELETE FROM Note WHERE sourceId = :sourceId")
    abstract void deleteAll(@NonNull final String sourceId);

    @Query("DELETE FROM Note WHERE id = :id")
    abstract void delete(@NonNull final int id);

    @Query("UPDATE Note SET lastUpdate = :lastUpdate WHERE id = :id")
    abstract void updateTimestamp(final long lastUpdate, final int id);

    @Query("UPDATE Note SET content = :content WHERE id = :id")
    abstract void updateContent(final String content, final int id);

    @Insert
    abstract void insert(@NonNull final Note note);

    public void updateNoteContent(@NonNull final Note note){

        final int id = note.getId();

        updateTimestamp(note.getLastUpdate(), id);
        updateContent(note.getContent(), id);
    }

}
