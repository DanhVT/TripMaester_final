package group.traffice.nhn.common;

import org.osmdroid.util.GeoPoint;

import android.provider.Settings.Secure;

public class Constants {
	public final static String LEFT_MENU_MODE = "leftmenumode"; 
	public final static String LEFT_MENU_SHARE ="leftmenushare";
	public final static String CLOSE_RIGHT_MENU ="closerightmenu";
	public final static int INT_MENU_ITEM_VOICE = 2;
	public final static int INT_MENU_ITEM_NAVIGATE = 3;
	public final static int INT_MENU_ITM_TRAFICC_INFO = 4;
	public final static int INT_MENU_ITEM_WARNING = 5;
	
	//map values
	public final static int ZOOM_LEVEL = 15;
	public final static GeoPoint GEO_HCM_UNIV= new GeoPoint(10.770432, 106.658081);
	public final static String DOMAIN_NAME = "traffic.hcmut.edu.vn";
	public static final int SERVER_PORT = 180;
	
	//files name
	public static final String FILE_FOLLOW_TRIP = "follow_trip.txt";
	public static final String FOLDER_FOLLOW_TRIP = "nhn";
}


