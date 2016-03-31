package com.mememachine.mike.laboratorynotehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mememachine.mike.laboratorynotehelper.database.NoteBaseHelper;
import com.mememachine.mike.laboratorynotehelper.database.NoteCursorWrapper;
import com.mememachine.mike.laboratorynotehelper.database.NoteSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class ListOfNotes {
    //Stores information about each note
    private static ListOfNotes sListOfNotes;
    private Context mContext;
    private SQLiteDatabase mSQLiteDatabase;

    public static ListOfNotes get(Context context){
        if (sListOfNotes == null){
            sListOfNotes = new ListOfNotes(context);
        }
        return sListOfNotes;
    }

    private ListOfNotes(Context context){
        mContext = context.getApplicationContext();
        mSQLiteDatabase = new NoteBaseHelper(mContext)
                .getWritableDatabase();
    }
    public List<Note> getNotes(){
        //Get all notes form the SQLite db.
        List<Note> notes = new ArrayList<>();
        NoteCursorWrapper cursorWrapper = queryNotes(null, null);

        try{
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()){
                notes.add(cursorWrapper.getNote());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }
        return notes;
    }

    public void addNote(Note n) {
        ContentValues values = getContentValues(n);
        mSQLiteDatabase.insert(NoteSchema.NoteTable.NAME, null, values);
    }

    public void deleteNote(Note n) {
        String id = n.getID().toString();
        mSQLiteDatabase.delete(NoteSchema.NoteTable.NAME,
                NoteSchema.NoteTable.Cols.UUID + "= ?", new String[] {id});
    }

    public Note getNote(UUID id){
        //Returns a note with a certain ID
        NoteCursorWrapper cursorWrapper = queryNotes(
                NoteSchema.NoteTable.Cols.UUID + " = ?",
                new String[] {id.toString()}
        );

        try {
            if (cursorWrapper.getCount() == 0) {
                return null;
            }

            cursorWrapper.moveToFirst();
            return cursorWrapper.getNote();
        } finally {
            cursorWrapper.close();
        }
    }

    public void updateNote(Note note) {
        String uuidString = note.getID().toString();
        ContentValues values = getContentValues(note);
        mSQLiteDatabase.update(NoteSchema.NoteTable.NAME, //Table
                values, //values to write
                NoteSchema.NoteTable.Cols.UUID + " = ?", // which col to update
                new String[] { uuidString }); // Arguments
    }

    private static ContentValues getContentValues(Note note){
        //Retreve specific values from a database fro editing.
        ContentValues values = new ContentValues();
        values.put(NoteSchema.NoteTable.Cols.UUID, note.getID().toString());
        values.put(NoteSchema.NoteTable.Cols.TITLE, note.getTitle());
        values.put(NoteSchema.NoteTable.Cols.DATE, note.getDate().getTime());
        values.put(NoteSchema.NoteTable.Cols.CELLTYPE, note.getCellType());
        values.put(NoteSchema.NoteTable.Cols.BODY, note.getBody());

        return values;
    }

    private NoteCursorWrapper queryNotes(String whereClause, String[] whereArgs) {
        //Read in data to the table.
        Cursor cursor = mSQLiteDatabase.query(
                NoteSchema.NoteTable.NAME,
                null, //Columns, null for all columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, //having?
                null //orderBy
        );
        return new NoteCursorWrapper(cursor);
    }

}
