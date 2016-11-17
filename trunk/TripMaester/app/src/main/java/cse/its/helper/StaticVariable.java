package cse.its.helper;//package cse.its.helper;
//
//import java.util.ArrayList;
//import java.util.Hashtable;
//import java.util.List;
//
//import org.osmdroid.views.MapController;
//import org.osmdroid.views.overlay.Overlay;
//import org.osmdroid.views.overlay.PathOverlay;
//
//import cse.its.dbhelper.NodeDrawable;
//import cse.its.dbhelper.WarningDrawable;
//import android.app.Dialog;
//import android.graphics.drawable.Drawable;
//import android.speech.tts.TextToSpeech;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//public class StaticVariable {
//	public static int INT_ROUTING_MODE = ModeHelper.DISTANCE_MODE;
//	public static int INT_TRANSPORTATION_MODE = ModeHelper.CAR_MODE;
//	public static int INT_LANGUAGE_MODE = ModeHelper.ENGLISH_MODE;
//	
//	public static boolean VOICE = true;
//	public static boolean NAVIGATE = true;	// false: only routing, user can choose starting point
//	public static boolean FOLLOW = false;	// true: always set map center as user's location
//	public static boolean MULTIPLE_POINT = false;
//	
//	public static boolean SHOW_TRAFFIC_INFO = false; // the flag control layer traffic (variable or not variable)	
//	public static boolean SHOW_WARNING_INFO = false; // the flag control showing warning information
//	
//	public static ImageView IV_ROUTE;
//	
//	public static long  LAST_TIME_REQUEST_ROUTING = 0;
//	public static int START_SEGMENT_ID;
//	public static int DES_SEGMENT_ID;
//	
//	// false: segment not found; true: valid segment
//	public static boolean DES_SEG_ID_STATUS = false;
//	public static boolean START_SEG_ID_STATUS = false;
//	
//	// false: block requesting while route parse doing in background
//	public static boolean FINISH_ROUTING = true;
//	
//	public static boolean PATH_OVERLAY_EXIST = false;// if path overlay is added on map
//	
//	// ###Routing variables
//	public static NodeDrawable START_NODE;// Departure node
//	public static NodeDrawable DES_NODE;// Destination node
//		
//	public static PathOverlay ROUTING_PATH;
//	
//	public static TextView TV_TIME_DESTANCE;
//	public static TextView TV_VIA;
//	public static TextView TV_ROUTING_DETAIL;
//	
//	public static Drawable DESTINATION_MARKER;
//	
//	public static TextToSpeech TEXT_TO_SPEECH;
//	
//	public static Dialog CONTEXT_MENU_DIAGLOG = null;
////	public static MapController MY_MAP_CONTROLLER;
////	public static List<Overlay> MAP_OVERLAY_LIST;
//	public static boolean START_NEW_PATH = false;
//	
//	public static List<NodeDrawable> LIST_MULTIPLE_POINT = new ArrayList<NodeDrawable>();
//	public static Hashtable<String, Drawable> WARNING_ICON = new Hashtable<String, Drawable>();
//	public static Drawable STARTING_MAKER;
//	
//	public static WarningDrawable WARNING_POINT = null;
//	
//}
