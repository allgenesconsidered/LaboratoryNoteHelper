package com.mememachine.mike.laboratorynotehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.mememachine.mike.laboratorynotehelper.database.NoteBaseHelper;
import com.mememachine.mike.laboratorynotehelper.database.DatabaseCursorWrapper;
import com.mememachine.mike.laboratorynotehelper.database.NoteSchema;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class DatabaseFunctions {
    //Stores information about each note
    private static DatabaseFunctions sCDatabaseFunctions;
    private Context mContext;
    private SQLiteDatabase mSQLiteDatabase;

    public static DatabaseFunctions get(Context context){
        if (sCDatabaseFunctions == null){
            sCDatabaseFunctions = new DatabaseFunctions(context);
        }
        return sCDatabaseFunctions;
    }

    private DatabaseFunctions(Context context){
        mContext = context.getApplicationContext();
        mSQLiteDatabase = new NoteBaseHelper(mContext)
                .getWritableDatabase();
    }
    //General Functions -------------------------------------------------------
    ///////////////////////////////////////////////////////////////////////////
    private DatabaseCursorWrapper queryDatabases(String whereClause, String[] whereArgs, String table) {
        //Read in data to the table.
        Cursor cursor = mSQLiteDatabase.query(
                table,
                null, //Columns, null for all columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, //having?
                null //orderBy
        );
        return new DatabaseCursorWrapper(cursor);
    }

    //Note Functions -------------------------------------------------------
    /////////////////////////////////////////////////////////////////////////
    public List<Note> getNotes(){
        //Get all notes form the SQLite db.
        List<Note> notes = new ArrayList<>();
        DatabaseCursorWrapper cursorWrapper = queryDatabases(
                null,
                null,
                NoteSchema.NoteTable.TABLE_NOTES);
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


    public void addNote(Note n, UUID notebookID) {
        ContentValues valuesNote = getContentValues(n);
        ContentValues valuesLinker = getContentValues(n, notebookID);
        mSQLiteDatabase.insert(NoteSchema.NoteTable.TABLE_NOTES, null, valuesNote);
        mSQLiteDatabase.insert(NoteSchema.NoteTable.TABLE_NOTE_NOTEBOOK_LINKER, null, valuesLinker);
    }

    public void deleteNote(Note n) {
        String id = n.getID().toString();
        mSQLiteDatabase.delete(NoteSchema.NoteTable.TABLE_NOTES,
                NoteSchema.NoteTable.Cols.UUID + "= ?", new String[]{id});
        mSQLiteDatabase.delete(NoteSchema.NoteTable.TABLE_NOTE_NOTEBOOK_LINKER,
                NoteSchema.NoteTable.Cols.NOTE_ID + "= ?", new String[]{id});
    }

    public Note getNote(UUID id){
        //Returns a note with a certain ID
        DatabaseCursorWrapper cursorWrapper = queryDatabases(
                NoteSchema.NoteTable.Cols.UUID + " = ?",
                new String[]{id.toString()},
                NoteSchema.NoteTable.TABLE_NOTES
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

    public File getPhotoFile(Note note){
        File externalFilesDir = mContext
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (externalFilesDir == null){
            return null;
        }
        return new File(externalFilesDir, note.getPhotoFilename());
    }

    public void updateNote(Note note) {
        String uuidString = note.getID().toString();
        ContentValues values = getContentValues(note);
        mSQLiteDatabase.update(NoteSchema.NoteTable.TABLE_NOTES, //Table
                values, //values to write
                NoteSchema.NoteTable.Cols.UUID + " = ?", // which col to update
                new String[]{uuidString}); // Arguments
    }

    private static ContentValues getContentValues(Note note){
        //Retrieve specific values from a database for editing.
        ContentValues values = new ContentValues();
        values.put(NoteSchema.NoteTable.Cols.UUID, note.getID().toString());
        values.put(NoteSchema.NoteTable.Cols.TITLE, note.getTitle());
        values.put(NoteSchema.NoteTable.Cols.DATE, note.getDate().getTime());
        values.put(NoteSchema.NoteTable.Cols.CELLTYPE, note.getCellType());
        values.put(NoteSchema.NoteTable.Cols.BODY, note.getBody());

        return values;
    }

    private static ContentValues getContentValues(Note note, UUID notebookID){
        ContentValues values = new ContentValues();
        values.put(NoteSchema.NoteTable.Cols.NOTE_ID, note.getID().toString());
        values.put(NoteSchema.NoteTable.Cols.NOTEBOOK_ID, notebookID.toString());
        return values;
    }

    //Notebook Functions ------------------------------------------------------
    ///////////////////////////////////////////////////////////////////////////

    public List<Notebook> getNotebooks(){
        //Get all notes form the SQLite db.
        //No arguments, Outputs a List of Notebook items.
        List<Notebook> notebooks = new ArrayList<>();
        DatabaseCursorWrapper cursorWrapper = queryDatabases(null, null, NoteSchema.NoteTable.TABLE_NOTEBOOKS);

        try{
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()){
                notebooks.add(cursorWrapper.getNotebook());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }
        return notebooks;
    }

    public Notebook getNotebook(UUID id) {
        //Returns a notebook with a certain ID
        DatabaseCursorWrapper cursorWrapper = queryDatabases(
                NoteSchema.NoteTable.Cols.UUID + " = ?",
                new String[]{id.toString()},
                NoteSchema.NoteTable.TABLE_NOTEBOOKS
        );
        try {
            if (cursorWrapper.getCount() == 0) {
                return null;
            }

            cursorWrapper.moveToFirst();
            return cursorWrapper.getNotebook();
        } finally {
            cursorWrapper.close();
        }
    }

    public void addNotebook(Notebook nb) {
        ContentValues values = getContentValues(nb);
        mSQLiteDatabase.insert(NoteSchema.NoteTable.TABLE_NOTEBOOKS, null, values);
    }

    public void deleteNotebook(Notebook nb) {
        String id = nb.getID().toString();
        mSQLiteDatabase.delete(NoteSchema.NoteTable.TABLE_NOTEBOOKS,
                NoteSchema.NoteTable.Cols.UUID + "= ?", new String[]{id});
        mSQLiteDatabase.delete(NoteSchema.NoteTable.TABLE_NOTE_NOTEBOOK_LINKER,
                NoteSchema.NoteTable.Cols.NOTEBOOK_ID + "= ?", new String[]{id});
    }

    public void updateNotebook(Notebook nb) {
        //TODO, allow the user to edit a notebook's title.
        String uuidString = nb.getID().toString();
        ContentValues values = getContentValues(nb);
        mSQLiteDatabase.update(NoteSchema.NoteTable.TABLE_NOTEBOOKS, //Table
                values, //values to write
                NoteSchema.NoteTable.Cols.UUID + " = ?", // which col to update
                new String[]{uuidString}); // Argument
    }

    private static ContentValues getContentValues(Notebook notebook){
        //Retrieve specific values from a database for editing.
        ContentValues values = new ContentValues();
        values.put(NoteSchema.NoteTable.Cols.UUID, notebook.getID().toString());
        values.put(NoteSchema.NoteTable.Cols.TITLE, notebook.getTitle());
        values.put(NoteSchema.NoteTable.Cols.DATE, notebook.getDate().getTime());
        values.put(NoteSchema.NoteTable.Cols.COLOR, notebook.getColor());

        return values;
    }
}
