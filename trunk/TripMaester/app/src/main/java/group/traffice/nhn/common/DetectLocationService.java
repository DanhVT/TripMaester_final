package group.traffice.nhn.common;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;


import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.NetworkLocationIgnorer;

import java.util.ArrayList;

/**
 * Created by Xinh on 5/25/2015.
 */
public class DetectLocationService extends Service implements LocationListener {

    public static String TAG = "DetectLocationService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private LocationManager mLocationManager;
    private boolean startLocationUpdates = false;

    @Override
    public void onCreate() {
        super.onCreate();

        mLocationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null){
            location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if (location != null) {
            //location known:
            onLocationChanged(location); 

        } else {
            //  TODO: no location known
        }

        //  Start locattion update
        startLocationUpdates = startLocationUpdates();
        CommonFunction.getSavedBoolean(this, SharedPreferencesKeys.KEY_STARTLOCATIONUPDATES, startLocationUpdates);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopLocationUpdates();
        stopSelf();

        Log.e("Xinh", "onDestroy - "+TAG);

    }

    /*  Start - LocationListener  */
    private final NetworkLocationIgnorer mIgnorer = new NetworkLocationIgnorer();
    private long mLastTime = 0; // milliseconds
    private double mSpeed = 0.0; // km/h
    private float mAzimuthAngleSpeed = 0.0f;
    private double TIMER_DETECT_LOCATION = 1 * 4 * 1000; // 4s

    private GeoPoint mDeparture, mDestination;
    private ArrayList<GeoPoint> mViaPoint = new ArrayList<>();

    @Override
    public void onLocationChanged(Location location) {

        long currentTime = System.currentTimeMillis();
        if (mIgnorer.shouldIgnore(location.getProvider(), currentTime)){ return; }

        double dT = currentTime - mLastTime;
        //  Check if departure != null && difference time < TIMER_DETECT_LOCATION --> NOT save point
        if (null != mDeparture && dT < TIMER_DETECT_LOCATION){
            return;
        }
        mLastTime = currentTime;

        //  Sent update current location
        Intent intentUpdateCurrentLocation = new Intent();
        intentUpdateCurrentLocation.putExtra(FlagUpdates.FlagUpdate, FlagUpdates.FlagUpdate_CURRENT_LOCATION);
        intentUpdateCurrentLocation.putExtra(FlagUpdates.FlagUpdate_CURRENT_LOCATION, location);
        sendActionUpdate(intentUpdateCurrentLocation);
        Log.e("Xinh", "*** intent Update Current Location ***");

        GeoPoint newLocation = new GeoPoint(location);
        mAzimuthAngleSpeed = location.getBearing();

        // Check if mDeparture == null
        if(null == mDeparture){
            mDeparture = newLocation;

            //  Save departure
            String defineStrLoction = mDeparture.getLatitude() +":"+ mDeparture.getLongitude();
            CommonFunction.saveString(this, SharedPreferencesKeys.KEY_DEPARTURE, defineStrLoction);

            //  Sent broadcast to main to update departure point - first
            //  TODO
            Intent intent = new Intent();
            intent.putExtra(FlagUpdates.FlagUpdate_AZIMUTHANGLE, mAzimuthAngleSpeed);
            intent.putExtra(FlagUpdates.FlagUpdate, FlagUpdates.FlagUpdate_DEPARTURE);
            intent.putExtra(FlagUpdates.FlagUpdate_DEPARTURE, location);
            sendActionUpdate(intent);


            return;
        }

        if (null != location.getProvider() && location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
			/*
			double d = prevLocation.distanceTo(newLocation);
			mSpeed = d/dT*1000.0; // m/s
			mSpeed = mSpeed * 3.6; //km/h
			*/
            mSpeed = location.getSpeed() * 3.6;

            //TODO: check if speed is not too small
            if (mSpeed >= 0.1) {

                CommonFunction.saveFloat(this, SharedPreferencesKeys.KEY_AZIMUTHANGLE, mAzimuthAngleSpeed);

                if(null == mDestination){
                    mDestination = newLocation;

                    //  Save destination
                    String defineStrLoction = mDestination.getLatitude() +":"+ mDestination.getLongitude();
                    CommonFunction.saveString(this, SharedPreferencesKeys.KEY_DESTINATION, defineStrLoction);

                    //  Sent broadcast to main to update destination point - first
                    //  TODO
                    Intent intent = new Intent();
                    intent.putExtra(FlagUpdates.FlagUpdate_AZIMUTHANGLE, mAzimuthAngleSpeed);
                    intent.putExtra(FlagUpdates.FlagUpdate, FlagUpdates.FlagUpdate_DESTINATION);
                    intent.putExtra(FlagUpdates.FlagUpdate_DESTINATION, location);
                    sendActionUpdate(intent);

                    return;
                }

                //  Add via point
                mViaPoint.add(mDestination);
                //  Save via point
                if(mViaPoint.size() == 0) {
                    String defineStrLoctions = mDestination.getLatitude() + ":" + mDestination.getLongitude() +";";
                    CommonFunction.saveString(this, SharedPreferencesKeys.KEY_VIAPOINTT, defineStrLoctions);
                }
                else{

                    GeoPoint geoPoint = mViaPoint.get(mViaPoint.size()-1);

                    String defineStrLoctions = CommonFunction.getSavedString(this, SharedPreferencesKeys.KEY_VIAPOINTT);
                    defineStrLoctions += geoPoint.getLatitude() + ":" + geoPoint.getLongitude() +";";

                    CommonFunction.saveString(this, SharedPreferencesKeys.KEY_VIAPOINTT, defineStrLoctions);
                }

                //  Set new destination
                mDestination = newLocation;
                //  Save destination
                String defineStrLoction = mDestination.getLatitude() +":"+ mDestination.getLongitude();
                CommonFunction.saveString(this, SharedPreferencesKeys.KEY_DESTINATION, defineStrLoction);

                //  Sent broadcast to main to update road
                //  TODO
                Intent intent = new Intent();
                intent.putExtra(FlagUpdates.FlagUpdate_AZIMUTHANGLE, mAzimuthAngleSpeed);
                intent.putExtra(FlagUpdates.FlagUpdate, FlagUpdates.FlagUpdate_VIAPOINT);
                intent.putParcelableArrayListExtra(FlagUpdates.FlagUpdate_VIAPOINT, mViaPoint);
                intent.putExtra(FlagUpdates.FlagUpdate_DESTINATION, location);

                sendActionUpdate(intent);

            }
        }


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //  TODO
    }

    @Override
    public void onProviderEnabled(String provider) {
        //  TODO
    }

    @Override
    public void onProviderDisabled(String provider) {
        //  TODO
    }

    /**
     *
     * Define function
     *
     */

    /**
     * Send action update map for main activity
     */
    private void sendActionUpdate(Intent intent){
        intent.setAction(StaticStrings.IntentFilter_ACTION_LOCATION_CHANGE);
        sendBroadcast(intent);
    }

    /**
     * Start location update
     * @return
     */
    boolean startLocationUpdates(){
        boolean result = false;
        for (final String provider : mLocationManager.getProviders(true)) {
            mLocationManager.requestLocationUpdates(provider, 2*1000, 0.0f, this);
            result = true;
        }
        return result;
    }

    void stopLocationUpdates(){
        mLocationManager.removeUpdates(this);
    }

}
