package com.mememachine.mike.laboratorynotehelper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mememachine.mike.laboratorynotehelper.Note;
import com.mememachine.mike.laboratorynotehelper.Notebook;
import com.mememachine.mike.laboratorynotehelper.database.NoteSchema.NoteTable;


public class NoteBaseHelper extends SQLiteOpenHelper{
    //Database Name/Version
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "crimeBase.db";

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
                        NoteTable.Cols.UUID + "," +
                        NoteTable.Cols.NOTE_ID + "," +
                        NoteTable.Cols.NOTEBOOK_ID +  ")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
