package us.ststephens.geonotes.Utils;

import android.location.Location;
import android.util.Log;

public class LocationUtils {
    private static final int TIME_DELTA = 1000 * 60;
    private static final int CITY_BLOCK = 200; //meters
    private static final int ACCURACY_THRESHOLD = 200;

    //May want to add something about if it has changed location by a certain delta.
    public static boolean isBetterLocation(Location newLocation, Location currentLocation) {
        if (currentLocation == null) {
            return true;
        }

        boolean hasUserMovedFar = compareMovement(newLocation, currentLocation);
        if (!hasUserMovedFar) {
            return false;
        }

        int ageDifference = compareAge(newLocation, currentLocation);
        if (ageDifference < 0) {
            return false;
        }

        int accuracyDifference = compareAccuracyLevel(newLocation, currentLocation);
        return accuracyDifference > 0;
    }

    // -1 if significantly older, 1 if significantly newer, 0 otherwise.
    public static int compareAge(Location newLocation, Location currentLocation) {
        long timeDelta = newLocation.getTime() - currentLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TIME_DELTA;
        boolean isSignificantlyOlder = timeDelta < -TIME_DELTA;

        if (isSignificantlyNewer) {
            return 1;
        } else if (isSignificantlyOlder) {
            return -1;
        } else {
            return 0;
        }
    }

    //returns -1 if significantly less accurate, 1 if significantly more accurate, 0 otherwise.
    public static int compareAccuracyLevel(Location newLocation, Location currentLocation) {
        int accuracyDelta = (int) (newLocation.getAccuracy() - currentLocation.getAccuracy());
        boolean isSignificantlyLessAccurate = accuracyDelta > ACCURACY_THRESHOLD;
        boolean isSignificantlyMoreAccurate = accuracyDelta < ACCURACY_THRESHOLD;
        if (isSignificantlyLessAccurate) {
            return -1;
        } else if (isSignificantlyMoreAccurate) {
            return  1;
        } else {
            return 0;
        }
    }

    //true if large movement, false otherwise.
    public static boolean compareMovement(Location newLocation, Location currentLocation) {
        float distance = newLocation.distanceTo(currentLocation);
        return distance > CITY_BLOCK;
    }
}
