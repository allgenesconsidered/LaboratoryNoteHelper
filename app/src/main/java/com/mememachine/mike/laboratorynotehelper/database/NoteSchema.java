package com.mememachine.mike.laboratorynotehelper.database;

public class NoteSchema {
    public static final class NoteTable {
        //Table Names
        public static final String TABLE_NOTES = "notes";
        public static final String TABLE_NOTEBOOKS = "notebooks";
        public static final String TABLE_NOTE_NOTEBOOK_LINKER = "linker";

        public static final class Cols{
            //Common Columns
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            //NOTE Table Columns (+ UUID, TITLE, DATE)
            public static final String CELLTYPE = "celltype";
            public static final String BODY = "body";
            //NOTEBOOK Table Columns (+UUID, TITLE, DATE)
            public static final String COLOR = "color";
            //LINKER TAble Columns
            public static final String NOTE_ID = "note_id";
            public static final String NOTEBOOK_ID = "notebook_id";
        }
    }
}