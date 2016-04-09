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
    private static final String DATABASE_NAME = "labRatDataBase.db";

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
}
