package com.bytebazar.toocold2.model.coordinates;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;

import com.bytebazar.toocold2.App;
import com.bytebazar.toocold2.model.coordinates.Coordinates;


public class LocationService extends Service {
    private LocationManager mLocationManager;
    private LocationListener mListener;


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        initializeLocationManager();
        mListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                App.getSubject().onNext(new Coordinates(location.getLatitude(), location.getLongitude()));
                mLocationManager.removeUpdates(mListener);
                stopSelf();
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
        };
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!checkPermission()) {
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 5, mListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseResources();
    }

    private boolean checkPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }


    private void releaseResources() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(mListener);
        }
    }
}