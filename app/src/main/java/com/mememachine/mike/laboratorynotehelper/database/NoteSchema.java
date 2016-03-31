package com.mememachine.mike.laboratorynotehelper.database;

public class NoteSchema {
    public static final class NoteTable {
        public static final String NAME = "crimes";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String CELLTYPE = "celltype";
            public static final String BODY = "body";
        }
    }
}