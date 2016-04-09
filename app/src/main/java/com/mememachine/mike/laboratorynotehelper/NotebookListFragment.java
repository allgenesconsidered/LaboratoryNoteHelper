package com.mememachine.mike.laboratorynotehelper;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.mememachine.mike.laboratorynotehelper.database.NoteBaseHelper;
import com.mememachine.mike.laboratorynotehelper.general.GenAppContext;
import com.mememachine.mike.laboratorynotehelper.imageRes.SimpleDividerItemDecoration;

import java.util.List;

public class NotebookListFragment extends Fragment{
    private RecyclerView mNotebookRecycleView;
    private NotebookAdapter mAdapter;
    private FloatingActionButton mFabAddNote;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //Set to TRUE to tell the activity it has a menu item.
        setHasOptionsMenu(true);
    }

    private class NotebookHolder extends RecyclerView.ViewHolder {

        private Notebook mNotebook;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private TextView mNoteCountTextView;

        public NotebookHolder(View itemView){
            //Populate the list item with the below views.
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_notebook_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_notebook_date);
            mNoteCountTextView = (TextView) itemView.findViewById(R.id.list_item_notebook_count);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    //Displays note info when clicked.
                    Intent intent = NoteListActivity.newIntent(getActivity(), mNotebook.getID());
                    startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    deleteAlertDialog(mNotebook);
                    return true;
                }
            });
        }

        public void bindNotebook(Notebook notebook){
            //Add a note to the list
            mNotebook = notebook;
            mTitleTextView.setText(mNotebook.getTitle());
            mDateTextView.setText(mNotebook.getStringDate());
            DatabaseFunctions noteList = DatabaseFunctions.get(getActivity());
            int noteCount;
            if( noteList.getNotes(mNotebook.getID()) == null){
                noteCount = 0;
            } else {
                noteCount = noteList.getNotes(mNotebook.getID()).size();
            }
            String count = getResources()
                    .getQuantityString(R.plurals.subtitle_plural,
                            noteCount, noteCount);
            mNoteCountTextView.setText(count);

        }
    }

    private class NotebookAdapter extends RecyclerView.Adapter<NotebookHolder>{
        private List<Notebook> mNotebookList;
        public NotebookAdapter(List<Notebook> notebooks) {mNotebookList = notebooks;}

        @Override
        public NotebookHolder onCreateViewHolder( ViewGroup parent, int viewType){
            //Inflate a single Holder (a single 'notebook' in the list).
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_notebooks, parent, false);
            return new NotebookHolder(view);
        }

        @Override
        public void onBindViewHolder(NotebookHolder holder, int position){
            Notebook notebook = mNotebookList.get(position);
            holder.bindNotebook(notebook);
        }

        @Override
        public int getItemCount(){ return mNotebookList.size();}

        public void setNotebookList(List<Notebook> notebooks) {mNotebookList = notebooks;}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
                             savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);
        mNotebookRecycleView = (RecyclerView) view.findViewById(R.id.note_recycler_view);
        mNotebookRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(savedInstanceState !=null){
            //TODO get Extras or something
            return null;
        }

        mFabAddNote = (FloatingActionButton) view.findViewById(R.id.fab_add_note);
        mFabAddNote.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                addNewNotebook();
            }
        });
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        DatabaseFunctions listNotebooks = DatabaseFunctions.get(getActivity());
        List<Notebook> notebooks = listNotebooks.getNotebooks();

        if(mAdapter == null) {
            mAdapter = new NotebookAdapter(notebooks);
            mNotebookRecycleView.addItemDecoration(new SimpleDividerItemDecoration(
                    GenAppContext.getAppContext()));
            mNotebookRecycleView.setAdapter(mAdapter);
        } else {
            mAdapter.setNotebookList(notebooks);
            mAdapter.notifyDataSetChanged();
        }
    }


    private void addNewNotebook(){
        final Notebook notebook = new Notebook();
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        final EditText edittext = new EditText(getContext());
        alert.setMessage("Create new Notebook");
        alert.setTitle("Enter the name of the new notebook");
        alert.setView(edittext);

        alert.setPositiveButton("Create Notebook", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String title = edittext.getText().toString();
                notebook.setTitle(title);
                DatabaseFunctions.get(getActivity()).addNotebook(notebook);
                updateUI();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }

    public void deleteAlertDialog(final Notebook nb){
        new android.app.AlertDialog.Builder(this.getContext())
                .setTitle("Deleting notebook...")
                .setMessage("Are you sure?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Switching between activities is a bit easier than switching from
                                //different Fragments.
                                DatabaseFunctions.get(getActivity()).deleteNotebook(nb);
                                updateUI();
                            }
                        }).create().show();
    }

}
