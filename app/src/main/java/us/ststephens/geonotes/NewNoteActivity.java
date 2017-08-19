package us.ststephens.geonotes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import us.ststephens.geonotes.core.BaseActivity;
import us.ststephens.geonotes.models.Note;

public class NewNoteActivity extends BaseActivity implements NewNoteFragment.OnNoteSavedListener{
    private static final String TAG_FRAG = "tag:fragment";
    public static final String KEY_NOTE = "key:note";

    public static Intent newInstance(Context context) {
        return new Intent(context, NewNoteActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_activity);
        if (savedInstanceState == null) {
            Fragment fragment = NewNoteFragment.newInstance();
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
        intent.putExtra(KEY_NOTE, note);
        setResult(RESULT_OK, intent);
        finish();
    }
}
