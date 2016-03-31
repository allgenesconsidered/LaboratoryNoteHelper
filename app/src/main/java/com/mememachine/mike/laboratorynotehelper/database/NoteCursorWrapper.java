package com.mememachine.mike.laboratorynotehelper.database;


import android.database.Cursor;
import android.database.CursorWrapper;

import com.mememachine.mike.laboratorynotehelper.Note;

import java.util.Date;
import java.util.UUID;

public class NoteCursorWrapper extends CursorWrapper{
    public NoteCursorWrapper(Cursor cursor) {
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
}
