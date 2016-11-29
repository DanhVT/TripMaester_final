package cse.its.helper;

import android.location.LocationManager;

import org.osmdroid.util.GeoPoint;

/**
 * 
 * @author Vo tinh this class will be define the all const paramemters
 * 
 */
public class Constant {

	public static final String NETWORK_PROVIDER = LocationManager.NETWORK_PROVIDER;
	public static final String GPS_PROVIDER = LocationManager.GPS_PROVIDER;

	// ### Shared preferences variables
	public static final String PREFERENCES_NAME = "MyPrefs";
	public static final String STR_TRANS_KEY = "transportationKey";
	public static final String STR_ROUTING_MODE_KEY = "routingModeKey";
	public static final String STR_LANGUAGE_KEY = "languageKey";
	
	public static final int TRAFFIC_INFO_UPDATE_INTERVAL = 30000;
	public static final GeoPoint GEO_HCM_UNIV= new GeoPoint(10.770432, 106.658081);
	public final static String ROOT = "http://traffic.hcmut.edu.vn/";
	public final static String ITS = ROOT + "ITS/rest/";
	// public final static String ROOT = "http://221.133.13.113/";
	public final static String GOOGLE_ROUTING_API = "http://maps.googleapis.com/maps/api/directions/json?";
	public final static String GOOGLE_STREET_NAME = "http://maps.googleapis.com/maps/api/geocode/json?latlng=";
	public final static String SEGMENT_ID_URL = ITS + "segment_id/";
	// public final static String CURRENT_TRAFFIC_URL = ROOT +
	// "hcm/rest/segmentSpeedMongo?";
	public final static String CURRENT_TRAFFIC_URL = ROOT + "hcm/rest/rectangleSpeed?";
	public final static String SEARCH_LOCATION_URL = "http://www.openstreetmap.org/geocoder/search_osm_nominatim?query=";
	public final static String SEARCH_LOCATION_AREA_URL = "&xhr=1&zoom=12&minlon=106.61785125732422&minlat=10.702779662654743&maxlon=106.96666717529297&maxlat=10.82925950697292";
	public final static String OSM_ADDRESS_SEARCH_URL = "http://nominatim.openstreetmap.org/reverse?format=xml&lat=";
	public final static String HCMC = " Hồ Chí Minh";
	// motor, car
	public final static String DISTANCE = "distance_dijkstra1/";
	public final static String STATIC_TIME = "static_time_dijkstra1/";
	public final static String PROFILE_TIME = "profile_time_dijkstra1/";
	public final static String REAL_TIME = "real_time_dijkstra1/";
	public final static String MULTIPLE_POINTS="multiple_pointsPOST/";
	public final static String PREFERENT_USER = "preference_real_time_dijkstra1/";
	public static final int STARTING_POINT_MAKER = 0;
	public static final int DESTINATION_POINT_MAKER = 1;
	public static final int NORMAL_POINT_MAKER = 2;
	public static final int TIME_WARNING_INFO_UPDATE = 50000;
	public static final int TIME_TRAFFIC_INFO_UPDATE = 5000;
	public static final String CONSTRUCTION = "construction";
	public static final String POLICE = "police";
	public static final String ACCIDENT = "accident";
	public static final String BROKEN_LIGHT ="broken_light";
	public static final String FLOOD = "flood";
	public static final String WARNING = "warning";
	public static int ZOOM_LEVEL = 15;
	
	
	
	
}
