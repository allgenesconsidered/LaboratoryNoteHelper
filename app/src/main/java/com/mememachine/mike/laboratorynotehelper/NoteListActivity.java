package com.mememachine.mike.laboratorynotehelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;


public class NoteListActivity extends SingleFragmentActivity {
    //First class called at start of application. Instantiates NoteListFragment.class.
    @Override
    protected Fragment createFragment() {
        return new NoteListFragment();
    }

    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext, NoteListActivity.class);
        return intent;
    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("Exit Laboratory Notebook.")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //NoteListActivity.super.onBackPressed();
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }).create().show();
    }

}
