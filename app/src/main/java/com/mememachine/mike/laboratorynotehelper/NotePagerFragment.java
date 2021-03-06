package com.mememachine.mike.laboratorynotehelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mememachine.mike.laboratorynotehelper.imageRes.PictureUtils;

import java.io.File;
import java.util.UUID;

import static android.support.v7.app.AlertDialog.*;

/**
 * Instantiates a fragment instance of a selected note, and inflates the layout.
 */
public class NotePagerFragment extends Fragment{
    private static final String ARG_NOTE_ID = "note_id";

    public File mNotePhoto;
    private Note mNote;
    private TextView mTitleField;
    private TextView mBodyField;
    private TextView mDateField;
    private ImageView mNotePhotoView;

    public static NotePagerFragment newInstance(UUID noteID){
        //Returns an instance of class corresponding
        //to the given UUID.
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTE_ID, noteID);

        NotePagerFragment fragment = new NotePagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        UUID noteID = (UUID) getArguments().getSerializable(ARG_NOTE_ID);
        mNote = DatabaseFunctions.get(getActivity()).getNote(noteID);
        mNotePhoto = DatabaseFunctions.get(getActivity()).getPhotoFile(mNote);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume(){
        super.onResume();
        DatabaseFunctions.get(getActivity())
                .updateNote(mNote);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        //inflater.inflate(int resource, Viewgroup root, boolean attached to root?)
        //We are explicitly inflating the fragment_crime.xml part of the resource
        View v = inflater.inflate(R.layout.fragment_note_static_imageless, container, false);

        if (mNotePhoto != null && mNotePhoto.exists()) {
            v = inflater.inflate(R.layout.fragment_note_static, container, false);
            mNotePhotoView = (ImageView) v.findViewById(R.id.note_photo);
            updatePhotoView(mNotePhotoView, mNotePhoto);
            mNotePhotoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showZoomed();
                }
            });
        }
        mTitleField = (TextView) v.findViewById(R.id.note_title);
        mTitleField.setText(mNote.getTitle());

        mDateField = (TextView) v.findViewById(R.id.note_date);
        mDateField.setText(mNote.getStringDate());

        mBodyField = (TextView) v.findViewById(R.id.note_body);
        mBodyField.setText(mNote.getBody());

        //Explicitly return the view that was inflated.
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_note_static, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            //pressed 'Delete' button.
            case R.id.menu_item_edit_note:
                //“Once you have handled the MenuItem, you should return true
                // to indicate that no further processing is necessary.”
                Intent intent = NoteEditorActivity.newIntent(getActivity(), mNote.getID());
                startActivity(intent);
                return true;
            case R.id.menu_item_delete_note_static:
                deleteAlertDialog();
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
                                DatabaseFunctions.get(getActivity()).deleteNote(mNote);
                                Intent intent = NoteListActivity.newIntent(getActivity());
                                startActivity(intent);
                            }
                        }).create().show();
    }

    public void updatePhotoView(ImageView imageView, File file) {
        if (file == null || !file.exists()) {
            imageView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    file.getPath(), getActivity());
            imageView.setImageBitmap(bitmap);
        }
    }

    private void showZoomed(){
        Builder alert = new Builder(getContext());
        final ImageView imageView = new ImageView(getContext());
        imageView.setRotation((float) 90);
        updatePhotoView(imageView, mNotePhoto);
        alert.setView(imageView);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                return;
            }
        });
        alert.show();
    }

}