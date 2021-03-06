package com.mememachine.mike.laboratorynotehelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.mememachine.mike.laboratorynotehelper.general.SingleFragmentActivity;

import java.util.UUID;


public class NoteListActivity extends SingleFragmentActivity {

    private static final String EXTRA_NOTEBOOK_ID =
            "com.mememachine.mike.laboratorynotehelper.notebook_id";
    static UUID mNotebookUUID;

    @Override
    public void onCreate(Bundle savedInstanceState){
        if (getIntent().hasExtra(EXTRA_NOTEBOOK_ID)){
            mNotebookUUID = (UUID) getIntent().getSerializableExtra(EXTRA_NOTEBOOK_ID);}
        Notebook notebook = DatabaseFunctions.get(this).getNotebook(mNotebookUUID);
        setTitle(notebook.getTitle());
        super.onCreate(savedInstanceState);
    }
    //First class called at start of application. Instantiates NoteListFragment.class.
    @Override
    protected Fragment createFragment() {
        return NoteListFragment.newInstance(mNotebookUUID);
    }

    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext, NoteListActivity.class);
        return intent;
    }

    public static Intent newIntent(Context packageContext, UUID notebookID){
        Intent intent = new Intent(packageContext, NoteListActivity.class);
        intent.putExtra(EXTRA_NOTEBOOK_ID, notebookID);
        return intent;
    }
}
