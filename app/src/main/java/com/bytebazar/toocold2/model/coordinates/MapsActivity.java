package com.bytebazar.toocold2.model.coordinates;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.bytebazar.toocold2.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {


    public static final String EXTRA_LATITUDE = "com.bytebazar.toocold2.latitude";
    public static final String EXTRA_LONGITUDE = "com.bytebazar.toocold2.longitude";
    private GoogleMap mMap;
    private LatLng mLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(latLng -> {
            mLatLng = latLng;
            mMap.clear();
            Toast.makeText(MapsActivity.this, latLng.latitude + " " + latLng.longitude, Toast.LENGTH_SHORT).show();
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            mMap.addMarker(markerOptions);
        });

    }

    @Override
    public void finish() {
        if (mLatLng != null) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_LATITUDE, mLatLng.latitude);
            intent.putExtra(EXTRA_LONGITUDE, mLatLng.longitude);
            setResult(RESULT_OK, intent);
        }
        super.finish();
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, com.bytebazar.toocold2.model.coordinates.MapsActivity.class);
    }
}
