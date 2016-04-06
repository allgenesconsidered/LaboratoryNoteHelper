package com.mememachine.mike.laboratorynotehelper.database;


import android.database.Cursor;
import android.database.CursorWrapper;

import com.mememachine.mike.laboratorynotehelper.Note;
import com.mememachine.mike.laboratorynotehelper.Notebook;

import java.util.Date;
import java.util.UUID;

public class DatabaseCursorWrapper extends CursorWrapper{
    public DatabaseCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Note getNote(){
        String uuidString = getString(getColumnIndex(NoteSchema.NoteTable.Cols.UUID));
        String title = getString(getColumnIndex(NoteSchema.NoteTable.Cols.TITLE));
        long date = getLong(getColumnIndex(NoteSchema.NoteTable.Cols.DATE));
        String cellType = getString(getColumnIndex(NoteSchema.NoteTable.Cols.CELLTYPE));
        String body = getString(getColumnIndex(NoteSchema.NoteTable.Cols.BODY));

        Note note = new Note(UUID.fromString(uuidString));
        note.setTitle(title);
        note.setDate(new Date(date));
        note.setCellType(cellType);
        note.setBody(body);

        return note;
    }

    public Notebook getNotebook(){
        String uuidString = getString(getColumnIndex(NoteSchema.NoteTable.Cols.UUID));
        String title = getString(getColumnIndex(NoteSchema.NoteTable.Cols.TITLE));
        long date = getLong(getColumnIndex(NoteSchema.NoteTable.Cols.DATE));
        String color = getString(getColumnIndex(NoteSchema.NoteTable.Cols.COLOR));

        Notebook notebook = new Notebook(UUID.fromString(uuidString));
        notebook.setTitle(title);
        notebook.setDate(new Date(date));
        notebook.setColor(color);


        return notebook;
    }
}
