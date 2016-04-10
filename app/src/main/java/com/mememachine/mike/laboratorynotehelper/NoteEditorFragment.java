package com.mememachine.mike.laboratorynotehelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mememachine.mike.laboratorynotehelper.imageRes.PictureUtils;

import java.io.File;
import java.util.UUID;

/**
 * Instantiates a fragment instance of a selected note, and inflates the layout.
 */
public class NoteEditorFragment extends Fragment{
    private static final String ARG_NOTE_ID = "note_id";
    private static final int REQUEST_PHOTO = 2;

    private Note mNote;
    private File mNotePhoto;
    private EditText mTitleField;
    private EditText mBodyField;
    private TextView mDateView;
    private FloatingActionButton mFabTakePicture;
    private ImageView mNotePhotoView;
    private ImageButton mImageButton;

    public static NoteEditorFragment newInstance(UUID noteID){
        //Returns an instance of NoteEditorFragment.class corresponding
        //to the given UUID.
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTE_ID, noteID);

        NoteEditorFragment fragment = new NoteEditorFragment();
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
    public void onPause(){
        super.onPause();
        DatabaseFunctions.get(getActivity()).updateNote(mNote);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        //inflater.inflate(int resource, Viewgroup root, boolean attached to root?)
        //We are explicitly inflating the fragment_crime.xml part of the resource
        View v = inflater.inflate(R.layout.fragment_note, container, false);
        PackageManager packageManager = getActivity().getPackageManager();


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

        mDateView = (TextView) v.findViewById(R.id.note_date);
        mDateView.setText(mNote.getStringDate());
        mDateView.setEnabled(false);

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

        mFabTakePicture = (FloatingActionButton) v.findViewById(R.id.fab_take_picture);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mNotePhoto != null && captureImage
                .resolveActivity(packageManager) != null;
        mFabTakePicture.setEnabled(canTakePhoto);
        if (canTakePhoto) {
            Uri uri = Uri.fromFile(mNotePhoto);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        mFabTakePicture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mNotePhotoView = (ImageView) v.findViewById(R.id.note_photo);

        mImageButton = (ImageButton) v.findViewById(R.id.image_button_finished_edit);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseFunctions.get(getActivity()).updateNote(mNote);
                Toast.makeText(getContext(),
                        "Note Updated!", Toast.LENGTH_SHORT).show();
                Intent intent = NotePagerActivity.newIntent(getActivity(), mNote.getID());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
            }
        });

        //Explicitly return the view that was inflated.
        updatePhotoView(mNotePhotoView, mNotePhoto);
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
                                DatabaseFunctions.get(getActivity()).deleteNote(mNote);
                                Intent intent = NoteListActivity.newIntent(getActivity());
                                startActivity(intent);

                            }
                        }).create().show();
    }

    @Override
    public void onActivityResult( int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_PHOTO) {
            updatePhotoView(mNotePhotoView, mNotePhoto);
        }
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

}
