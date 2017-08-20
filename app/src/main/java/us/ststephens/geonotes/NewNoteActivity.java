package us.ststephens.geonotes;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import us.ststephens.geonotes.core.BaseActivity;
import us.ststephens.geonotes.models.Note;

public class NewNoteActivity extends BaseActivity implements NewNoteFragment.OnNoteSavedListener{
    private static final String TAG_FRAG = "tag:fragment";
    private static final String KEY_LOCATION = "key:location";
    public static final String KEY_CREATED_NOTE = "key:note";

    public static Intent newInstance(Context context, Location location) {
        Intent intent = new Intent(context, NewNoteActivity.class);
        intent.putExtra(KEY_LOCATION, location);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_activity);
        if (savedInstanceState == null) {
            Location location = getIntent().getParcelableExtra(KEY_LOCATION);
            Fragment fragment = NewNoteFragment.newInstance(location);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout, fragment, TAG_FRAG)
                    .commit();
        }

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.create_note);
    }

    @Override
    public void onNoteSaved(Note note) {
        Intent intent = new Intent();
        intent.putExtra(KEY_CREATED_NOTE, note);
        setResult(RESULT_OK, intent);
        finish();
    }
}
