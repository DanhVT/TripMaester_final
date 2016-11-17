package group.traffice.nhn.common;

import org.osmdroid.util.GeoPoint;

import group.traffic.nhn.map.MapFragment;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

public class MapLocationListener implements LocationListener {

	private MapFragment mapFragment;

	public MapLocationListener(MapFragment mapFragment) {
		this.mapFragment = mapFragment;
	}

	@Override
	public void onLocationChanged(Location arg0) {
		if (mapFragment.isStart) {
			//Location Listener
			
			mapFragment.lstPassedPoint.add(arg0);
			Toast.makeText(
					mapFragment.mainActivity.getApplicationContext(),
					"latitude = " + arg0.getLatitude() * 1e6 + " longitude = "
							+ arg0.getLongitude() * 1e6, Toast.LENGTH_SHORT)
					.show();

			//mapFragment.mLastLocation = arg0;

			int latitude = (int) (arg0.getLatitude() * 1E6);
			int longitude = (int) (arg0.getLongitude() * 1E6);
			GeoPoint point = new GeoPoint(latitude, longitude);
			mapFragment.updatePosition(point);
		}
	}
	

	@Override
	public void onProviderDisabled(String arg0) {

	}

	@Override
	public void onProviderEnabled(String arg0) {

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

	}

}
