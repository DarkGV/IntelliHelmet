package pt.ubi.di.pmd.intellihelmet20;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MyLocationListener implements LocationListener {

    private static String stringLongLat = null;

    @Override
    public void onLocationChanged(Location loc) {

        String longitude = "Longitude: " + loc.getLongitude();
        String latitude = "Latitude: " + loc.getLatitude();

        String s = "\n" + longitude + "\n" + latitude + "\n" ;
        stringLongLat = s;

        Log.d("LOCATIONHELMET", s);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public static String getLocation(){
        return stringLongLat;
    }


}