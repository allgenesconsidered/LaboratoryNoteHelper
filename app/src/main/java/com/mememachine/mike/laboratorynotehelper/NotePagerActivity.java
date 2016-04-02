package com.mememachine.mike.laboratorynotehelper;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

public class NotePagerActivity extends AppCompatActivity {

    private static final String EXTRA_NOTE_ID_NUM =
            "com.mememachine.mike.laboratorynotehelper.note_id";

    private ViewPager mViewPager;
    private List<Note> mNotes;


    public static Intent newIntent(Context packageContext, UUID noteID){
        /*
        Use this intent method when transitioning between fragments. That way the pager
        knows what note to display.
        Input: Context of the Activity you're transitioning from, UUID of the note to display.
        Output: Intent to switch to the NotePagerActivity.
        */
        Intent intent = new Intent(packageContext, NotePagerActivity.class);
        intent.putExtra(EXTRA_NOTE_ID_NUM, noteID);
        return intent;
    }

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_note_pager);

        UUID noteID = (UUID) getIntent().getSerializableExtra(EXTRA_NOTE_ID_NUM);
        mViewPager = (ViewPager) findViewById(R.id.activity_note_pager_view);
        mNotes = ListOfNotes.get(this).getNotes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Note note = mNotes.get(position);
                return NoteFragmentStatic.newInstance(note.getID());
            }

            @Override
            public int getCount() {
                return mNotes.size();
            }
        });

        for (int i=0; i < mNotes.size(); i++) {
            if (mNotes.get(i).getID().equals(noteID)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}

