package us.ststephens.geonotes;

import android.Manifest;
import android.location.Location;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import us.ststephens.geonotes.core.BaseActivity;

public class NotesActivity extends BaseActivity implements OnSuccessListener<Location>, OnFailureListener{
    private static final String TAG_LIST = "tag:list";
    private static final String KEY_LOCATION = "key:location";
    private FusedLocationProviderClient fusedLocationClient;
    private Location lastKnownLocation;
    private static final int REQ_LOCATION = 0x1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_activity);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getUserLocation();

        if (savedInstanceState == null) {
            NoteListFragment fragment = NoteListFragment.newInstance(null);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout, fragment, TAG_LIST)
                    .commit();
        } else {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
        }

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("Notes Nearby");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_LOCATION, lastKnownLocation);
    }

    private void getUserLocation() {
        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, REQ_LOCATION);
    }

    @Override
    @SuppressWarnings({"ResourceType"})
    public void onPermissionGranted(int requestCode) {
        if (requestCode == REQ_LOCATION) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, this)
                    .addOnFailureListener(this, this);
        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        Toast.makeText(NotesActivity.this, "Unable to retrieve location", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(Location location) {
        lastKnownLocation = location;
        Log.d("Notes", "Location: " + location.toString());
        //load some notes using this location.
    }
}
