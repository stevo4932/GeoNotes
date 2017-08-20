package us.ststephens.geonotes;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import us.ststephens.geonotes.models.Note;


public class NewNoteFragment extends Fragment {
    private static final String KEY_NOTE = "key:note";
    private static final String KEY_LOCATION = "key:location";
    private OnNoteSavedListener listener;
    private Note note;
    private Location currentLocation;

    public static NewNoteFragment newInstance(Location location) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_LOCATION, location);
        NewNoteFragment fragment = new NewNoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnNoteSavedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Must implement OnNoteSavedListener in NewNoteFragment");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            note = savedInstanceState.getParcelable(KEY_NOTE);
            currentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
        } else {
            note = new Note();
            currentLocation = getArguments().getParcelable(KEY_LOCATION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_note_fragment, container, false);
        EditText title = (EditText) view.findViewById(R.id.title);
        EditText description = (EditText) view.findViewById(R.id.description);

        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                note.setDescription(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                note.setTitle(s.toString());
                if (shouldUpdateSave(before, count)) {
                    getActivity().invalidateOptionsMenu();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    public boolean shouldUpdateSave(int before, int after) {
        boolean beforeEmpty = before == 0;
        boolean afterEmpty = after == 0;
        return (afterEmpty && !beforeEmpty) || (!afterEmpty && beforeEmpty);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.new_note_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.save);
        if (menuItem == null) return;
        boolean isEnabled = !TextUtils.isEmpty(note.getTitle());
        menuItem.setEnabled(isEnabled);
        menuItem.getIcon().setAlpha(isEnabled ? 255 : 64);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            // TODO: 7/29/17 save the item.
            listener.onNoteSaved(note);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_NOTE, note);
        outState.putParcelable(KEY_LOCATION, currentLocation);
    }

    public interface OnNoteSavedListener{
        void onNoteSaved(Note note);
    }
}
