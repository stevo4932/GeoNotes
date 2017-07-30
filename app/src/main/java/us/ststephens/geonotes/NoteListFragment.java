package us.ststephens.geonotes;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NoteListFragment extends Fragment implements View.OnClickListener, NotesListAdapter.OnNoteSelectedListener{
    private static final String KEY_NOTES = "key:notes";
    private Note[] notes;
    private NotesListAdapter adapter;

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
            notes = (Note[]) savedInstanceState.getParcelableArray(KEY_NOTES);
        } else {
            notes = (Note[]) getArguments().getParcelableArray(KEY_NOTES);
        }
        adapter = new NotesListAdapter(createNotes(10));
        adapter.setListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.note_list_fragment, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.note_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            startActivity(NewNoteActivity.newInstance(getContext()));
        }
    }

    @Override
    public void onNoteSelected(Note note) {
        startActivity(NoteDetailsActivity.newIntent(getContext(), note));
    }

    private static Note[] createNotes(int num) {
        Note[] notes = new Note[num];
        for (int i = 0; i < num; i++) {
            Note note = new Note();
            note.setTitle("Test " + i);
            note.setDescription("Adipiscing blandit venenatis feugiat dignissim viverra nisl habitasse vestibulum a dis leo nec nec fringilla a suspendisse ullamcorper.");
            notes[i] = note;
        }
        return notes;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArray(KEY_NOTES, notes);
    }

    public void setNotes(Note[] notes) {
        this.notes = notes;
        adapter.setNotes(notes);
    }
}
