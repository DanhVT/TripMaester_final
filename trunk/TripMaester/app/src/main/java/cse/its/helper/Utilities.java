package cse.its.helper;


import java.util.ArrayList;
import java.util.Date;

import org.osmdroid.util.GeoPoint;

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Utilities {
	/**
	 * get distance between 2 locations
	 * 
	 * @param loc1
	 * @param loc2
	 * @return
	 */
	public static double distance(Location loc1, Location loc2) {
		return 110000 * Math.sqrt((loc1.getLatitude() - loc2.getLatitude())
				* (loc1.getLatitude() - loc2.getLatitude())
				+ (loc1.getLongitude() - loc2.getLongitude())
				* (loc1.getLongitude() - loc2.getLongitude()));
	}

	public static double distanceInKm(GeoPoint currentLocation,
			GeoPoint location2) {
		// measure distance

		double earthRadius = 3958.75;

		double dLat = Math.toRadians(currentLocation.getLatitude()
				- location2.getLatitude());
		double dLng = Math.toRadians(currentLocation.getLongitude()
				- location2.getLongitude());
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(currentLocation.getLatitude()))
				* Math.cos(Math.toRadians(location2.getLatitude()))
				* Math.sin(dLng / 2) * Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		return dist;
	}
	
	public static double distanceInKm(ArrayList<GeoPoint> lstGeoPoints){
		double distance = 0;
		
		for(int i=0;i < lstGeoPoints.size()-1;i++){
			distance += distanceInKm(lstGeoPoints.get(i), lstGeoPoints.get(i+1));
		}
		distance = Math.round(distance * 10) / 10;
		return distance;
	}

	/**
	 * Checks if the device has Internet connection.
	 * 
	 * @return <code>true</code> if the phone is connected to the Internet.
	 */
	public static boolean hasConnection(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo wifiNetwork = cm
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetwork != null && wifiNetwork.isConnected()) {
			return true;
		}

		NetworkInfo mobileNetwork = cm
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetwork != null && mobileNetwork.isConnected()) {
			return true;
		}

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnected();

	}

	// convert string to datetime with given format
	public static Date convertStringToDateTime(String dateString) {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"dd/MM/yyyy HH:mm:ss");// 20\/07\/2015 05:50:34
		Date d = null;
		try {
			d = sdf.parse(dateString);
		} catch (Exception ex) {
			Log.i("convertStringToDateTime", ex.getMessage());
		}

		return d;
	}
}
