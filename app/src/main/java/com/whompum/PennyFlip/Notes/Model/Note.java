package com.whompum.PennyFlip.Notes.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity
public class Note implements Serializable{

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String sourceId;
    private String noteTitle;
    private String content;
    private long lastUpdate;
    private int transactionType;

    public Note(final int id, String sourceId, String noteTitle, String content, long lastUpdate, int transactionType) {
        this.id = id;
        this.sourceId = sourceId;
        this.noteTitle = noteTitle;
        this.content = content;
        this.lastUpdate = lastUpdate;
        this.transactionType = transactionType;
    }

    @Ignore
    public Note(@NonNull final String sourceId, @NonNull final String noteTitle, final int transactionType){
        this.sourceId = sourceId;
        this.noteTitle = noteTitle;
        this.transactionType = transactionType;
        this.lastUpdate = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public String getContent() {
        return content;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

}
