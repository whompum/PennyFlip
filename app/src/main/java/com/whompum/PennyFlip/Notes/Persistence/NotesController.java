package com.whompum.PennyFlip.Notes.Persistence;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Notes.Model.Note;

import java.util.List;

public class NotesController {

    private static NotesController instance;

    private NotesDao accessor;

    private NotesController(@NonNull final Context c){

        this.accessor = Room.databaseBuilder(c.getApplicationContext(),
                NotesDatabase.class, NotesDatabase.NAME)
                .build()
                .getDao();
    }

    public synchronized static NotesController obtain(@NonNull final Context c){
        if(instance == null) instance = new NotesController(c);

        return instance;
    }


    public void insert(@NonNull final Note note){

        new Thread(){
            @Override
            public void run() {
                accessor.insert(note);
            }
        }.start();

    }

    public void fetch(@NonNull final Handler h, final String sourceId){

        new Thread(){
            @Override
            public void run() {

                final Message m = Message.obtain(h);

                final List<Note> notes = accessor.fetch(sourceId);

                m.obj = notes;

                h.sendMessage(m);

            }
        }.start();

    }

    public void fetch(@NonNull final Handler h, final int id){
        new Thread(){
            @Override
            public void run() {

                final Message m = Message.obtain(h);

                final Note note = accessor.fetch(id);

                m.obj = note;

                h.sendMessage(m);

            }
        }.start();
    }

    public void delete(@NonNull final String sourceTitle){
        new Thread(){
            @Override
            public void run() {
                accessor.deleteAll(sourceTitle);
            }
        }.start();
    }

    public void delete(final int id){
        new Thread(){
            @Override
            public void run() {
                accessor.delete(id);
            }
        }.start();
    }

    public void update(@NonNull final Note note){
        new Thread(){
            @Override
            public void run() {
                accessor.updateNoteContent(note);
            }
        }.start();
    }

}
