package com.mememachine.mike.laboratorynotehelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;


import com.mememachine.mike.laboratorynotehelper.general.SingleFragmentActivity;

import java.util.UUID;

public class NoteEditorActivity extends SingleFragmentActivity {

    private static final String EXTRA_NOTE_ID_NUM =
            "com.mememachine.mike.laboratorynotehelper.note_id";

    @Override
    protected Fragment createFragment() {
        UUID uuid = (UUID) getIntent()
                .getSerializableExtra(EXTRA_NOTE_ID_NUM);
        return NoteEditorFragment.newInstance(uuid);
    }

    public static Intent newIntent(Context packageContext, UUID uuid){
        Intent intent = new Intent(packageContext, NoteEditorActivity.class);
        intent.putExtra(EXTRA_NOTE_ID_NUM, uuid);
        return intent;
    }
    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("Cancel Edits")
                .setMessage("Are you sure you want to cancel editing this note?")
                .setNegativeButton("Oops..", null)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NoteEditorActivity.super.onBackPressed();
                            }
                        }).create().show();
    }

}
