package us.ststephens.geonotes;

import android.Manifest;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import us.ststephens.geonotes.core.BaseActivity;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback, OnSuccessListener<Location>, OnFailureListener{
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap mMap;
    private Location lastKnownLocation;
    private static final int REQ_LOCATION = 0x1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getUserLocation();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        Toast.makeText(MapsActivity.this, "Unable to retrieve location", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(Location location) {
        lastKnownLocation = location;
        updateMapLocation();
    }

    private void updateMapLocation() {
        if (mMap != null && lastKnownLocation != null) {
            LatLng myLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(myLocation).title("Your location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        updateMapLocation();
    }
}
