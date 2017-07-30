package us.ststephens.geonotes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import us.ststephens.geonotes.core.BaseActivity;

public class NoteDetailsActivity extends BaseActivity {
    private static final String KEY_NOTE = "key:note";
    private static final String TAG_FRAG = "tag:frag";


    public static Intent newIntent(Context context, Note note) {
        Intent intent = new Intent(context, NoteDetailsActivity.class);
        intent.putExtra(KEY_NOTE, note);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_activity);
        if (savedInstanceState == null) {
            Note note = getIntent().getParcelableExtra(KEY_NOTE);
            Fragment fragment = NoteDetailsFragment.newInstance(note);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout, fragment, TAG_FRAG)
                    .commit();
        }

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.note_details);
    }
}
