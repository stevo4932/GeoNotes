package us.ststephens.geonotes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import us.ststephens.geonotes.models.Note;

public class NoteDetailsFragment extends Fragment {
    public static final String KEY_NOTE = "key:note";
    private Note note;
    private TextView title;
    private TextView location;
    private TextView description;
    private TextView time;

    public static NoteDetailsFragment newInstance(Note note) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_NOTE, note);
        NoteDetailsFragment fragment = new NoteDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            note = savedInstanceState.getParcelable(KEY_NOTE);
        } else {
            note = getArguments().getParcelable(KEY_NOTE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.note_details_fragment, container, false);
        title = (TextView) view.findViewById(R.id.title);
        location = (TextView) view.findViewById(R.id.location);
        description = (TextView) view.findViewById(R.id.description);
        time = (TextView) view.findViewById(R.id.time_stamp);
        setNoteInfo(note);
        return view;
    }

    public void setNoteInfo(Note note) {
        title.setText(note.getTitle());
        description.setText(note.getDescription());
    }
}
