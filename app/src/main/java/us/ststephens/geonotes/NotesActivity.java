package us.ststephens.geonotes;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import us.ststephens.geonotes.core.BaseActivity;

public class NotesActivity extends BaseActivity implements OnSuccessListener<LocationSettingsResponse>, OnFailureListener{
    private static final String TAG_LIST = "tag:list";
    private static final String KEY_LOCATION = "key:location";
    private static final String KEY_LOCATION_REQUEST = "key:location_request";
    private static final int REQUEST_CHECK_SETTINGS = 432;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private Location latestLocation;
    private boolean isRequestingLocation;
    private static final int REQ_LOCATION = 0x1;

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
           onNewLocationReceived(locationResult);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_activity);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (savedInstanceState == null) {
            NoteListFragment fragment = NoteListFragment.newInstance(null);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout, fragment, TAG_LIST)
                    .commit();
        } else {
            latestLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            isRequestingLocation = savedInstanceState.getBoolean(KEY_LOCATION_REQUEST);
        }

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("Notes Nearby");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isRequestingLocation) {
            getUserLocation();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_LOCATION, latestLocation);
        outState.putBoolean(KEY_LOCATION_REQUEST, isRequestingLocation);
    }

    private void getUserLocation() {
        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, REQ_LOCATION);
    }

    @Override
    @SuppressWarnings({"ResourceType"})
    public void onPermissionGranted(int requestCode) {
        if (requestCode == REQ_LOCATION) {
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(getLocationRequest());

            //Check if the user has the proper locations settings enabled.
            SettingsClient client = LocationServices.getSettingsClient(this);
            Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
            task.addOnSuccessListener(this, this);
            task.addOnFailureListener(this, this);
        }
    }

    @Override
    public void onSuccess(LocationSettingsResponse location) {
        startLocationUpdates();
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        int statusCode = ((ApiException) e).getStatusCode();
        switch (statusCode) {
            case CommonStatusCodes.RESOLUTION_REQUIRED:
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(NotesActivity.this,
                            REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                noLocationAvailable();
                break;
        }
    }

    private void noLocationAvailable() {
        Toast.makeText(NotesActivity.this, "Unable to retrieve location", Toast.LENGTH_SHORT).show();
    }

    @SuppressWarnings({"ResourceType"})
    private void startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(getLocationRequest(),
                locationCallback,
                null);
        isRequestingLocation = true;
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
        isRequestingLocation = false;
    }

    private LocationRequest getLocationRequest() {
        if (locationRequest == null) {
            locationRequest = new LocationRequest();
            locationRequest.setInterval(15000);
            locationRequest.setFastestInterval(5000);
            locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        }
        return locationRequest;
    }

    private void onNewLocationReceived(LocationResult locationResult) {
        for (Location location : locationResult.getLocations()) {
            if (latestLocation == null || latestLocation.getTime() < location.getTime()) {
                latestLocation = location;
            }
        }
        Log.d("Note", "Location Updated: " + latestLocation.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS){
            if (resultCode == RESULT_OK) {
                startLocationUpdates();
            } else {
                noLocationAvailable();
            }
        }
    }
}
