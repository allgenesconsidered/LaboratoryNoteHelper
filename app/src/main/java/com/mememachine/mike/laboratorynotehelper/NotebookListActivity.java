package com.mememachine.mike.laboratorynotehelper;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.mememachine.mike.laboratorynotehelper.general.SingleFragmentActivity;


public class NotebookListActivity extends SingleFragmentActivity{

    @Override
    protected Fragment createFragment() {
        return new NotebookListFragment();
    }

    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext, NotebookListActivity.class);
        return intent;
    }

    @Override
    public void onBackPressed() {
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
