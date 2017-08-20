package us.ststephens.geonotes.core;

import android.location.Location;

public interface LocationUpdate {
    void onLocationUpdated(Location location);
}
