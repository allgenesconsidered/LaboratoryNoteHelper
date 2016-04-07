package com.mememachine.mike.laboratorynotehelper.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mememachine.mike.laboratorynotehelper.Note;
import com.mememachine.mike.laboratorynotehelper.Notebook;
import com.mememachine.mike.laboratorynotehelper.database.NoteSchema.NoteTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class NoteBaseHelper extends SQLiteOpenHelper{

    static NoteBaseHelper mNoteBaseHelper;
    //Database Name/Version
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "crimeBase.db";

    public static NoteBaseHelper get(Context context){
        if (mNoteBaseHelper == null){
            mNoteBaseHelper = new NoteBaseHelper(context);
        }
        return mNoteBaseHelper;
    }

    public NoteBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + NoteTable.TABLE_NOTES + "(" +
                "_id integer primary key autoincrement, " +
                        NoteTable.Cols.UUID + "," +
                        NoteTable.Cols.TITLE + "," +
                        NoteTable.Cols.DATE + "," +
                        NoteTable.Cols.CELLTYPE + "," +
                        NoteTable.Cols.BODY + ")"
        );
        db.execSQL("create table " + NoteTable.TABLE_NOTEBOOKS + "(" +
                        "_id integer primary key autoincrement, " +
                        NoteTable.Cols.UUID + "," +
                        NoteTable.Cols.TITLE + "," +
                        NoteTable.Cols.DATE + "," +
                        NoteTable.Cols.COLOR + ")"
        );
        db.execSQL("create table " + NoteTable.TABLE_NOTE_NOTEBOOK_LINKER + "(" +
                        "_id integer primary key autoincrement, " +
                        NoteTable.Cols.NOTE_ID + "," +
                        NoteTable.Cols.NOTEBOOK_ID + ")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<Note> getNotesByNotebookID(String notebookID){
        List<Note> notes = new ArrayList<Note>();
        String selectQuery = "SELECT * FROM " + NoteTable.TABLE_NOTES + " td, "
                + NoteTable.TABLE_NOTEBOOKS + " tg, " + NoteTable.TABLE_NOTE_NOTEBOOK_LINKER
                + " tt WHERE tg." + NoteTable.Cols.UUID + " = '" + notebookID + "'"
                + " AND tg." + NoteTable.Cols.UUID + " = " + "tt." + NoteTable.Cols.NOTEBOOK_ID
                + " AND td." + NoteTable.Cols.UUID + " = " + "tt." + NoteTable.Cols.NOTE_ID;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                String uuidString = c.getString(c.getColumnIndex(NoteSchema.NoteTable.Cols.UUID));
                String title = c.getString(c.getColumnIndex(NoteSchema.NoteTable.Cols.TITLE));
                long date = c.getLong(c.getColumnIndex(NoteSchema.NoteTable.Cols.DATE));
                String cellType = c.getString(c.getColumnIndex(NoteSchema.NoteTable.Cols.CELLTYPE));
                String body = c.getString(c.getColumnIndex(NoteSchema.NoteTable.Cols.BODY));

                Note note = new Note(UUID.fromString(uuidString));
                note.setTitle(title);
                note.setDate(new Date(date));
                note.setCellType(cellType);
                note.setBody(body);

                notes.add(note);
            } while (c.moveToNext());
        }
        return notes;
    }
}
