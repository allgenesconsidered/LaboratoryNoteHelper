package com.mememachine.mike.laboratorynotehelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.UUID;

/**
 * Instantiates a fragment instance of a selected note, and inflates the layout.
 */
public class NoteFragment extends Fragment{
    private static final String ARG_NOTE_ID = "note id";

    private boolean MARKED_FOR_DELETE = false;

    private Note mNote;
    private EditText mTitleField;
    private EditText mCellTypeField;
    private EditText mBodyField;
    private Button mDateButton;

    public static NoteFragment newInstance(UUID noteID){
        //Returns an instance of NoteFragment.class corresponding
        //to the given UUID.
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTE_ID, noteID);

        NoteFragment fragment = new NoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        UUID noteID = (UUID) getArguments().getSerializable(ARG_NOTE_ID);
        mNote = ListOfNotes.get(getActivity()).getNote(noteID);

        setHasOptionsMenu(true);
    }

    @Override
    public void onPause(){
        super.onPause();
        ListOfNotes.get(getActivity())
                .updateNote(mNote);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        //inflater.inflate(int resource, Viewgroup root, boolean attached to root?)
        //We are explicitly inflating the fragment_crime.xml part of the resource
        View v = inflater.inflate(R.layout.fragment_note, container, false);

        mTitleField = (EditText) v.findViewById(R.id.note_title);
        mTitleField.setText(mNote.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Set the typed message to the title of the Note() instance.
                mNote.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //BLANK, add more later to save titles and stuff. Major TODO.
            }
        });

        mDateButton = (Button) v.findViewById(R.id.note_date);
        mDateButton.setText(mNote.getStringDate());
        mDateButton.setEnabled(false);

        mCellTypeField = (EditText) v.findViewById(R.id.note_celltype);
        mCellTypeField.setText(mNote.getCellType());
        mCellTypeField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //None
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNote.setCellType(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //None
            }
        });

        mBodyField = (EditText) v.findViewById(R.id.note_body);
        mBodyField.setText(mNote.getBody());
        mBodyField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //None
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNote.setBody(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //None
            }
        });

        //Explicitly return the view that was inflated.
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_note, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            //pressed 'Delete' button.
            case R.id.menu_item_delete_note:
                deleteAlertDialog();
                //“Once you have handled the MenuItem, you should return true
                // to indicate that no further processing is necessary.”
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void deleteAlertDialog(){
        new AlertDialog.Builder(this.getContext())
                .setTitle("Deleting note...")
                .setMessage("Are you sure?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO delete Note from database
                                //Switching between activities is a bit easier than switching from
                                //different Fragments.
                                ListOfNotes.get(getActivity()).deleteNote(mNote);
                                Intent intent = NoteListActivity.newIntent(getActivity());
                                startActivity(intent);

                            }
                        }).create().show();
    }

}
