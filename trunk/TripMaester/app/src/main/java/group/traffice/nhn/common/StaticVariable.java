package group.traffice.nhn.common;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.speech.tts.TextToSpeech;
import android.widget.ImageView;
import android.widget.TextView;

import org.osmdroid.views.overlay.PathOverlay;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import cse.its.dbhelper.NodeDrawable;
import cse.its.dbhelper.WarningDrawable;
import group.traffic.nhn.rightmenu.RightMenuGroup;

public class StaticVariable {
	// Routing algorithms
	public static final int DISTANCE_MODE = 0;
	public static final int REAL_TIME_MODE = 1;
	public static final int MULTI_POINT_PATH = 2;
	public static final int PREFERENT_REAL_TIME = 3;
	// public static final int PROFILE_TIME_MODE = 3;

	// Transportations
	public static final int CAR_MODE = 0;
	public static final int BIKE_MODE = 1;
	public static final int BUS_MODE = 2;
	public static final int TRUCK_MODE = 3;

	// Languages
	public static final int ENGLISH_MODE = 0;
	public static final int VIETNAMESE_MODE = 1;

	public static int INT_ROUTING_MODE = DISTANCE_MODE;
	public static int INT_TRANSPORTATION_MODE = CAR_MODE;
	public static int INT_LANGUAGE_MODE = ENGLISH_MODE;

	public static boolean VOICE = true;
	public static boolean NAVIGATE = true; // false: only routing, user can
											// choose starting point
	public static boolean FOLLOW = false; // true: always set map center as
											// user's location
	public static boolean MULTIPLE_POINT = false;

	public static boolean SHOW_TRAFFIC_INFO = false; // the flag control layer
														// traffic (variable or
														// not variable)
	public static boolean SHOW_WARNING_INFO = false; // the flag control showing
														// warning information

	public static final ArrayList<RightMenuGroup> GROUP_MAP_MENU_ITEMS = new ArrayList<>();

	public static ImageView IV_ROUTE;

	public static long LAST_TIME_REQUEST_ROUTING = 0;
	public static int START_SEGMENT_ID;
	public static int DES_SEGMENT_ID;

	// false: segment not found; true: valid segment
	public static boolean DES_SEG_ID_STATUS = false;
	public static boolean START_SEG_ID_STATUS = false;

	// false: block requesting while route parse doing in background
	public static boolean FINISH_ROUTING = true;

	public static boolean PATH_OVERLAY_EXIST = false;// if path overlay is added
														// on map

	// ###Routing variables
	public static NodeDrawable START_NODE;// Departure node
	public static NodeDrawable DES_NODE;// Destination node

	public static PathOverlay ROUTING_PATH;

	public static TextView TV_TIME_DESTANCE;
	public static TextView TV_VIA;
	public static TextView TV_ROUTING_DETAIL;

	public static Drawable DESTINATION_MARKER;

	public static TextToSpeech TEXT_TO_SPEECH;

	public static Dialog CONTEXT_MENU_DIAGLOG = null;
	// public static MapController MY_MAP_CONTROLLER;
	// public static List<Overlay> MAP_OVERLAY_LIST;
	public static boolean START_NEW_PATH = false;

	public static List<NodeDrawable> LIST_MULTIPLE_POINT = new ArrayList<>();
	public static Hashtable<String, Drawable> WARNING_ICON = new Hashtable<>();
	public static Drawable STARTING_MAKER;

	public static WarningDrawable WARNING_POINT = null;

}
