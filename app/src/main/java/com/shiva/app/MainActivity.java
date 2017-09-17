package com.shiva.app;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends BaseActivity {

    private TextView locData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLocationFetching(MainActivity.this);

        locData = (TextView) findViewById(R.id.locData);
    }


    public void locationFetch(Location mLocal, Location oldLocation, String time, String locationProvider) {
        //super.locationFetch(mLocal, oldLocation, time, locationProvider);
        Toast.makeText(getApplication(), "Lat : " + mLocal.getLatitude() + " Lng : " + mLocal.getLongitude(), Toast.LENGTH_LONG).show();

        try {
            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(mLocal.getLatitude(), mLocal.getLongitude(),1);

            Log.i("Location", addresses.get(0).toString());
            locData.setText(addresses.get(0).toString());

        }catch (Exception e){e.printStackTrace();}
    }
}
