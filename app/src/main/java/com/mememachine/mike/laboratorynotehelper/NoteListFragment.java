package com.mememachine.mike.laboratorynotehelper;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mememachine.mike.laboratorynotehelper.database.NoteBaseHelper;
import com.mememachine.mike.laboratorynotehelper.general.GenAppContext;
import com.mememachine.mike.laboratorynotehelper.imageRes.PictureUtils;
import com.mememachine.mike.laboratorynotehelper.imageRes.SimpleDividerItemDecoration;

import java.io.File;
import java.util.List;
import java.util.UUID;


public class NoteListFragment extends Fragment{

    private static final String SUBTITLE_VISIBLE_BOOL = "isSubtitleVisible";
    private static final String ARG_NOTEBOOK_ID =
            "com.mememachine.mike.laboratorynotehelper.notebook_id";

    private UUID mNotebookID;
    private RecyclerView mNoteRecycleView;
    private NoteAdapter mAdapter;
    private FloatingActionButton mFloatingActionButton;
    private boolean mSubtitleVisible;

    public static NoteListFragment newInstance(UUID noteID){
        //Returns an instance of class corresponding
        //to the given UUID.
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTEBOOK_ID, noteID);
        NoteListFragment fragment = new NoteListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mNotebookID = (UUID) getArguments().getSerializable(ARG_NOTEBOOK_ID);
        //Set to TRUE to tell the activity it has a menu item.
        setHasOptionsMenu(true);
    }

    private class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //'implements' keyword refers to implementing an interface, in this case a listener
        //to see what note is pushed.
        //NoteHolder Holds the instances of the different recyclerview objects.
        private Note mNote;
        public File mNotePhoto;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mNoteImage;
        private TextView mBodyTextView;

        public NoteHolder(View itemView){
            //Set method variables
            super(itemView);
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_notes_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_notes_date_text_view);
            mNoteImage = (ImageView) itemView.findViewById(R.id.list_item_notes_image);
            mBodyTextView = (TextView) itemView.findViewById(R.id.list_item_notes_body);

        }
        public void bindNote(Note note){
            //Add a note to the list
            mNote = note;
            mTitleTextView.setText(mNote.getTitle());
            mDateTextView.setText(mNote.getStringDate());
            mNotePhoto = DatabaseFunctions.get(getActivity()).getPhotoFile(mNote);
            updatePhotoView(mNoteImage, mNotePhoto);
            if (mNote.getBody() != null) {
                String body = mNote.getBody();
                mBodyTextView.setText(body.substring(0, Math.min(body.length(), 200)));
            }
        }
        @Override
        public void onClick(View v){
            //Displays note info when clicked.
            Intent intent = NotePagerActivity.newIntent(getActivity(), mNote.getID());
            startActivity(intent);
        }
    }

    private class NoteAdapter extends RecyclerView.Adapter<NoteHolder>{
        private List<Note> mNotes;

        public NoteAdapter(List<Note> notes) {
            mNotes = notes;
        }

        @Override
        public NoteHolder onCreateViewHolder( ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_notes, parent, false);
            return new NoteHolder(view);
        }

        @Override
        public void onBindViewHolder(NoteHolder holder, int position){
            Note note = mNotes.get(position);
            holder.bindNote(note);
        }

        @Override
        public int getItemCount(){
            return mNotes.size();
        }

        public void setNotes(List<Note> notes) {
            mNotes = notes;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);
        mNoteRecycleView = (RecyclerView) view.findViewById(R.id.note_recycler_view);
        mNoteRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(savedInstanceState !=null){
            mSubtitleVisible = savedInstanceState.getBoolean(SUBTITLE_VISIBLE_BOOL);
        }
        updateUI();

        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_add_note);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewNote();
            }
        });
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        //Save subtitle display across instance states.
        outState.putBoolean(SUBTITLE_VISIBLE_BOOL, mSubtitleVisible);
    }

    //Options menu actions and inflation.
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_note_list, menu);
        //Sets the text of 'Subtitle' option on creation.
        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            //pressed 'New Crime' button.
            case R.id.menu_item_new_note:
                addNewNote();
                //“Once you have handled the MenuItem, you should return true
                // to indicate that no further processing is necessary.”
                return true;
            case R.id.menu_item_show_subtitle:
                //Switch the value of mSubtitleVisible
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu(); //onCreateOptionsMenu can be called again.
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addNewNote(){
        Note note = new Note();
        DatabaseFunctions.get(getActivity()).addNote(note, mNotebookID);
        Intent intent = NoteEditorActivity
                .newIntent(getActivity(), note.getID());
        startActivity(intent);
    }

    private void updateSubtitle(){
        //Produces subtitles for the RecycleViews, based on the size of the
        //total number of crimes.
        DatabaseFunctions noteList = DatabaseFunctions.get(getActivity());
        int noteCount = noteList.getNotes().size();
        String subtitle = getResources()
                .getQuantityString(R.plurals.subtitle_plural,
                        noteCount, noteCount);
        if(!mSubtitleVisible){
            subtitle = null;
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI(){
        NoteBaseHelper listNotes = NoteBaseHelper.get(getActivity());
        List<Note> notes = listNotes.getNotesByNotebookID(mNotebookID.toString());

        if(mAdapter == null) {
            mAdapter = new NoteAdapter(notes);
            mNoteRecycleView.addItemDecoration(new SimpleDividerItemDecoration(
                    GenAppContext.getAppContext()));
            mNoteRecycleView.setAdapter(mAdapter);
        } else {
            mAdapter.setNotes(notes);
            mAdapter.notifyDataSetChanged();
        }
        updateSubtitle();
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
