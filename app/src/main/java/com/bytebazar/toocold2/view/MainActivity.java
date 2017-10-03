package com.bytebazar.toocold2.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.bytebazar.toocold2.Presenter;
import com.bytebazar.toocold2.R;
import com.bytebazar.toocold2.model.ModelImpl;
import com.bytebazar.toocold2.model.coordinates.MapsActivity;
import com.bytebazar.toocold2.model.realm.WeatherRealm;
import com.bytebazar.toocold2.view.adapter.DailyAdapter;

public class MainActivity extends AppCompatActivity implements ViewWeather, View.OnClickListener {

    private static final String TAG = "MainActivity";
    private final static int REQUEST_LOCATION_PERMISSION = 0;
    private static final int GPS_SETTINGS_REQUEST_CODE = 1;
    private static final int GOOGLE_MAP_REQUEST_CODE = 2;

    private View bottomSheetIndicator;
    private ProgressBar progressBar;

    private DailyAdapter weatherAdapter;
    private RecyclerView recyclerView;

    private BottomSheetBehavior bottomSheetBehavior;
    private Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        createPresenter();
        getWeatherData();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                    , WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    private void getWeatherData() {
        presenter.getWeatherData();
    }

    private void createPresenter() {
        ModelImpl model = new ModelImpl(this);
        presenter = new Presenter(this, model);
    }

    private void initView() {
        recyclerView = findViewById(R.id.dailyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        bottomSheetIndicator = findViewById(R.id.indicator);

        final FrameLayout llBottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    animateExpandedBottomSheet(bottomSheetIndicator);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    animateCollapsedBottomSheet(bottomSheetIndicator);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        View map = findViewById(R.id.map);
        View gps = findViewById(R.id.gps);

        map.setOnClickListener(this);
        gps.setOnClickListener(this);

        progressBar = findViewById(R.id.progress_bar);
    }

    private void animateCollapsedBottomSheet(View v) {
        ViewCompat.animate(v).rotationX(0).setDuration(1000).start();
    }

    private void animateExpandedBottomSheet(View v) {
        ViewCompat.animate(v).rotationX(180f).setDuration(1000).start();
    }

    @Override
    public void showWeather(WeatherRealm weatherRealm) {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        if (weatherAdapter == null) {
            weatherAdapter = new DailyAdapter(weatherRealm);
            recyclerView.setAdapter(weatherAdapter);
        } else {
            weatherAdapter.setData(weatherRealm);
            weatherAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void showLoading() {
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCoordinateChooser() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void showInternetError() {
        showSnackBar();
    }

    private void showSnackBar() {
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        Snackbar snackbar = Snackbar.make(recyclerView, "no internet", Snackbar.LENGTH_LONG);
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                bottomSheetBehavior.setHideable(false);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        snackbar.show();
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.map:
                startActivityForResult(MapsActivity.createIntent(this), GOOGLE_MAP_REQUEST_CODE);
                break;
            case R.id.gps:
                checkPermissions();
                break;
        }
    }

    private void checkPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_DENIED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

                    new AlertDialog.Builder(this)
                            .setTitle(R.string.title_location_permission)
                            .setMessage(R.string.text_location_permission)
                            .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> requestPermissions())
                            .create()
                            .show();
                } else {
                    requestPermissions();
                }
                return;
            }
        }
        handleGpsStatus();
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_LOCATION_PERMISSION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_MAP_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                presenter.getWeatherData(
                        data.getDoubleExtra(MapsActivity.EXTRA_LATITUDE, 0),
                        data.getDoubleExtra(MapsActivity.EXTRA_LONGITUDE, 0));
            }
        } else if (requestCode == GPS_SETTINGS_REQUEST_CODE) {
            if (isGpsEnabled()) {
                presenter.getCoordinates();
            }
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if ( grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    handleGpsStatus();
                }
            }
        }
    }

    private void handleGpsStatus() {
        if (isGpsEnabled()) {
            presenter.getCoordinates();
        } else {
            showGpsSettingsDialog();
        }
    }

    private boolean isGpsEnabled() {
        return ((LocationManager) getSystemService(Context.LOCATION_SERVICE))
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void showGpsSettingsDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.gps_not_available_title)
                .setMessage(R.string.enable_gps_message)
                .setNegativeButton(getString(android.R.string.cancel), null)
                .setPositiveButton(getString(R.string.open_gps_settings), (dialog, which) -> openGpsSettings())
                .create();
        alertDialog.show();
    }

    private void openGpsSettings() {
        final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, GPS_SETTINGS_REQUEST_CODE);
    }

}
