package com.shiva.app.location;

import android.location.Location;

/**
 * Created by OM on 17-09-2017.
 */

public interface LocationInterface {
    String TAG = LocationInterface.class.getSimpleName();
    void locationFetch(Location location, Location oldLocation, String time, String locationProvider);
}
