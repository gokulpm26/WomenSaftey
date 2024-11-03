package android.cybersiblings.safegaurd;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class LocationHelperClass {
    private final Context context;
    private LocationManager locationManager;

    public LocationHelperClass(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void getLocation(LocationResult locationResult) {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        locationResult.onLocationObtained(location.getLatitude(), location.getLongitude());
                        locationManager.removeUpdates(this);
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {}

                @Override
                public void onProviderEnabled(String provider) {}

                @Override
                public void onProviderDisabled(String provider) {}
            });
        } catch (SecurityException e) {
            Log.e("LocationHelperClass", "Permission denied for location access.", e);
        }
    }
}
