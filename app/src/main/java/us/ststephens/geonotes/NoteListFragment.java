package us.ststephens.geonotes;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import us.ststephens.geonotes.core.LocationUpdate;
import us.ststephens.geonotes.models.Note;

public class NoteListFragment extends Fragment implements View.OnClickListener, LocationUpdate{
    private static final String KEY_NOTES = "key:notes";
    private static final String KEY_LOCATION = "key:location";
    private static final int REQ_CREATE_NOTE = 55;
    private Note[] notes;
    private NotesListAdapter adapter;
    private Location location;

    public static NoteListFragment newInstance(Note[] notes) {
        Bundle args = new Bundle();
        args.putParcelableArray(KEY_NOTES, notes);
        NoteListFragment fragment = new NoteListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            notes = (Note[]) savedInstanceState.getParcelableArray(KEY_NOTES); // TODO: 8/20/17 don't save the list of notes in saved state bundle
            location = savedInstanceState.getParcelable(KEY_LOCATION);
        } else {
            Bundle arguments = getArguments();
            notes = (Note[]) arguments.getParcelableArray(KEY_NOTES);
            location = arguments.getParcelable(KEY_LOCATION);
        }
        adapter = new NotesListAdapter();
        adapter.setNotes(createNotes(10));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.note_list_fragment, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.note_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new MyItemAnimator());
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            startActivityForResult(NewNoteActivity.newInstance(getContext(), location), REQ_CREATE_NOTE);
        }
    }

    private static Note[] createNotes(int num) {
        Note[] notes = new Note[num];
        for (int i = 0; i < num; i++) {
            Note note = new Note();
            note.setTitle("Test " + i);
            note.setDescription("Adipiscing blandit venenatis feugiat dignissim viverra nisl habitasse vestibulum a dis leo nec nec fringilla a suspendisse ullamcorper." +
                    "Adipiscing blandit venenatis feugiat dignissim viverra nisl habitasse vestibulum a dis leo nec nec fringilla a suspendisse ullamcorper." +
                    "Adipiscing blandit venenatis feugiat dignissim viverra nisl habitasse vestibulum a dis leo nec nec fringilla a suspendisse ullamcorper.");
            notes[i] = note;
        }
        return notes;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArray(KEY_NOTES, notes);
        outState.putParcelable(KEY_LOCATION, location);
    }

    @Override
    public void onLocationUpdated(Location location) {
        // TODO: 8/19/17 update user's list
        Log.d("Notes", "Location Updated");
        this.location = location;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CREATE_NOTE) {
            if (resultCode == Activity.RESULT_OK && adapter != null) {
                Note createdNote = data.getParcelableExtra(NewNoteActivity.KEY_CREATED_NOTE);
                if (createdNote != null) {
                    adapter.addNote(createdNote);
                }
            } else {
                //you might want to reload the list.
            }
        }
    }
}
