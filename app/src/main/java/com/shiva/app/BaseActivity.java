package com.shiva.app;

import android.app.Activity;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.shiva.app.location.LocationInterface;
import com.shiva.app.location.SmartLocation;
import com.shiva.app.network.NetworkChangeReceiver;

/**
 * Created by OM on 17-09-2017.
 */

public class BaseActivity  extends AppCompatActivity implements LocationInterface {

    private static final int REQUEST_FINE_LOCATION = 1;
    private Activity mCurrentActivity;

    public SmartLocation mLocationManager;

    protected final IntentFilter mIntentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION); // A filter for a BR. We want to listen to internet changes
    protected final NetworkChangeReceiver mConnectionDetector = new NetworkChangeReceiver(); // Creating an instance of our BR for activity to use

    @Override
    public void locationFetch(Location location, Location oldLocation, String time, String locationProvider) {
// storing it on application level
        MainApplication.mCurrentLocation = location;
        MainApplication.oldLocation = oldLocation;
        MainApplication.locationProvider = locationProvider;
        MainApplication.locationTime = time;
    }

    public void initLocationFetching(Activity mActivity) {
        mCurrentActivity = mActivity;
        // ask permission for M
        // if (mLocationManager.askLocationPermission())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            showLocationPermission();
        } else {
            mLocationManager = new SmartLocation(getApplicationContext(),
                    mActivity, this, SmartLocation.ALL_PROVIDERS,
                    LocationRequest.PRIORITY_HIGH_ACCURACY,
                    SmartLocation.LOCATION_PROVIDER_RESTRICTION_NONE); // init location manager
        }
    }

    private void showLocationPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(mCurrentActivity, android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mCurrentActivity, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                //showExplanation("Permission Needed", "Rationale", Manifest.permission.READ_PHONE_STATE, REQUEST_FINE_LOCATION);
                requestPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_FINE_LOCATION);
            } else {
                requestPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_FINE_LOCATION);
            }
        } else {
            mLocationManager = new SmartLocation(getApplicationContext(), BaseActivity.this,
                    this, SmartLocation.ALL_PROVIDERS, LocationRequest.PRIORITY_HIGH_ACCURACY,
                    SmartLocation.LOCATION_PROVIDER_RESTRICTION_NONE); // init location manager
            //mLocationManager.startLocationFetching();
            //Toast.makeText(mCurrentActivity, "Permission (already) Granted!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            registerReceiver(mConnectionDetector, mIntentFilter); // Activity gets shown, we register a BR and it starts to receive notifications about internet changes
        } catch (Exception exc) {
            // whoops
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(mConnectionDetector); // Try to unregister BR, since when activity is not visible to user, we don't want to perform any operations on internet change
        } catch (Exception exc) {
            // whoops
        }
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(mCurrentActivity, new String[]{permissionName}, permissionRequestCode);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationManager = new SmartLocation(getApplicationContext(), BaseActivity.this,
                            this, SmartLocation.ALL_PROVIDERS, LocationRequest.PRIORITY_HIGH_ACCURACY,
                            SmartLocation.LOCATION_PROVIDER_RESTRICTION_NONE); // init location manager
                    mLocationManager.startLocationFetching();

                    Toast.makeText(BaseActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BaseActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

}

