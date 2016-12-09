package group.traffic.nhn.map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer;
import org.osmdroid.bonuspack.location.GeocoderNominatim;
import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.bonuspack.overlays.FolderOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.MarkerInfoWindow;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.DirectedLocationOverlay;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.PathOverlay;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.UUID;

import cse.its.adapter.ContextMenuAdapter;
import cse.its.adapter.StreetListAdapter;
import cse.its.dbhelper.NodeDrawable;
import cse.its.dbhelper.WarningDrawable;
import cse.its.helper.Constant;
import cse.its.helper.ModeHelper;
import cse.its.helper.UrlHelper;
import cse.its.helper.Utilities;
import cse.its.listeners.OnContextItemClickListener;
import cse.its.parser.AddressParser;
import cse.its.parser.RouteParser;
import cse.its.parser.SearchParser;
import cse.its.parser.SegmentIDParser;
import cse.its.parser.SegmentIDParser2;
import cse.its.parser.TrafficInfoParser;
import cse.its.parser.WarningInfoParser;
import cse.its.sendpacket.GSPSender;
import cse.its.slidingmenu.ContextMenuItem;
import group.traffic.nhn.asynctask.LocationSender;
import group.traffic.nhn.trip.PointItem;
import group.traffice.nhn.common.BuildConfigs;
import group.traffice.nhn.common.Constants;
import group.traffice.nhn.common.DetectLocationService;
import group.traffice.nhn.common.FlagUpdates;
import group.traffice.nhn.common.LogFile;
import group.traffice.nhn.common.StaticStrings;
import group.traffice.nhn.common.StaticVariable;
import vn.edu.hcmut.its.tripmaester.R;
import vn.edu.hcmut.its.tripmaester.controller.ICallback;
import vn.edu.hcmut.its.tripmaester.controller.manager.LoginManager;
import vn.edu.hcmut.its.tripmaester.helper.CameraHelper;
import vn.edu.hcmut.its.tripmaester.model.Trip;
import vn.edu.hcmut.its.tripmaester.service.http.HttpManager;
import vn.edu.hcmut.its.tripmaester.ui.activity.MainActivity;
import vn.edu.hcmut.its.tripmaester.ui.activity.VideoPlayer;

public class MapFragment extends Fragment implements MapEventsReceiver,
        SensorEventListener {
    private static final String TAG = MapFragment.class.getSimpleName();
    // Storage for camera image URI components
    private final static int REQUEST_TAKE_PHOTO = 100;
    private final static int REQUEST_TAKE_VIDEO = 200;
    private final static String FOLDER_NAME = "TempFolder";
    public static MapFragment Instance = null;
    /**
     * Async task to get the road in a separate thread.
     */
    public static Road mRoad; // made static to pass between activities
    public static ArrayList<POI> mPOIs; // made static to pass between
    protected static int START_INDEX = -2, DEST_INDEX = -1;
    public static MapView mMapView;
    private static MapController mMapController;
    private static List<Overlay> mOverlays;
    private static String mDeviceId;
    private static LocationSender mLocationSender;
    private static RouteParser mRouteParserController = null;
    public static ArrayList<MyMarker> listMarkerTrip;

    /**
     * Define interface
     */
    final OnItineraryMarkerDragListener mItineraryListener = new OnItineraryMarkerDragListener();
    public MainActivity mainActivity;
    public boolean isStart = false;
    // TRIP INFO
    public List<Location> lstPassedPoint;// list of points user went pass
    // protected MapView map;
    protected DirectedLocationOverlay myLocationOverlay;
    protected ArrayList<GeoPoint> viaPoints;
    protected FolderOverlay mKmlOverlay; // root container of overlays from KML
    protected boolean mStartTracking = false;
    protected Marker markerStart, markerDestination;
    // protected ViaPointInfoWindow mViaPointInfoWindow;
    protected FolderOverlay mItineraryMarkers;
    protected Polyline mRoadOverlay;
    protected FolderOverlay mRoadNodeMarkers;
    // activities
    RadiusMarkerClusterer mPoiMarkers;
    ArrayList<Road> lstUserPassRoad = new ArrayList<Road>();
    private Context mContext;
    private Context mContext_FLTrip;
    private MapOverlayListener mMapOverlyListener;
    private LocationManager mLocationManager;
    private Location mLastLocation;
    private LogFile mLogFile;
    private TextToSpeech mTextToSpeech;
    private Drawable mDrawablePosition;
    private PathOverlay mPathOverlay;
    private int screen_width;
    private int screen_height;
    private int cross_line;
    private Drawable position_marker;
    //	private SharedPreferences mSharedpreferences;
    private double lat, lon;
    private Location lastStartingLocation;
    /**
     * parameter for context menu item
     */
    private List<ContextMenuItem> mContextMenuItems = new ArrayList<ContextMenuItem>();
    private LayoutInflater inflater;
    private View child;
    private ListView mListViewContext;
    private ContextMenuAdapter contextMenuAdapter;
    private boolean start_marker_exist = false;
    private boolean des_marker_exist = false;
    private ItemizedOverlay<OverlayItem> startingPointOverlay;
    private ItemizedOverlay<OverlayItem> destinationOverlay;
    private ItemizedOverlay<OverlayItem> userLocationOverlay;
    private List<Overlay> multipleOverlay = new ArrayList<Overlay>();
    private TextView tv_routing_mode;
    private LinearLayout ll_routing_info, ll_distance_time_via,
            ll_search_routing_mode;
    private View ll_info_bar;
    private EditText et_search;
    private ImageView iv_search, iv_transportation;
    private Animation slide_down, slide_up, slide_right;
    private Location lastLocation;
    private boolean add_des = false; // false: add starting point, true: add
    // destination
    private DynamicOverlay userLocDynamicOverlay;
    private IGeoPoint currentMapCenterPoint;
    private TrafficInfoParser trafficInfoParser;
    private WarningInfoParser warningIfoParser;
    private Handler trafficHandler;
    private Runnable trafficRunnable;
    private Handler warningHandler;
    private Runnable warningRunnable;
    private FloatingActionButton fab_gps;
    private Drawable gps_drawable, gps_follow_drawable;
    private int radius = 2500;
    private long lastTimeUpdateGPS = System.currentTimeMillis();
    private long lastTimeReroute = System.currentTimeMillis();
    private LocationListener locationListener = new MyLocationListener();
    private Animation Move_Duoi, Move_Tren, Back_Duoi, Back_Tren;
    private  boolean move_back = false;
    //===============
    // map location listener
    // private MapLocationListener mapLocationListener;
    private ResourceProxy resourceProxy;
    // reading
    private List<GeoPoint> waypoints;
    private RoadManager roadManager;
    private Polyline roadOverlay;
    private Button btn_capture;
    private String currentPath = null;
    private View rootView;
    // Required for camera operations in order to save the image file on resume.
    private static List<MyMarker> lst_markers;
    private List<MyMarker> lst_around_markers;
    private int selectMarkerChoice;
    private boolean isShowDialogMarker = false;
    private Button btn_start = null;
    private boolean isStartingText = true;
    private Date currentTime_start_trip;
    private Button btnStart, btnCurrentLocation;
    private float mAzimuthAngleSpeed = 0.0f;
    private GeoPoint mDeparture, mDestination;// , mLastLocation;
    private List<Bitmap> lst_user_capture_bitmap;
    private boolean isCapturing;
    private ReceiverLocationChange mReceiverLocationChange = new ReceiverLocationChange();
    private File fileUri;

    public MapFragment() {

    }

    /**
     * Receiver location change
     */

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth,
                                                     int reqHeight) { // BEST QUALITY MATCH

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            // if(Math.round((float)width / (float)reqWidth) > inSampleSize) //
            // If bigger SampSize..
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * IRequest routing from service ITS or Google
     *
     * @param context       : application context
     * @param myOpenMapView : the object was used to draw
     * @param rerouteMode   : the flag reroute
     */
    public static void requestRouting(Context context, MapView myOpenMapView,
                                      Boolean rerouteMode) {
        StaticVariable.LAST_TIME_REQUEST_ROUTING = System.currentTimeMillis();
        StaticVariable.FINISH_ROUTING = false;

        int apiMode = RouteParser.GOOGLE_API_MODE; // set google route mode
        if (StaticVariable.START_SEGMENT_ID > 0
                && StaticVariable.START_SEGMENT_ID > 0)
            apiMode = RouteParser.ITS_API_MODE; // set ITS route mode

        // set start and destination segment id
        StaticVariable.START_NODE.setId(StaticVariable.START_SEGMENT_ID);
        StaticVariable.DES_NODE.setId(StaticVariable.DES_SEGMENT_ID);

        UrlHelper routingUrlHelper = new UrlHelper(StaticVariable.START_NODE,
                StaticVariable.DES_NODE);


        // to create full path url to request traffic data
        String routingUrl = routingUrlHelper.getUrl(
                StaticVariable.INT_ROUTING_MODE,
                StaticVariable.INT_TRANSPORTATION_MODE, apiMode);

        LogFile.writeToFile(mDeviceId + " #3 URL " + routingUrl);
//		if (mLocationSender == null)
        mLocationSender = new LocationSender(context);
        mLocationSender.execute(" #3 URL " + routingUrl);

        // remove lasted routing path from mapoverlaylist object
        mOverlays.remove(StaticVariable.ROUTING_PATH);
        StaticVariable.PATH_OVERLAY_EXIST = false;

        // to do request traffic data from service using URL
//		new RouteParser(Instance.getActivity().getWindow().getContext(), myOpenMapView, rerouteMode, apiMode).execute(routingUrl);
        new RouteParser(Instance.mContext, myOpenMapView, rerouteMode, apiMode).execute(routingUrl);
//		mRouteParserController.setRouteMode(myOpenMapView, rerouteMode, apiMode );
//		mRouteParserController.execute(routingUrl);

        Log.wtf("AirDistance",
                NodeDrawable.getDistance(StaticVariable.START_NODE,
                        StaticVariable.DES_NODE) + " ");
        double air_distance = NodeDrawable.getDistance(
                StaticVariable.START_NODE, StaticVariable.DES_NODE) / 1000;
        if (StaticVariable.FOLLOW) {
            mMapController.setZoom(16);
            mMapController.animateTo(new GeoPoint(StaticVariable.START_NODE
                    .getLat(), StaticVariable.START_NODE.getLon()));
        } else {
            if (air_distance > 7)
                mMapController.setZoom(13);
            else if (air_distance > 2.5)
                mMapController.setZoom(14);
            else if (air_distance > 1.3)
                mMapController.setZoom(15);
            else
                mMapController.setZoom(16);
            mMapController
                    .animateTo(new GeoPoint(
                            (StaticVariable.START_NODE.getLat() + StaticVariable.DES_NODE
                                    .getLat()) / 2, (StaticVariable.START_NODE
                            .getLon() + StaticVariable.DES_NODE
                            .getLon()) / 2));
        }
    }

    public void addViaPoint(GeoPoint p) {
        // Not add added_added
        if (viaPoints.contains(p)) {
            return;
        }
        viaPoints.add(p);



        if (BuildConfigs.DRAW_ROAD_BY_CALL_API) {
            updateItineraryMarker(null, p, viaPoints.size() - 1,
                    R.string.viapoint, R.drawable.marker_via, -1, null);
        }
    }

    private void clear4NewPathInMultiplePath() {
        if (StaticVariable.START_NEW_PATH) {
            removePreviousMakingPoints(true);
        }
    }

    /**
     * clear routing info
     */
    public void clearRoutingInfo() {
        StaticVariable.TV_TIME_DESTANCE.setVisibility(View.GONE);
        StaticVariable.TV_VIA.setVisibility(View.GONE);
        StaticVariable.TV_ROUTING_DETAIL.setVisibility(View.GONE);
    }

    // ### clear all routing overlay: marker and path
    public void clearRoutingOverlay() {
        mOverlays.remove(startingPointOverlay);
        mOverlays.remove(destinationOverlay);

        //remove multiple point maker
        removePreviousMakingPoints(true);

        mOverlays.remove(StaticVariable.ROUTING_PATH);
        mMapView.invalidate();
        start_marker_exist = false;
        StaticVariable.START_SEG_ID_STATUS = false;
        des_marker_exist = false;
        StaticVariable.DES_SEG_ID_STATUS = false;
        StaticVariable.PATH_OVERLAY_EXIST = false;


        clearRoutingInfo();
    }

    /**
     * create warning context menu items
     */
    private void createContextMenu() {
        if (mContextMenuItems.size() > 0)
            return;

        inflater = (LayoutInflater) getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        child = inflater.inflate(R.layout.listview_context_menu, null);
        mListViewContext = (ListView) child
                .findViewById(R.id.listView_context_menu);
        mContextMenuItems = new ArrayList<ContextMenuItem>();

        // accident item
        mContextMenuItems.add(new ContextMenuItem(getResources().getDrawable(
                R.drawable.ic_accident), getResources().getString(
                R.string.it_accident)));

        // broken light
        mContextMenuItems.add(new ContextMenuItem(getResources().getDrawable(
                R.drawable.ic_broken_light), getResources().getString(
                R.string.it_brokenlight)));

        // carefully
        mContextMenuItems.add(new ContextMenuItem(getResources().getDrawable(
                R.drawable.ic_warning), getResources().getString(R.string.it_care)));

        // construction
        mContextMenuItems.add(new ContextMenuItem(getResources().getDrawable(
                R.drawable.ic_construction), getResources().getString(
                R.string.it_construc)));

        // flood
        mContextMenuItems
                .add(new ContextMenuItem(getResources().getDrawable(
                        R.drawable.ic_flood), getResources().getString(
                        R.string.it_flood)));

        // police
        mContextMenuItems.add(new ContextMenuItem(getResources().getDrawable(
                R.drawable.ic_police), getResources().getString(
                R.string.it_police)));

        contextMenuAdapter = new ContextMenuAdapter(mContext, mContextMenuItems);
        mListViewContext.setAdapter(contextMenuAdapter);
        mListViewContext.setOnItemClickListener(new OnContextItemClickListener(
                getActivity().getApplicationContext()));

        if (StaticVariable.CONTEXT_MENU_DIAGLOG == null) {
            StaticVariable.CONTEXT_MENU_DIAGLOG = new Dialog(mContext);
        }
        StaticVariable.CONTEXT_MENU_DIAGLOG.setTitle(R.string.warning);
        StaticVariable.CONTEXT_MENU_DIAGLOG.setContentView(child);

    }

    public void drawTrip(ArrayList<Road> lstRoads) {
        for (int i = 0; i < lstRoads.size(); i++) {
            updateUIWithRoadForTrip(lstRoads.get(i));
        }
    }

    // ### execute asynctask GET segment ID
    public void executeSegIDParser(Context context, MapView mapView,
                                   double lat, double lon, int mode) {
        // mode 1: destination point; 0: starting point
        String url = Constant.SEGMENT_ID_URL + lat + "/" + lon;
        if (mode == SegmentIDParser.STARTING_POINT_MODE)
            StaticVariable.DES_SEG_ID_STATUS = false;
        else if (mode == SegmentIDParser.DESINATION_MODE)
            StaticVariable.START_SEG_ID_STATUS = false;
        new SegmentIDParser(context, mapView, mode).execute(url);
    }

    // ### execute asynctask GET segment ID
    public void executeSegIDParser2(Context context, MapView mapView, int index) {
        String url = Constant.SEGMENT_ID_URL + lat + "/" + lon;
        new SegmentIDParser2(context, mapView, index).execute(url);
    }

    /**
     * Reverse Geocoding
     */
    public String getAddress(GeoPoint p) {
        GeocoderNominatim geocoder = new GeocoderNominatim(mainActivity);
        String theAddress;
        try {
            double dLatitude = p.getLatitude();
            double dLongitude = p.getLongitude();
            List<Address> addresses = geocoder.getFromLocation(dLatitude,
                    dLongitude, 1);
            StringBuilder sb = new StringBuilder();
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                int n = address.getMaxAddressLineIndex();
                for (int i = 0; i <= n; i++) {
                    if (i != 0)
                        sb.append(", ");
                    sb.append(address.getAddressLine(i));
                }
                theAddress = new String(sb.toString());
            } else {
                theAddress = null;
            }
        } catch (IOException e) {
            theAddress = null;
        }
        if (theAddress != null) {
            return theAddress;
        } else {
            return "";
        }
    }

    void getPOIAsync(String tag) {
        mPoiMarkers.getItems().clear();
        new POILoadingTask().execute(tag);
    }

    //FIXME: draw road has passed
    public void getRoadAsync() {
        mRoad = null;
        GeoPoint roadStartPoint = null;
        if (mDeparture != null) {
            roadStartPoint = mDeparture;
        } else if (myLocationOverlay.isEnabled()
                && myLocationOverlay.getLocation() != null) {
            // use my current location as itinerary start point:
            roadStartPoint = myLocationOverlay.getLocation();
        }
        if (roadStartPoint == null || mDestination == null) {
            updateUIWithRoad(mRoad);
            return;
        }
        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>(2);
        waypoints.add(roadStartPoint);
        // add intermediate via points:
        for (GeoPoint p : viaPoints) {
            waypoints.add(p);
        }
        waypoints.add(mDestination);

        //call update road online or offline
        if (BuildConfigs.DRAW_ROAD_BY_CALL_API) {
            new UpdateRoadTask().execute(waypoints);
        } else {
            new UpdateRoadOfflineTask().execute(waypoints);
        }
    }

    /**
     * initiate
     */
    private void initialize() {

        mContext = getActivity().getWindow().getContext();

        // android device id
        mDeviceId = Secure.getString(mContext.getContentResolver(),
                Secure.ANDROID_ID);

        // Set up log file
        mLocationSender = new LocationSender(mContext);
        mLogFile = new LogFile(mContext);

        // log sender data
        Calendar c = Calendar.getInstance();
        String current_time = c.get(Calendar.HOUR_OF_DAY) + ":"
                + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
        String current_date = c.get(Calendar.DATE) + "-"
                + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR);
        LogFile.writeToFile(mDeviceId + " #1 STARTING APP " + current_time
                + "  " + current_date);
        mLocationSender.execute(" #1 STARTING APP ");

        // Text-to-Speech
        StaticVariable.TEXT_TO_SPEECH = new TextToSpeech(getActivity(),
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        StaticVariable.TEXT_TO_SPEECH.setLanguage(Locale.US);
                        StaticVariable.TEXT_TO_SPEECH.setSpeechRate(0.7f);
                    }
                });

        // ###Setup animation
        slide_right = AnimationUtils
                .loadAnimation(mContext, R.anim.slide_right);
        slide_down = AnimationUtils.loadAnimation(mContext, R.anim.slide_down);
        slide_up = AnimationUtils.loadAnimation(mContext, R.anim.slide_up);
        slide_up.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                Log.wtf("SLIDE UP", "end");
                ll_routing_info.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
                Log.wtf("SLIDE UP", "start");
            }
        });
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screen_width = size.x;
        screen_height = size.y;
        cross_line = (int) Math
                .sqrt((screen_height * screen_height + screen_width
                        * screen_width));

        Log.wtf("CROSS LINE", cross_line + " ");

        // warning icon loaded
        loadWarningIcon();
        createContextMenu();

//		// get shared preferences
//		mSharedpreferences = getActivity().getSharedPreferences(
//				Constant.PREFERENCES_NAME, Context.MODE_PRIVATE);

    }

    /**
     * load icon for warning information
     */
    private void loadWarningIcon() {
        StaticVariable.DESTINATION_MARKER = getResources().getDrawable(
                R.drawable.des_icon);
        StaticVariable.STARTING_MAKER = getResources().getDrawable(
                R.drawable.starting_point);
        StaticVariable.WARNING_ICON.put("accident",
                getResources().getDrawable(R.drawable.ic_accident));
        StaticVariable.WARNING_ICON.put("brokenlight", getResources()
                .getDrawable(R.drawable.ic_broken_light));
        StaticVariable.WARNING_ICON.put("warning",
                getResources().getDrawable(R.drawable.ic_warning));
        StaticVariable.WARNING_ICON.put("construction", getResources()
                .getDrawable(R.drawable.ic_construction));
        StaticVariable.WARNING_ICON.put("flood",
                getResources().getDrawable(R.drawable.ic_flood));
        StaticVariable.WARNING_ICON.put("police",
                getResources().getDrawable(R.drawable.ic_police));
    }

    @Override
    public boolean longPressHelper(GeoPoint arg0) {
        return false;
    }

    public void mapResume() {
        StaticVariable.SHOW_TRAFFIC_INFO = false;
        // ### register location listener
        mLocationManager.requestLocationUpdates(Constant.GPS_PROVIDER, 0, 0,
                locationListener);
        if (mLocationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)) {
            mLocationManager.requestLocationUpdates(Constant.NETWORK_PROVIDER, 0, 0,
                    locationListener);
        }
        StaticVariable.GROUP_MAP_MENU_ITEMS.get(3).check = StaticVariable.VOICE;
        if (StaticVariable.NAVIGATE) {
            // set check for navigate mode menu
            StaticVariable.GROUP_MAP_MENU_ITEMS.get(4).check = StaticVariable.NAVIGATE;
//				if (StaticVariable.FOLLOW)
//					fab_gps.setDrawable(gps_follow_drawable);
//				else
//					fab_gps.setDrawable(gps_drawable);
        } else {
            StaticVariable.FOLLOW = false;
//				fab_gps.setDrawable(gps_drawable);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    //===============

    /**
     * The activity returns with the photo.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            if (requestCode == REQUEST_TAKE_PHOTO) {
                if(resultCode == Activity.RESULT_OK && fileUri.getPath() != null) {
                    // Bundle extras = data.getExtras();
                    // Bitmap imageBitmap = (Bitmap) extras.get("data");

                    // Mimimum acceptable free memory you think your app needs
                    long minRunningMemory = (12024 * 12024);

                    Runtime runtime = Runtime.getRuntime();

                    if (runtime.freeMemory() < minRunningMemory)
                        System.gc();

                    String filePath = fileUri.getPath();

                    Bitmap imageBitmap = decodeSampledBitmapFromFile(filePath, 400, 600);// BitmapFactory.decodeFile(filePath,
                    // options);

                    if (lastLocation == null) {
                        lastLocation = mLocationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
//					updateCurrentLocation();
                    }

                    // if user want to capture more image or not
                    if (!isCapturing) {
                        GeoPoint geoPoint = new GeoPoint(lastLocation);
                        setMarker(geoPoint, imageBitmap);

                    } else {
                        // save to list image of marker
                        if (lst_markers != null && lst_markers.size() > 1) {
                            Marker marker = lst_markers.get(lst_markers.size() - 1).getMarker();
                            ViaPointInfoWindow viaInfoWindow = (ViaPointInfoWindow) marker
                                    .getInfoWindow();
                            viaInfoWindow.addImage(imageBitmap);
                            viaInfoWindow.setLastImageButton();
                        }

                        new AlertDialog.Builder(mainActivity)
                                .setTitle(mainActivity.getString(R.string.take_photo_title))
                                .setMessage(mainActivity.getString(R.string.take_photo_prompt))
                                .setPositiveButton(android.R.string.yes,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                                isCapturing = true;
//										btn_capture.performClick();
                                                MainActivity.fab_camera.performClick();
                                            }
                                        })
                                .setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                                // FIXME: save marker
                                                isCapturing = false;
                                                lst_user_capture_bitmap = new ArrayList<Bitmap>();

                                            }
                                        }).setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                    }
                    //JSONObject json  = HttpManager.uploadImage(mTempCameraPhotoFile.getPath());
//                    new UploadAsync(filePath, CameraHelper.getMimeType(filePath), mContext).execute(fileUri);
                }
                else {
                    Toast.makeText(mainActivity, mainActivity.getString(R.string.take_photo_fail),
                            Toast.LENGTH_SHORT).show();
                }
            }

            if (requestCode == REQUEST_TAKE_VIDEO) {
                if(resultCode == Activity.RESULT_OK && fileUri.getPath() != null){
                    String filePath = fileUri.getPath();
                    Bitmap bmThumbnail;
                    bmThumbnail = ThumbnailUtils.createVideoThumbnail(filePath,
                            MediaStore.Video.Thumbnails.MICRO_KIND);
                    GeoPoint geoPoint = new GeoPoint(lastLocation);
                    setMarker(geoPoint, bmThumbnail);

//            File file = new File (currentPath);
//            new UploadAsync(currentPath,  CameraHelper.getMimeType(currentPath) , mContext).execute(file);
                }
                else{
                    Toast.makeText(mainActivity, mainActivity.getString(R.string.take_video_fail), Toast.LENGTH_SHORT ).show();
                }

            }
        }
        catch (NullPointerException ex){
            ex.printStackTrace();
        }

    }

    public boolean onChildClick(int groupPosition, int childPosition) {
        Log.wtf("Sliding CHILD menu item clicked", " " + groupPosition);

        // ### Transportation menu
        if (groupPosition == 0) {
            StaticVariable.INT_TRANSPORTATION_MODE = childPosition;
            if (des_marker_exist && StaticVariable.PATH_OVERLAY_EXIST) {
                requestRouting(mContext, mMapView, false);
            }
            // ### change the transportation image
            setTransportationImage(StaticVariable.INT_TRANSPORTATION_MODE);
        }

        // ### Routing algorithm
        if (groupPosition == 1) {
            StaticVariable.INT_ROUTING_MODE = childPosition;
            // set multiple point
            StaticVariable.MULTIPLE_POINT = childPosition == 2;

            clearRoutingOverlay();
//			clearRoutingInfo();
            // ### change content of routing mode text view
            setRoutingModeText(StaticVariable.INT_ROUTING_MODE);

        }

        return false;
    }

    public boolean onClickGroupMapMenuItem(int groupPosition) {
        Log.wtf("Sliding GROUP menu item clicked", " " + groupPosition);

        switch (groupPosition) {
            case Constants.INT_MENU_ITEM_VOICE: // voice menu
                StaticVariable.VOICE = !StaticVariable.VOICE;
                StaticVariable.GROUP_MAP_MENU_ITEMS.get(groupPosition).check = StaticVariable.VOICE;//!StaticVariable.GROUP_MAP_MENU_ITEMS.get(groupPosition).check;
                break;
            case Constants.INT_MENU_ITEM_NAVIGATE:
                if (StaticVariable.NAVIGATE) {
                    StaticVariable.FOLLOW = false;
                    mMapView.setLayoutParams(new LinearLayout.LayoutParams(screen_width, screen_height));
                    if (mMapView.getRotation() != 0)
                        mMapView.setRotation(0);
                } else {
                    if (StaticVariable.FOLLOW) {
//						fab_gps.setDrawable(gps_follow_drawable);
                    } else {
//						fab_gps.setDrawable(gps_drawable);
                    }
                }
                add_des = false;
                clearRoutingOverlay();
                if (lastLocation != null)
                    updateUserLocationMarker(position_marker,
                            lastLocation.getLatitude(),
                            lastLocation.getLongitude());

                StaticVariable.NAVIGATE = !StaticVariable.NAVIGATE;
                StaticVariable.GROUP_MAP_MENU_ITEMS.get(groupPosition).check = StaticVariable.NAVIGATE;//!StaticVariable.GROUP_MAP_MENU_ITEMS.get(groupPosition).check;

//				fab_gps.setDrawable(gps_drawable);
                break;

            case Constants.INT_MENU_ITM_TRAFICC_INFO:
                if (StaticVariable.SHOW_TRAFFIC_INFO) {// disable

                    mOverlays.removeAll(TrafficInfoParser.listPath);
                    mMapView.invalidate();
                    trafficHandler.removeCallbacks(trafficRunnable);
                } else {// enable
                    trafficHandler.post(trafficRunnable);
                }
                StaticVariable.SHOW_TRAFFIC_INFO = !StaticVariable.SHOW_TRAFFIC_INFO;
                StaticVariable.GROUP_MAP_MENU_ITEMS.get(groupPosition).check = StaticVariable.SHOW_TRAFFIC_INFO;
                //!StaticVariable.GROUP_MAP_MENU_ITEMS.get(groupPosition).check;


                break;
            case Constants.INT_MENU_ITEM_WARNING:
                if (StaticVariable.SHOW_WARNING_INFO) {
                    mOverlays.removeAll(WarningInfoParser.listWarning);
                    mMapView.invalidate();
                    warningHandler.removeCallbacks(warningRunnable);
                } else {
                    warningHandler.post(warningRunnable);
                }
                StaticVariable.SHOW_WARNING_INFO = !StaticVariable.SHOW_WARNING_INFO;
                StaticVariable.GROUP_MAP_MENU_ITEMS.get(groupPosition).check = StaticVariable.SHOW_WARNING_INFO;//!StaticVariable.GROUP_MAP_MENU_ITEMS.get(groupPosition).check;

                break;
            default:
                break;

        }

        switch (groupPosition) {

            case 6: // show warning information

        }

        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initialize();

        View rootView = inflater.inflate(R.layout.fragment_map, container,
                false);

        onLoadViewAndMap(rootView);

        Instance = this;
        return rootView;
    }
    /**
     * load map and view
     *
     * @param root
     */
    @SuppressLint("NewApi")
    private void onLoadViewAndMap(View root) {
        mMapView = (MapView) root.findViewById(R.id.openmapview);
        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        mMapView.setMultiTouchControls(true);
        mMapView.setBuiltInZoomControls(false);

        // register context menu for mapview
        registerForContextMenu(mMapView);

        // add controller
        mMapController = (MapController) mMapView.getController();
        mMapController.setZoom(Constants.ZOOM_LEVEL);
        mMapController.animateTo(Constants.GEO_HCM_UNIV);

        // add overly listener
        // mMapOverlyListener = new MapOverlayListener(getActivity()); // Add
        // Map overlay listener to catch touch event on map

        mMapOverlyListener = new MapOverlayListener(getActivity()); // Add Map
        // overlay
        // listener
        // to catch
        // touch
        // event on
        // map

        mOverlays = mMapView.getOverlays();
        mOverlays.add(mMapOverlyListener);

        mPathOverlay = new PathOverlay(Color.parseColor("#00FF0000"), mContext);
        mDrawablePosition = getResources().getDrawable(R.drawable.ic_position);

        StaticVariable.ROUTING_PATH = new PathOverlay(Color.RED, getActivity()
                .getApplicationContext());
        position_marker = getResources().getDrawable(R.drawable.position2);

        et_search = (EditText) root.findViewById(R.id.et_search);
        et_search.setHint(mContext.getResources().getString(R.string.search));
        et_search.setBackground(null);
        et_search
                .setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId,
                                                  KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            performSearch();
                            return true;
                        }
                        return false;
                    }
                });

        iv_search = (ImageView) root.findViewById(R.id.iv_search);
        iv_search.setImageResource(R.drawable.search);
        iv_search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                performSearch();
            }
        });

        // ### set up view routing info
        ll_info_bar = root.findViewById(R.id.ll_info_bar);
        ll_info_bar.setBackgroundColor(Color.WHITE);
        if (Build.VERSION.SDK_INT > 10)
            ll_info_bar.setAlpha(0.7f);

        tv_routing_mode = (TextView) root.findViewById(R.id.tv_routing_mode);
        iv_transportation = (ImageView) root
                .findViewById(R.id.iv_transportation);

        StaticVariable.TV_TIME_DESTANCE = (TextView) root.findViewById(R.id.tv_time_distance);
        StaticVariable.TV_TIME_DESTANCE.setVisibility(View.GONE);
        StaticVariable.TV_VIA = (TextView) root.findViewById(R.id.tv_via);
        StaticVariable.TV_VIA.setVisibility(View.GONE);
        StaticVariable.TV_ROUTING_DETAIL = (TextView) root.findViewById(R.id.tv_preview);
        StaticVariable.TV_ROUTING_DETAIL.setText(Html.fromHtml(" <font color=#5789ff>  >> "
                + mContext.getResources().getString(R.string.detail)
                + " </font> "));
        StaticVariable.TV_ROUTING_DETAIL.setTextSize(13);
        StaticVariable.TV_ROUTING_DETAIL.setVisibility(View.GONE);

        ll_routing_info = (LinearLayout) root.findViewById(R.id.ll_routing_info);
        ll_routing_info.setVisibility(View.GONE);

        ll_distance_time_via = (LinearLayout) root.findViewById(R.id.ll_distance_time_via);
        ll_search_routing_mode = (LinearLayout) root.findViewById(R.id.ll_search_routing_mode);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ll_distance_time_via.setOrientation(LinearLayout.HORIZONTAL);
            ll_search_routing_mode.setOrientation(LinearLayout.HORIZONTAL);
        } else {
            ll_distance_time_via.setOrientation(LinearLayout.VERTICAL);
            ll_search_routing_mode.setOrientation(LinearLayout.VERTICAL);
        }

        ll_info_bar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StaticVariable.TV_ROUTING_DETAIL.getVisibility() == View.VISIBLE) {
                    Log.wtf("Route preview", "preview");
                    final Dialog dialog = new Dialog(mContext);
                    dialog.setContentView(R.layout.dialog_search_result);
                    dialog.setTitle(mContext.getResources().getString(
                            R.string.route_info));
                    ListView lv_search_result = (ListView) dialog
                            .findViewById(R.id.lv_search_result);
                    ArrayList<String> streetNameList = RouteParser.mainStreetName;
                    if (streetNameList == null) {
                        streetNameList = new ArrayList<String>();
                        streetNameList.add(mContext.getResources().getString(
                                R.string.route_not_found));
                    }
                    final StreetListAdapter adapter = new StreetListAdapter(
                            mContext, R.layout.street_row, streetNameList,
                            RouteParser.mainStreetLength,
                            RouteParser.directionList);
                    lv_search_result.setAdapter(adapter);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                }
            }
        });

        // get transportation mode
        if (MainActivity.mSharedPreferences.contains(Constant.STR_TRANS_KEY)) {
            StaticVariable.INT_TRANSPORTATION_MODE = MainActivity.mSharedPreferences.getInt(
                    Constant.STR_TRANS_KEY, ModeHelper.CAR_MODE);
            setTransportationImage(StaticVariable.INT_TRANSPORTATION_MODE);
            Log.wtf("Transportation", StaticVariable.INT_TRANSPORTATION_MODE + " ");
        }

        // get routing mode
        if (MainActivity.mSharedPreferences.contains(Constant.STR_ROUTING_MODE_KEY)) {
            StaticVariable.INT_ROUTING_MODE = MainActivity.mSharedPreferences.getInt(
                    Constant.STR_ROUTING_MODE_KEY, ModeHelper.DISTANCE_MODE);
            setRoutingModeText(StaticVariable.INT_ROUTING_MODE);
            Log.wtf("Routing mode", StaticVariable.INT_ROUTING_MODE + "");
        }

//		// ### Floating Action Button GPS
//		fab_gps = (FloatingActionButton) root.findViewById(R.id.fab_gps);
//		gps_drawable = getResources().getDrawable(R.drawable.gps01);
//		gps_follow_drawable = getResources().getDrawable(R.drawable.gps02);
//		fab_gps.setDrawable(gps_drawable);
//
//		fab_gps.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (lastLocation != null) {
//					mMapController.animateTo(new GeoPoint(lastLocation.getLatitude(), lastLocation.getLongitude()));
//					updateUserLocationMarker(position_marker,
//							lastLocation.getLatitude(),
//							lastLocation.getLongitude());
//					if (StaticVariable.NAVIGATE) {
//						if (StaticVariable.FOLLOW) {
//							fab_gps.setDrawable(gps_drawable);
//							StaticVariable.FOLLOW = false;
//							mMapView.setLayoutParams(new LinearLayout.LayoutParams(
//											screen_width, screen_height));
//							if (mMapView.getRotation() != 0)
//								mMapView.setRotation(0);
//						} else {
//							fab_gps.setDrawable(gps_follow_drawable);
//							StaticVariable.FOLLOW = true;
//							mMapView.setLayoutParams(new LinearLayout.LayoutParams(cross_line, cross_line));
//							mMapView.invalidate();
//							if (StaticVariable.DES_SEG_ID_STATUS && StaticVariable.FINISH_ROUTING
//									&& StaticVariable.PATH_OVERLAY_EXIST) {
//								requestRouting(mContext, mMapView, false);
//							}
//						}
//					}
//				}
//			}
//		});


        // ###Set up location manager
        mLocationManager = (LocationManager) mContext
                .getSystemService(Context.LOCATION_SERVICE);
        // ################################################
        long currentTime = System.currentTimeMillis();
        long bestTime = 0;
        List<String> providers = mLocationManager.getAllProviders();
        for (String provider : providers) {
            Log.wtf("PROVIDER " + provider, "###");
            Location tmpLocation = mLocationManager
                    .getLastKnownLocation(provider);
            if (tmpLocation != null) {
                Log.wtf("PROVIDER " + provider,
                        "Accuracy: " + tmpLocation.getAccuracy() + "Time: "
                                + (tmpLocation.getTime() - currentTime));
                if (tmpLocation.getTime() > bestTime) {
                    bestTime = tmpLocation.getTime();
                    lastLocation = tmpLocation;
                }
            }
        }
        // ################################################
        if (lastLocation != null) {
            mMapController.animateTo(new GeoPoint(lastLocation.getLatitude(), lastLocation.getLongitude()));
            userLocDynamicOverlay = new DynamicOverlay(mContext, lon, lat, mMapView, true);
            updateUserLocationMarker(position_marker, lastLocation.getLatitude(), lastLocation.getLongitude());
        }

        // ### Check if GPS is enabled
        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(mContext, R.string.gps_dialog_title, Toast.LENGTH_SHORT).show();
            final Dialog dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.gps_dialog);
            dialog.setTitle(R.string.gps_dialog_title);
            Button bt_cancel = (Button) dialog.findViewById(R.id.bt_gps_cancel);
            bt_cancel.setText(R.string.cancel);
            bt_cancel.setBackgroundColor(Color.TRANSPARENT);
            bt_cancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            Button bt_ok = (Button) dialog.findViewById(R.id.bt_gps_ok);
            bt_ok.setText(R.string.ok);
            bt_ok.setBackgroundColor(Color.TRANSPARENT);
            bt_ok.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent_settings = new Intent(
                            Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent_settings);
                }
            });
            dialog.show();
        }

        currentMapCenterPoint = mMapView.getMapCenter();

        // ### setup image button route
        StaticVariable.IV_ROUTE = (ImageView) root.findViewById(R.id.iv_route);
        StaticVariable.IV_ROUTE.setImageResource(R.drawable.routing_grey);
        StaticVariable.IV_ROUTE.setOnClickListener(new OnClickListener() {
            // ### REQUEST ROUTING
            @Override
            public void onClick(View v) {
                if (StaticVariable.DES_SEG_ID_STATUS
                        && StaticVariable.FINISH_ROUTING) {
                    LogFile.writeToFile(mDeviceId + " #2 ROUTING BUTTON");
                    mLocationSender = new LocationSender(mContext);
//					mLocationSender.execute(" #2 ROUTING BUTTON ");
//					requestRouting(getActivity().getWindow().getContext(), mMapView, false);
                    requestRouting(mContext, mMapView, false);

                }
                StaticVariable.START_NEW_PATH = true;
                start_marker_exist = false;
            }
        });

        // ### setup traffic information layer
        trafficInfoParser = new TrafficInfoParser(mContext, mMapView);
        trafficHandler = new Handler();
        trafficRunnable = new Runnable() {
            @Override
            public void run() {
                requestTrafficInfo();
                trafficHandler.postDelayed(this,
                        Constant.TRAFFIC_INFO_UPDATE_INTERVAL);
            }
        };

        warningIfoParser = new WarningInfoParser(mContext, mMapView);
        warningHandler = new Handler();
        warningRunnable = new Runnable() {
            @Override
            public void run() {
                // TODO implement request warning runnable
                requestWarningInfor();
                warningHandler.postDelayed(this, Constant.TRAFFIC_INFO_UPDATE_INTERVAL);
            }
        };

        mMapView.setMapListener(new MapListener() {
            // ### on map scroll
            @Override
            public boolean onScroll(ScrollEvent arg0) {
                if (StaticVariable.SHOW_TRAFFIC_INFO) {

                    NodeDrawable current = new NodeDrawable(0,
                            currentMapCenterPoint.getLongitude(),
                            currentMapCenterPoint.getLatitude());
                    NodeDrawable newCenter = new NodeDrawable(0, mMapView
                            .getMapCenter().getLongitude(), mMapView
                            .getMapCenter().getLatitude());
                    if (NodeDrawable.getDistance(current, newCenter) > radius / 2) {
                        trafficHandler.removeCallbacks(trafficRunnable);
                        if (TrafficInfoParser.finish_get_traffic_info)
                            trafficHandler.post(trafficRunnable);
                        else
                            trafficHandler.postDelayed(trafficRunnable, Constant.TIME_TRAFFIC_INFO_UPDATE);
                    }

                }

                if (StaticVariable.SHOW_WARNING_INFO) {
                    NodeDrawable current = new NodeDrawable(0,
                            currentMapCenterPoint.getLongitude(),
                            currentMapCenterPoint.getLatitude());
                    NodeDrawable newCenter = new NodeDrawable(0, mMapView
                            .getMapCenter().getLongitude(), mMapView
                            .getMapCenter().getLatitude());
                    if (NodeDrawable.getDistance(current, newCenter) > radius / 2) {
                        warningHandler.removeCallbacks(warningRunnable);
                        if (WarningInfoParser.finish_get_warning_info)
                            trafficHandler.post(warningRunnable);
                        else
                            trafficHandler.postDelayed(warningRunnable, Constant.TIME_WARNING_INFO_UPDATE);
                    }

                }
                return false;
            }
            // ### on zoom level change
            @Override
            public boolean onZoom(ZoomEvent arg0) {
                Constant.ZOOM_LEVEL = mMapView.getZoomLevel();
                // if zoom level is too small, don't show traffic info
                if (Constant.ZOOM_LEVEL <= 13) {
                    mOverlays.removeAll(TrafficInfoParser.listPath);
                    mOverlays.removeAll(WarningInfoParser.listWarning); // 20150204 add warning
                } else {
                    if (Constant.ZOOM_LEVEL >= 16)
                        radius = 1500;
                    else
                        radius = 500 * (16 - Constant.ZOOM_LEVEL) * 5;
                }
                if (StaticVariable.SHOW_TRAFFIC_INFO) {
                    trafficHandler.removeCallbacks(trafficRunnable);
                    if (TrafficInfoParser.finish_get_traffic_info)
                        trafficHandler.post(trafficRunnable);
                    else
                        trafficHandler.postDelayed(trafficRunnable, Constant.TIME_TRAFFIC_INFO_UPDATE);
                }
                //@2015 02 04
                // include show warning information
                if (StaticVariable.SHOW_WARNING_INFO) {
                    warningHandler.removeCallbacks(warningRunnable);
                    if (WarningInfoParser.finish_get_warning_info) {
                        warningHandler.post(warningRunnable);
                    } else {
                        warningHandler.postDelayed(warningRunnable, Constant.TIME_WARNING_INFO_UPDATE);
                    }
                }
                return false;
            }
        });


//		mRouteParserController =  new RouteParser(getActivity());

        // FIXME: init Follow Trip
        // set mainactivity: for fragment
        this.mainActivity = (MainActivity) this.getActivity();

        mContext_FLTrip = mainActivity.getApplicationContext();
//		mDeviceId = Secure.getString(mContext.getContentResolver(),
//				Secure.ANDROID_ID);

        // Set up log file
//		mLocationSender = new LocationSender(mContext);
//		mLogFile = new LogFile(mContext);

//		// Text-to-Speech
//		mTextToSpeech = new TextToSpeech(mContext, new MapOnInitListener());

        // road overlays
        waypoints = new ArrayList<GeoPoint>();
        roadManager = new OSRMRoadManager();

        lst_markers = new ArrayList<MyMarker>();
        lst_around_markers = new ArrayList<MyMarker>();

        // ### init LoadMap View
//		mMapView = (MapView) root.findViewById(R.id.openmapview);
//		mMapView.setTileSource(TileSourceFactory.MAPQUESTOSM);
//		mMapView.setMultiTouchControls(true);
//		mMapView.setBuiltInZoomControls(false);

        // add controller
//		mMapController = (MapController) mMapView.getController();
//		mMapController.setZoom(Constant.ZOOM_LEVEL);
//		mMapController.animateTo(Constant.GEO_HCM_UNIV);

        // add overly listener
        // mMapOverlyListener = new MapOverlayListener(getActivity()); // Add
        // Map overlay listener to catch touch event on map

//		mMapOverlyListener = new MapOverlayListener(mainActivity); // Add Map
//																	// overlay
//																	// listener
//																	// to catch
//																	// touch
//																	// event on
//																	// map
//
//		mOverlays = mMapView.getOverlays();
//		mOverlays.add(mMapOverlyListener);

        mDrawablePosition = getResources().getDrawable(R.drawable.ic_position);

        // Map location listener
        resourceProxy = new DefaultResourceProxyImpl(
                mainActivity.getApplicationContext());
        // mItemizedOverlay = new OsmMapsItemizedOverlay(
        // new ArrayList<OverlayItem>(), myOnItemGestureListener,
        // mResourceProxy);

        // GeoPoint(10.809807,106.645101));

		/* location manager */
//		mLocationManager = (LocationManager) mainActivity
//				.getSystemService(Context.LOCATION_SERVICE);

        // button capture image and set marker at current postion
//		btn_capture = (Button) rootView.findViewById(R.id.btn_capture);
//		btn_capture.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				takeFromCamera();
//			}
//		});

        btn_start = (Button) root.findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //check user login FB
                if (!LoginManager.getInstance().isLogin()) {
                    new AlertDialog.Builder(mainActivity)
                            .setTitle(mainActivity.getString(R.string.login_first_title))
                            .setMessage(mainActivity.getString(R.string.login_first_prompt))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    return;
                }

                // set button text when click
                // TODO: KenK11 refactor this compare
                if (btn_start.getText().equals(mainActivity.getString(R.string.btn_start))) {
                    btn_start.setText(mainActivity.getString(R.string.btn_end));
                } else {
                    btn_start.setText(mainActivity.getString(R.string.btn_start));
                }


                if (!isStart) {
                    // create new trip
                    isStart = true;
                    lstUserPassRoad = new ArrayList<Road>();
                    viaPoints = new ArrayList<GeoPoint>();
                    MainActivity.fab_btn_capture.setVisibility(View.VISIBLE);
                    lstPassedPoint = new ArrayList<Location>();
                    Calendar c = Calendar.getInstance();
                    currentTime_start_trip = c.getTime();
                    // FIXME: =====================
                    // updateUITrackingButton();
                    // GeoPoint location = myLocationOverlay.getLocation();
                    // mMapView.getController().animateTo(location);

                    mContext_FLTrip.startService(new Intent(mContext,
                            DetectLocationService.class));
                    mContext_FLTrip.registerReceiver(
                            mReceiverLocationChange,
                            new IntentFilter(
                                    StaticStrings.IntentFilter_ACTION_LOCATION_CHANGE));
                } else {
                    isStart = false;
                    MainActivity.fab_btn_capture.setVisibility(View.INVISIBLE);
                    MainActivity.fab_camera.setVisibility(View.INVISIBLE);
                    MainActivity.fab_video.setVisibility(View.INVISIBLE);
                    // get prompts.xml view
                    LayoutInflater li = LayoutInflater.from(mainActivity);
                    View promptsView = li.inflate(
                            R.layout.input_trip_info_dialog, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            mainActivity);

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);

                    final EditText txt_startPlace = (EditText) promptsView
                            .findViewById(R.id.txtStartPlace);
                    final EditText txt_endPlace = (EditText) promptsView
                            .findViewById(R.id.txtEndPlace);
                    final Spinner spinner_trip_privacy = (Spinner) promptsView.findViewById(R.id.spinner_trip_privacy);
                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            // FIXME: save trip and send to server
                                            final Trip trip1 = new Trip();
                                            trip1.setUserName(LoginManager.getInstance().getUser().getName());
                                            trip1.setAvaUserCreateTrip(R.drawable.ic_user_profile);
                                            trip1.setNumberCommentTrip("0");
                                            trip1.setNumberLikeTrip("0");

                                            Calendar c = Calendar.getInstance();
                                            Date currentTime = c.getTime();

                                            StringBuffer sb = new StringBuffer();

                                            // TODO : KenK11 implement function add date time
                                            long diffInSeconds = (currentTime.getTime() - currentTime_start_trip.getTime()) / 1000;

                                            long sec = (diffInSeconds >= 60 ? diffInSeconds % 60 : diffInSeconds);
                                            long min = (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60 : diffInSeconds;
                                            long hrs = (diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24 : diffInSeconds;
                                            long days = (diffInSeconds = (diffInSeconds / 24)) >= 30 ? diffInSeconds % 30 : diffInSeconds;
                                            long months = (diffInSeconds = (diffInSeconds / 30)) >= 12 ? diffInSeconds % 12 : diffInSeconds;
                                            long years = (diffInSeconds = (diffInSeconds / 12));

                                            if (years > 0) {
                                                if (years == 1) {
                                                    sb.append("a year");
                                                } else {
                                                    sb.append(years + " years");
                                                }
                                                if (years <= 6 && months > 0) {
                                                    if (months == 1) {
                                                        sb.append(" and a month");
                                                    } else {
                                                        sb.append(" and " + months + " months");
                                                    }
                                                }
                                            } else if (months > 0) {
                                                if (months == 1) {
                                                    sb.append("a month");
                                                } else {
                                                    sb.append(months + " months");
                                                }
                                                if (months <= 6 && days > 0) {
                                                    if (days == 1) {
                                                        sb.append(" and a day");
                                                    } else {
                                                        sb.append(" and " + days + " days");
                                                    }
                                                }
                                            } else if (days > 0) {
                                                if (days == 1) {
                                                    sb.append("a day");
                                                } else {
                                                    sb.append(days + " days");
                                                }
                                                if (days <= 3 && hrs > 0) {
                                                    if (hrs == 1) {
                                                        sb.append(" and an hour");
                                                    } else {
                                                        sb.append(" and " + hrs + " hours");
                                                    }
                                                }
                                            } else if (hrs > 0) {
                                                if (hrs == 1) {
                                                    sb.append("an hour");
                                                } else {
                                                    sb.append(hrs + " hours");
                                                }
                                                if (min > 1) {
                                                    sb.append(" and " + min + " minutes");
                                                }
                                            } else if (min > 0) {
                                                if (min == 1) {
                                                    sb.append("a minute");
                                                } else {
                                                    sb.append(min + " minutes");
                                                }
                                                if (sec > 1) {
                                                    sb.append(" and " + sec + " seconds");
                                                }
                                            } else {
                                                if (sec <= 1) {
                                                    sb.append("about a second");
                                                } else {
                                                    sb.append("about " + sec + " seconds");
                                                }
                                            }
                                            trip1.setDateTime(sb.toString());

                                            String intMonth = (String) android.text.format.DateFormat
                                                    .format("MM", currentTime); // 06
                                            String year = (String) android.text.format.DateFormat
                                                    .format("yyyy", currentTime); // 2013
                                            String day = (String) android.text.format.DateFormat
                                                    .format("dd", currentTime); // 20

                                            trip1.setTimeStartTrip(day + "/"
                                                    + intMonth + "/" + year);
                                            trip1.setTimeEndTrip(day + "/"
                                                    + intMonth + "/" + year);
                                            trip1.setPlaceStartTrip(txt_startPlace.getText().toString());
                                            trip1.setPlaceEndTrip(txt_endPlace.getText().toString());
                                            trip1.setNumberLikeTrip(mainActivity.getString(R.string.default_like));
                                            trip1.setNumberCommentTrip(mainActivity.getString(R.string.default_comment));
                                            trip1.setPrivacy(spinner_trip_privacy.getSelectedItem().toString());
                                            //send trip to server
                                            HttpManager.createTrip(trip1, getActivity(), new ICallback<JSONObject>() {
                                                @Override
                                                public void onCompleted(JSONObject jsonobject, Object tag, Exception ex) {
                                                    if (ex != null || jsonobject == null){
                                                        Log.e(TAG,"Error when create trip",ex);
                                                    }
                                                    //save list passed point==================================
                                                    if (!jsonobject.isNull("tripId")) {
                                                        try {
                                                            trip1.setTripId(jsonobject.getString("tripId"));
                                                            if (Utilities.hasConnection(mContext)) {
                                                                for (int j = 0; j < viaPoints
                                                                        .size(); j++) {
                                                                    GeoPoint geoPoint = viaPoints
                                                                            .get(j);
                                                                    PointItem pointItem = new PointItem();
                                                                    pointItem
                                                                            .setX_Lat(geoPoint
                                                                                    .getLatitudeE6());
                                                                    pointItem
                                                                            .setY_Long(geoPoint
                                                                                    .getLongitudeE6());
                                                                    // FIXME: set trip
                                                                    // id
                                                                    final int finalJ = j;
                                                                    HttpManager
                                                                            .createPointOnTrip(
                                                                                    trip1.getTripId(),
                                                                                    pointItem, getActivity(), new ICallback<JSONObject>() {
                                                                                        @Override
                                                                                        public void onCompleted(JSONObject data, Object tag, Exception e) {
                                                                                            if (e != null || data == null){
                                                                                                Log.e(TAG,"Error when create trip",e);
                                                                                            }
                                                                                            if (!data.isNull("pointId")) {
                                                                                                try{
                                                                                                    String pointId = data.getString("pointId");
                                                                                                    for(int k=0; k <lst_markers.size(); k++){
                                                                                                        if(lst_markers.get(k).pointIndex > finalJ) break;
                                                                                                        if(lst_markers.get(k).pointIndex < finalJ) continue;
                                                                                                        String data1 = lst_markers.get(k).getData();
                                                                                                        HttpManager.uploadImage(new File(data1), data1, CameraHelper.getMimeType(data1), pointId);
                                                                                                    }
                                                                                                }
                                                                                                catch(JSONException ex){
                                                                                                    ex.printStackTrace();
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    });
                                                                }

                                                            }
                                                        } catch (JSONException e) {
                                                            // TODO Auto-generated catch block
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                            });

//											//===================================

                                            // stop sevice trip
                                            mContext_FLTrip.stopService(new Intent(
                                                    mContext,
                                                    DetectLocationService.class));
                                            mContext_FLTrip.unregisterReceiver(mReceiverLocationChange);
                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();


                }
            }
        });

        myLocationOverlay = new DirectedLocationOverlay(mainActivity);
        mMapView.getOverlays().add(myLocationOverlay);

        mDeparture = null;
        mDestination = null;
        viaPoints = new ArrayList<GeoPoint>();

        // ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(this);
        // map.getOverlays().add(scaleBarOverlay);

        // Itinerary markers:
        mItineraryMarkers = new FolderOverlay(mainActivity);
        mItineraryMarkers.setName(mainActivity.getString(R.string.itinerary_maker));
        mMapView.getOverlays().add(mItineraryMarkers);
        // mViaPointInfoWindow = new
        // ViaPointInfoWindow(R.layout.itinerary_bubble, mMapView){
        // @Override
        // public void onClickRemovePoint(int p) {
        // // removePoint(p);
        // }
        // };

        mRoadNodeMarkers = new FolderOverlay(mainActivity);
        mRoadNodeMarkers.setName(mainActivity.getString(R.string.route_step));
        mMapView.getOverlays().add(mRoadNodeMarkers);

        mPoiMarkers = new RadiusMarkerClusterer(mainActivity);
        // mKmlDocument = new KmlDocument();

        // Button btnStart = (Button)findViewById(R.id.btnStart);
        // registerForContextMenu(btnStart);

        // ### Floating Action Button Capture
        MainActivity.fab_btn_capture = (FloatingActionButton) mainActivity.findViewById(R.id.fab_btn_capture);
        MainActivity.fab_camera = (FloatingActionButton) mainActivity.findViewById(R.id.fab_camera);
        MainActivity.fab_video = (FloatingActionButton) mainActivity.findViewById(R.id.fab_video);

        Move_Duoi = AnimationUtils.loadAnimation(getActivity(), R.anim.move_duoi);
        Move_Tren = AnimationUtils.loadAnimation(getActivity(), R.anim.move_tren);
        Back_Duoi = AnimationUtils.loadAnimation(getActivity(), R.anim.back_duoi);
        Back_Tren = AnimationUtils.loadAnimation(getActivity(), R.anim.back_tren);

        MainActivity.fab_btn_capture.setVisibility(View.INVISIBLE);
        MainActivity.fab_btn_capture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(move_back == false) Move();
                else Back();
                move_back = !move_back;
            }
        });
        MainActivity.fab_camera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showCamera();
            }
        });
        MainActivity.fab_video.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showCameraVideo();
            }
        });
        isCapturing = false;
    }

    // @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    /**
     * perform search
     */
    public void performSearch() {
        String url = et_search.getText().toString() + Constant.HCMC;
        try {
            url = URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        url = url.replace(" ", "+");
        url = Constant.SEARCH_LOCATION_URL + url
                + Constant.SEARCH_LOCATION_AREA_URL;

        SearchParser searchParser = new SearchParser(getActivity(), mMapView);
        searchParser.execute(url);
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);

        et_search.clearFocus();
    }

    private void putRoadNodes(Road road) {
        mRoadNodeMarkers.getItems().clear();
        Drawable icon = getResources().getDrawable(R.drawable.marker_node);
        int n = road.mNodes.size();
        MarkerInfoWindow infoWindow = new MarkerInfoWindow(
                R.layout.bonuspack_bubble, mMapView);
        TypedArray iconIds = getResources().obtainTypedArray(
                R.array.direction_icons);
        for (int i = 0; i < n; i++) {
            RoadNode node = road.mNodes.get(i);
            String instructions = (node.mInstructions == null ? ""
                    : node.mInstructions);
            Marker nodeMarker = new Marker(mMapView);
            //TODO: KenK11
            nodeMarker.setTitle("Step " + " " + (i + 1));
            nodeMarker.setSnippet(instructions);
            nodeMarker.setSubDescription(Road.getLengthDurationText(
                    node.mLength, node.mDuration));
            nodeMarker.setPosition(node.mLocation);
            nodeMarker.setIcon(icon);
            nodeMarker.setInfoWindow(infoWindow); // use a shared infowindow.
            int iconId = iconIds.getResourceId(node.mManeuverType,
                    R.drawable.ic_empty);
            if (iconId != R.drawable.ic_empty) {
                Drawable image = getResources().getDrawable(iconId);
                nodeMarker.setImage(image);
            }
            mRoadNodeMarkers.add(nodeMarker);
        }
        iconIds.recycle();
    }

    /**
     * @param removeStart
     */
    private void removePreviousMakingPoints(boolean removeStart) {
        int count = multipleOverlay.size();
        for (int i = 0; i < count; i++) {
            mOverlays.remove(multipleOverlay.get(i));
        }
        multipleOverlay.clear();
        StaticVariable.START_NEW_PATH = false;
        if (!StaticVariable.NAVIGATE && removeStart) {
            mOverlays.remove(startingPointOverlay);
        }
        StaticVariable.LIST_MULTIPLE_POINT.clear();
        mOverlays.remove(destinationOverlay);
    }

    public void removeUpdates() {
        mLocationManager.removeUpdates(locationListener);
    }

    /**
     * request traffic information
     */
    public void requestTrafficInfo() {
        trafficInfoParser.cancel(true);
        if (mMapView.getZoomLevel() > 13) {
            currentMapCenterPoint = mMapView.getMapCenter();

            double r = (double) radius / 110000;
            Log.wtf("RADIUS", r + "  " + radius);
            double top_lat = currentMapCenterPoint.getLatitude() + 1.25 * r;
            double left_lon = currentMapCenterPoint.getLongitude() - r;
            double bottom_lat = currentMapCenterPoint.getLatitude() - 1.25 * r;
            double right_lon = currentMapCenterPoint.getLongitude() + 1.5 * r;
            String url = Constant.CURRENT_TRAFFIC_URL
                    + "latTL=" + top_lat
                    + "&lonTL=" + left_lon
                    + "&latTR=" + top_lat
                    + "&lonTR=" + right_lon
                    + "&latBL=" + bottom_lat
                    + "&lonBL=" + left_lon
                    + "&latBR=" + bottom_lat
                    + "&lonBR=" + right_lon
                    + "&zoom=" + (mMapView.getZoomLevel() - 1);
            trafficInfoParser = new TrafficInfoParser(mContext, mMapView);
            trafficInfoParser.execute(url);
        }
    }

    private void Move(){
        FrameLayout.LayoutParams paramsTrai = (FrameLayout.LayoutParams) MainActivity.fab_video.getLayoutParams();
        paramsTrai.topMargin = (int)(MainActivity.fab_video.getWidth() * 1.5);
        MainActivity.fab_video.setLayoutParams(paramsTrai);
        MainActivity.fab_video.startAnimation(Move_Duoi);

        FrameLayout.LayoutParams paramsTren = (FrameLayout.LayoutParams) MainActivity.fab_camera.getLayoutParams();
        paramsTren.bottomMargin = (int)(MainActivity.fab_camera.getWidth() * 1.5);
        MainActivity.fab_camera.setLayoutParams(paramsTren);
        MainActivity.fab_camera.startAnimation(Move_Tren);
    }
    private void Back(){
        FrameLayout.LayoutParams paramsTrai = (FrameLayout.LayoutParams) MainActivity.fab_video.getLayoutParams();
        paramsTrai.topMargin -= (int)(MainActivity.fab_video.getWidth() * 1.5);
        MainActivity.fab_video.setLayoutParams(paramsTrai);
        MainActivity.fab_video.startAnimation(Back_Duoi);

        FrameLayout.LayoutParams paramsTren = (FrameLayout.LayoutParams) MainActivity.fab_camera.getLayoutParams();
        paramsTren.bottomMargin -= (int)(MainActivity.fab_camera.getWidth() * 1.5);
        MainActivity.fab_camera.setLayoutParams(paramsTren);
        MainActivity.fab_camera.startAnimation(Back_Tren);
    }

    /**
     * request warning information
     */
    private void requestWarningInfor() {
        //TODO: implement
        warningIfoParser = new WarningInfoParser(mContext, mMapView);
        String url = Constant.ROOT + "hcm/rest/policeWarning";
        warningIfoParser.execute(url);
    }

    //set marker for image capture
    public void setMarker(GeoPoint geoPoint, Bitmap bitmap) {

        // //0. Using the Marker overlay
        final MyMarker startMarker = new MyMarker(mMapView);
        startMarker.getMarker().setPosition(geoPoint);
        startMarker.getMarker().setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        String str_lat_long = "Lattitude: " + geoPoint.getLatitude()
                + "\r\nLongtitude: " + geoPoint.getLongitude();
        startMarker.getMarker().setTitle(str_lat_long);

        ViaPointInfoWindow viaPOIInfoWindow = new ViaPointInfoWindow(
                R.layout.itinerary_bubble, mMapView, bitmap, mainActivity) {

            @Override
            public void onClickRemovePoint(int p) {

            }
        };
        startMarker.getMarker().setInfoWindow(viaPOIInfoWindow);
        startMarker.getMarker().setDraggable(true);
        // startMarker.setOnMarkerDragListener(new
        // OnMarkerDragListenerDrawer());
        startMarker.getMarker()
                .setOnMarkerClickListener(new Marker.OnMarkerClickListener() {

                    @Override
                    public boolean onMarkerClick(Marker arg0, MapView arg1) {

                        GeoPoint curr_marker_pos = arg0.getPosition();
                        lst_around_markers = new ArrayList<MyMarker>();
                        for (int i = 0; i < lst_markers.size(); i++) {
                            MyMarker marker = lst_markers.get(i);

                            double distance = Utilities.distanceInKm(
                                    curr_marker_pos, marker.getMarker().getPosition());
                            if (Math.abs(distance) <= 2) {
                                lst_around_markers.add(marker);
                            }
                        }

                        if (lst_around_markers.size() > 1
                                && !isShowDialogMarker) {
                            isShowDialogMarker = true;
                            showChoiceDialogMarker(lst_around_markers);
                            // return false;
                        } else {
                            lst_markers.get(startMarker.getIndex()).getMarker()
                                    .showInfoWindow();
                        }
                        return false;
                    }
                });
        startMarker.setIndex(lst_markers.size());
        startMarker.setPointIndex(viaPoints.size());
        startMarker.setData(fileUri.getPath());
        lst_markers.add(startMarker);
        mMapView.getOverlays().add(startMarker.getMarker());
        mMapView.invalidate();
    }

    //set marker for image capture
    public void setMarkerForTrip(Trip trip) {
        for(int i = 0 ; i < listMarkerTrip.size(); i++){
            //start marker
            String str_lat_long = "Lattitude: " + listMarkerTrip.get(i).getMarker().getPosition().getLatitude()
                    + "\r\nLongtitude: " + listMarkerTrip.get(i).getMarker().getPosition().getLongitude();

            if(i==0 || (i== listMarkerTrip.size() -1)){
                str_lat_long = "Start Place: " + trip.getPlaceStartTrip()
                        + "\r\nStart Time: " + trip.getTimeStartTrip()
                        + "\r\nDuration: " + trip.getDateOpenTrip()
                        + "\r\nDistance: " + Utilities.distanceInKm(trip.getLstWayPoints()) + " km";
            }

            listMarkerTrip.get(i).getMarker().setTitle(str_lat_long);

            ViaPointInfoWindow viaPOIInfoWindow = new ViaPointInfoWindow(
                    R.layout.itinerary_bubble, mMapView, null, mainActivity) {

                @Override
                public void onClickRemovePoint(int p) {

                }
            };
            listMarkerTrip.get(i).getMarker().setInfoWindow(viaPOIInfoWindow);
            listMarkerTrip.get(i).getMarker().setDraggable(false);
            final int finalI = i;
            listMarkerTrip.get(i).getMarker()
                    .setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker arg0, MapView arg1) {
                            GeoPoint curr_marker_pos = arg0.getPosition();
                            lst_around_markers = new ArrayList<MyMarker>();
                            for (int j = 0; j < listMarkerTrip.size(); j++) {
                                MyMarker marker = listMarkerTrip.get(j);

                                double distance = Utilities.distanceInKm(
                                        curr_marker_pos, marker.getMarker().getPosition());
                                if (Math.abs(distance) <= 2) {
                                    lst_around_markers.add(marker);
                                }
                            }

                            if (lst_around_markers.size() > 1
                                    && !isShowDialogMarker) {
                                isShowDialogMarker = true;
                                showChoiceDialogMarker(lst_around_markers);
                                // return false;
                            } else {
                                listMarkerTrip.get(finalI).getMarker()
                                        .showInfoWindow();
                            }
                            return false;
                        }
                    });
            mMapView.getOverlays().add(listMarkerTrip.get(i).getMarker());
        }


        mMapView.invalidate();
    }

    // ### set text of routing mode text view
    public void setRoutingModeText(int routingMode) {
        switch (routingMode) {
            case ModeHelper.DISTANCE_MODE:
                tv_routing_mode.setText(R.string.shortest);
                break;
            case ModeHelper.REAL_TIME_MODE:
                tv_routing_mode.setText(R.string.static_shortest);
                break;
            case ModeHelper.MULTI_POINT_PATH:
                tv_routing_mode.setText(R.string.multi_point_path);
                break;
            case ModeHelper.PREFERENT_REAL_TIME:
                tv_routing_mode.setText(R.string.profile_shortest);
                break;

        }
    }

    // ### set image of transportation image view
    public void setTransportationImage(int transportation) {
        switch (transportation) {
            case ModeHelper.CAR_MODE:
                iv_transportation.setImageResource(R.drawable.ic_car_black);
                break;
            case ModeHelper.BIKE_MODE:
                iv_transportation.setImageResource(R.drawable.ic_motorbike_black);
                break;
            case ModeHelper.BUS_MODE:
                iv_transportation.setImageResource(R.drawable.ic_bus_black);
                break;
            case ModeHelper.TRUCK_MODE:
                iv_transportation.setImageResource(R.drawable.ic_truck_black);
                break;
            default:
                iv_transportation.setImageResource(R.drawable.ic_motorbike_black);
                break;
        }
    }

    private void showChoiceDialogMarker(final List<MyMarker> lstMarker) {
        // lay danh sach Long Lat cua marker xung quanh de hien thi
        ArrayList<String> lstString = new ArrayList<String>();
        for (int i = 0; i < lstMarker.size(); i++) {
            lstString.add(String.valueOf("Lat: "
                    + lstMarker.get(i).getMarker().getPosition().getLatitude())
                    + " Long:"
                    + String.valueOf(lstMarker.get(i).getMarker().getPosition()
                    .getLongitude()));
        }
        selectMarkerChoice = 0;
        CharSequence[] charSeq = lstString.toArray(new CharSequence[lstString
                .size()]);
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Markers")
                .setSingleChoiceItems(charSeq, selectMarkerChoice,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                selectMarkerChoice = which;
                            }
                        })
                .setPositiveButton(R.string.btn_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                isShowDialogMarker = false;
                                lstMarker.get(selectMarkerChoice).getMarker()
                                        .showInfoWindow();
                                Intent intent = new Intent(getActivity(), VideoPlayer.class);
                                String url = lstMarker.get(selectMarkerChoice).getData();
                                intent.putExtra("URL", url);
                                startActivity(intent);
                                    /* User clicked Yes so do some stuff */
                            }
                        })
                .setNegativeButton(R.string.alert_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                isShowDialogMarker = false;
                                    /* User clicked No so do some stuff */
                            }
                        });
        dialog.show();
    }

    private void showContextMenu() {
        // createContextMenu();
        StaticVariable.CONTEXT_MENU_DIAGLOG.show();
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint arg0) {
        return false;
    }

    public void showCameraVideo() {
        File storageDir = new File(Environment
                .getExternalStorageDirectory() + "/filetoupload/");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            fileUri = new File(storageDir, timeStamp + "_video.mp4");
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(fileUri));
            startActivityForResult(intent, REQUEST_TAKE_VIDEO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent
                .resolveActivity(getActivity().getPackageManager()) != null) {
            //KenK11 create folder for saving new image
            File exportDir = new File(
                    Environment.getExternalStorageDirectory(), FOLDER_NAME);
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            } else {
                exportDir.delete();
            }
            fileUri = new File(exportDir, "/"
                    + UUID.randomUUID().toString().replaceAll("-", "") + ".jpg");
            // Log.d(LOG_TAG, "/" + UUID.randomUUID().toString().replaceAll("-",
            // "") + ".jpg");
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(fileUri));
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }
    /**
     * update current location
     */
    private void updateCurrentLocation() {
        mLastLocation = mLocationManager
                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (null != mLastLocation) {
            GeoPoint locGeoPoint = new GeoPoint(mLastLocation.getLatitude(),
                    mLastLocation.getLongitude());
            mMapController.setCenter(locGeoPoint);

            mMapController.setZoom(15);
            mMapView.invalidate();
        }
    }

    /**
     * Update (or create if null) a marker in itineraryMarkers.
     */
    public Marker updateItineraryMarker(Marker marker, GeoPoint p, int index, int titleResId, int markerResId, int imageResId, String address) {
        Drawable icon = getResources().getDrawable(markerResId);
        String title = getResources().getString(titleResId);
        if (marker == null) {
            marker = new Marker(mMapView);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            // marker.setInfoWindow(mViaPointInfoWindow);
            marker.setDraggable(true);
            marker.setOnMarkerDragListener(mItineraryListener);
            mItineraryMarkers.add(marker);
        }
        marker.setTitle(title);
        marker.setPosition(p);
        marker.setIcon(icon);
        if (imageResId != -1)
            marker.setImage(getResources().getDrawable(imageResId));
        marker.setRelatedObject(index);
        mMapView.invalidate();
        if (address != null)
            marker.setSnippet(address);
        else
            // Start geocoding task to get the address and update the Marker
            // description:
            new GeocodingTask().execute(marker);
        return marker;
    }

    // ### UPDATE STARTING POINT DESTINATION MARKER
    // mode true: starting point, false: destination
    public void updateMarker(Drawable marker, double lat, double lon, int mode) {
        ResourceProxy mResourceProxy = new DefaultResourceProxyImpl(
                getActivity().getApplicationContext());
        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        GeoPoint point = new GeoPoint(lat, lon);
        items.add(new OverlayItem("Here", "My location", point));
        ItemizedOverlay<OverlayItem> tmpOverlay = new ItemizedIconOverlay<OverlayItem>(
                items, marker,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemLongPress(int arg0, OverlayItem item) {
                        return false;
                    }

                    @Override
                    public boolean onItemSingleTapUp(final int index,
                                                     final OverlayItem item) {
                        return true;
                    }
                }, mResourceProxy);
        if (SearchParser.searchLoc != null)
            mOverlays.remove(SearchParser.searchLoc);

        switch (mode) {
            case Constant.STARTING_POINT_MAKER: // starting point maker
                mOverlays.remove(startingPointOverlay);

                removePreviousMakingPoints(true);

                startingPointOverlay = tmpOverlay;
                mOverlays.add(startingPointOverlay);

                break;
            case Constant.DESTINATION_POINT_MAKER: // destination point maker
                // multiple point path mode
                if (StaticVariable.MULTIPLE_POINT) {
                    multipleOverlay.add(tmpOverlay);
                    mOverlays.add(tmpOverlay);
                } else {
                    // remove distination overlay in the map
                    mOverlays.remove(destinationOverlay);
                    destinationOverlay = tmpOverlay;
                    mOverlays.add(destinationOverlay);
                }

                ll_routing_info.setVisibility(View.VISIBLE);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                    ll_routing_info.setAnimation(slide_right);
                else
                    ll_routing_info.setAnimation(slide_down);

                ll_routing_info.getAnimation().start();

                break;
            case Constant.NORMAL_POINT_MAKER:

                break;

        }

        mMapView.invalidate();
    }


    ///==============

    public void updatePosition(GeoPoint aPoint) {
        waypoints.add(aPoint);

        if (waypoints.size() > 1) {
            ArrayList<GeoPoint> lst2LastPoint = new ArrayList<GeoPoint>();
            lst2LastPoint.add(waypoints.get(waypoints.size() - 1));
            lst2LastPoint.add(waypoints.get(waypoints.size() - 2));
            Road road = roadManager.getRoad(lst2LastPoint);

            if (road.mStatus != Road.STATUS_OK) {
                Toast.makeText(mainActivity.getApplicationContext(),
                        mainActivity.getString(R.string.error_loading_road_status) + road.mStatus,
                        Toast.LENGTH_SHORT).show();
            }
//				roadOverlay.setColor(Color.parseColor("#00FF0000"));
            roadOverlay = RoadManager.buildRoadOverlay(road,
                    this.mainActivity.getApplicationContext());
            mMapView.getOverlays().add(roadOverlay);

            mMapView.invalidate();
        }
    }

    //update POI
    void updateUIWithPOI(ArrayList<POI> pois, String featureTag) {
        if (pois != null) {
            POIInfoWindow poiInfoWindow = new POIInfoWindow(mMapView);
            for (POI poi : pois) {
                Marker poiMarker = new Marker(mMapView);
                poiMarker.setTitle(poi.mType);
                poiMarker.setSnippet(poi.mDescription);
                poiMarker.setPosition(poi.mLocation);
                Drawable icon = null;
                if (poi.mServiceId == POI.POI_SERVICE_NOMINATIM
                        || poi.mServiceId == POI.POI_SERVICE_OVERPASS_API) {
                    icon = getResources().getDrawable(R.drawable.marker_poi);
                    poiMarker.setAnchor(Marker.ANCHOR_CENTER, 1.0f);
                } else if (poi.mServiceId == POI.POI_SERVICE_GEONAMES_WIKIPEDIA) {
                    if (poi.mRank < 90)
                        icon = getResources().getDrawable(
                                R.drawable.marker_poi_wikipedia_16);
                    else
                        icon = getResources().getDrawable(
                                R.drawable.marker_poi_wikipedia_32);
                } else if (poi.mServiceId == POI.POI_SERVICE_FLICKR) {
                    icon = getResources().getDrawable(
                            R.drawable.marker_poi_flickr);
                } else if (poi.mServiceId == POI.POI_SERVICE_PICASA) {
                    icon = getResources().getDrawable(
                            R.drawable.marker_poi_picasa_24);
                    poiMarker.setSubDescription(poi.mCategory);
                }
                poiMarker.setIcon(icon);
                poiMarker.setRelatedObject(poi);
                poiMarker.setInfoWindow(poiInfoWindow);
                // thumbnail loading moved in async task for better
                // performances.
                mPoiMarkers.add(poiMarker);
            }
        }
        mPoiMarkers.setName(featureTag);
        mPoiMarkers.invalidate();
        mMapView.invalidate();
    }

    //update POI for Trip
    void updateUIWithPOIForTrip(ArrayList<POI> pois, String featureTag) {
        if (pois != null) {
            POIInfoWindow poiInfoWindow = new POIInfoWindow(mMapView);
            for (POI poi : pois) {
                Marker poiMarker = new Marker(mMapView);
                poiMarker.setTitle(poi.mType);
                poiMarker.setSnippet(poi.mDescription);
                poiMarker.setPosition(poi.mLocation);
                Drawable icon = null;
                if (poi.mServiceId == POI.POI_SERVICE_NOMINATIM
                        || poi.mServiceId == POI.POI_SERVICE_OVERPASS_API) {
                    icon = getResources().getDrawable(R.drawable.marker_poi);
                    poiMarker.setAnchor(Marker.ANCHOR_CENTER, 1.0f);
                } else if (poi.mServiceId == POI.POI_SERVICE_GEONAMES_WIKIPEDIA) {
                    if (poi.mRank < 90)
                        icon = getResources().getDrawable(
                                R.drawable.marker_poi_wikipedia_16);
                    else
                        icon = getResources().getDrawable(
                                R.drawable.marker_poi_wikipedia_32);
                } else if (poi.mServiceId == POI.POI_SERVICE_FLICKR) {
                    icon = getResources().getDrawable(
                            R.drawable.marker_poi_flickr);
                } else if (poi.mServiceId == POI.POI_SERVICE_PICASA) {
                    icon = getResources().getDrawable(
                            R.drawable.marker_poi_picasa_24);
                    poiMarker.setSubDescription(poi.mCategory);
                }
                poiMarker.setIcon(icon);
                poiMarker.setRelatedObject(poi);
                poiMarker.setInfoWindow(poiInfoWindow);
                // thumbnail loading moved in async task for better
                // performances.
                mPoiMarkers.add(poiMarker);
            }
        }
        mPoiMarkers.setName(featureTag);
        mPoiMarkers.invalidate();
        mMapView.invalidate();
    }

    //FIXME ===========updateUIWithRoad
    void updateUIWithRoad(Road road) {
        mRoadNodeMarkers.getItems().clear();
        // TextView textView = (TextView)findViewById(R.id.routeInfo);
        // textView.setText("");
        List<Overlay> mapOverlays = mMapView.getOverlays();
        if (mRoadOverlay != null) {
            mapOverlays.remove(mRoadOverlay);
            mRoadOverlay = null;
        }
        if (road == null)
            return;
        if (road.mStatus == Road.STATUS_TECHNICAL_ISSUE)
            Toast.makeText(mMapView.getContext(), getActivity().getString(R.string.technical_issue_routing),
                    Toast.LENGTH_SHORT).show();
        else if (road.mStatus > Road.STATUS_TECHNICAL_ISSUE) // functional
            // issues
            Toast.makeText(mMapView.getContext(), getActivity().getString(R.string.no_route),
                    Toast.LENGTH_SHORT).show();
        mRoadOverlay = RoadManager
                .buildRoadOverlay(road, mMapView.getContext());

//		mRoadOverlay.setColor(Color.parseColor("#00FF0000"));
        String routeDesc = road.getLengthDurationText(-1);
        mRoadOverlay.setTitle(getActivity().getString(R.string.road));
        mapOverlays.add(1, mRoadOverlay);
        // we insert the road overlay at the "bottom", just above the
        // MapEventsOverlay,
        // to avoid covering the other overlays.


        lstUserPassRoad.add(road);
        putRoadNodes(road);
        mMapView.invalidate();
        // Set route info in the text view:
        // textView.setText(routeDesc);
    }

    //FIXME ===========updateUIWithRoadForTrip
    void updateUIWithRoadForTrip(Road road) {
        mRoadNodeMarkers.getItems().clear();
        // TextView textView = (TextView)findViewById(R.id.routeInfo);
        // textView.setText("");
        List<Overlay> mapOverlays = mMapView.getOverlays();
        if (mRoadOverlay != null) {
            mapOverlays.remove(mRoadOverlay);
            mRoadOverlay = null;
        }
        if (road == null)
            return;
        if (road.mStatus == Road.STATUS_TECHNICAL_ISSUE)
            Toast.makeText(mMapView.getContext(),
                    getActivity().getString(R.string.technical_issue_routing),
                    Toast.LENGTH_SHORT).show();
        else if (road.mStatus > Road.STATUS_TECHNICAL_ISSUE) // functional
            // issues
            Toast.makeText(mMapView.getContext(), getActivity().getString(R.string.no_route),
                    Toast.LENGTH_SHORT).show();
        mRoadOverlay = RoadManager
                .buildRoadOverlay(road, mMapView.getContext());

//		mRoadOverlay.setColor(Color.parseColor("#FF69B4"));
        String routeDesc = road.getLengthDurationText(-1);
        mRoadOverlay.setTitle(getActivity().getString(R.string.road));
        mapOverlays.add(1, mRoadOverlay);
        // we insert the road overlay at the "bottom", just above the
        // MapEventsOverlay,
        // to avoid covering the other overlays.


        //lstUserPassRoad.add(road);
        putRoadNodes(road);
        mMapView.invalidate();
        // Set route info in the text view:
        // textView.setText(routeDesc);
    }

    // ### UPDATE USER LOCATION MARKER
    // duplication reason: need to add animation to user location marker
    public void updateUserLocationMarker(Drawable marker, double lat, double lon) {
        try {
            Context context = getActivity().getApplicationContext();
            ResourceProxy mResourceProxy = new DefaultResourceProxyImpl(context);
            ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
            GeoPoint point = new GeoPoint(lat, lon);
            items.add(new OverlayItem("Here", "My location", point));
            mOverlays.remove(userLocationOverlay);
            userLocationOverlay = new ItemizedIconOverlay<OverlayItem>(
                    items,
                    marker,
                    new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                        @Override
                        public boolean onItemLongPress(int arg0,
                                                       OverlayItem item) {
                            return false;
                        }

                        @Override
                        public boolean onItemSingleTapUp(final int index,
                                                         final OverlayItem item) {
                            return true;
                        }
                    }, mResourceProxy);
            // ### location marker doesn't show in navigate mode
            mOverlays.remove(userLocDynamicOverlay);
            userLocDynamicOverlay = new DynamicOverlay(context, lon, lat,
                    mMapView, true);
            if (!StaticVariable.NAVIGATE)
                mOverlays.add(userLocationOverlay);
            else
                mOverlays.add(userLocDynamicOverlay);
            mMapView.invalidate();
        } catch (NoSuchElementException err) {
            Log.e("updateUserLocationMarker", err.toString());
        } catch (IndexOutOfBoundsException err) {
            Log.e("updateUserLocationMarker", err.toString());
        }

    }

    //DEFINE subclass
    public static class MyMarker {
        private Marker marker;
        private int index;
        private int pointIndex;
        private String data;

        public MyMarker(MapView mapView) {
            marker = new Marker(mapView);
        }

        public int getIndex() {
            return index;
        }

        public Marker getMarker() {
            return marker;
        }

        public void setPointIndex(int index){
            this.pointIndex = index;
        }

        public int getPointIndex(){
            return this.pointIndex;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public void setMarker(Marker marker) {
            this.marker = marker;
        }

        public void setData(String data){
            this.data = data;
        }
        public String getData(){
            return  this.data;
        }
        public void setIcon(Drawable icon){
            marker.setIcon(icon);
        }
        public void setImage(Drawable image){
            marker.setImage(image);
        }
    }

    class OnItineraryMarkerDragListener implements Marker.OnMarkerDragListener {
        @Override
        public void onMarkerDrag(Marker marker) {
        }

        @Override
        public void onMarkerDragEnd(Marker marker) {
            int index = (Integer) marker.getRelatedObject();
            if (index == START_INDEX)
                mDeparture = marker.getPosition();
            else if (index == DEST_INDEX)
                mDestination = marker.getPosition();
            else
                viaPoints.set(index, marker.getPosition());
            // update location:
            new GeocodingTask().execute(marker);
            // update route:
            getRoadAsync();
        }

        @Override
        public void onMarkerDragStart(Marker marker) {
        }
    }

    // Async task to reverse-geocode the marker position in a separate thread:
    private class GeocodingTask extends AsyncTask<Object, Void, String> {
        Marker marker;

        protected String doInBackground(Object... params) {
            marker = (Marker) params[0];
            return getAddress(marker.getPosition());
        }

        protected void onPostExecute(String result) {
            marker.setSnippet(result);
            marker.showInfoWindow();
        }
    }

    private class ReceiverLocationChange extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent data) {

            //FIXME: Xinh ===== location change catcher


            if (null != data
                    && data.getAction().equals(
                    StaticStrings.IntentFilter_ACTION_LOCATION_CHANGE)) {

                String flagUpdate = data.getStringExtra(FlagUpdates.FlagUpdate);
                if (null != flagUpdate) {

                    Location mDetectLocation = data
                            .getParcelableExtra(FlagUpdates.FlagUpdate_CURRENT_LOCATION);
                    int Accuracy = 0;

                    if (flagUpdate.equals(FlagUpdates.FlagUpdate_DEPARTURE)) {

                        mDetectLocation = data
                                .getParcelableExtra(FlagUpdates.FlagUpdate_DEPARTURE);
                        Accuracy = (int) mDetectLocation.getBearing();

                        // Update start departure
                        mDeparture = new GeoPoint(mDetectLocation);
                        // markerStart = updateItineraryMarker(markerStart,
                        // mDeparture, START_INDEX,
                        // R.string.departure, R.drawable.marker_departure, -1,
                        // null);

                        getRoadAsync();

                        Log.e("Xinh",
                                "mDeparture: Lat = " + mDeparture.getLatitude()
                                        + " - Lon = "
                                        + mDeparture.getLongitude());

                    } else if (flagUpdate
                            .equals(FlagUpdates.FlagUpdate_DESTINATION)) {

                        mDetectLocation = data
                                .getParcelableExtra(FlagUpdates.FlagUpdate_DESTINATION);
                        Accuracy = (int) mDetectLocation.getBearing();

                        mDestination = new GeoPoint(mDetectLocation);
                        Log.e("Xinh",
                                "mDestination: Lat = "
                                        + mDestination.getLatitude()
                                        + " - Lon = "
                                        + mDestination.getLongitude());

                        // markerDestination = updateItineraryMarker(null,
                        // mDestination, DEST_INDEX,
                        // R.string.destination, R.drawable.marker_destination,
                        // -1, null);

                        getRoadAsync();

                    } else if (flagUpdate
                            .equals(FlagUpdates.FlagUpdate_VIAPOINT)) {

                        mDetectLocation = data
                                .getParcelableExtra(FlagUpdates.FlagUpdate_DESTINATION);
                        Accuracy = (int) mDetectLocation.getBearing();

                        mDestination = new GeoPoint(mDetectLocation);
                        Log.e("Xinh",
                                "mDestination: Lat = "
                                        + mDestination.getLatitude()
                                        + " - Lon = "
                                        + mDestination.getLongitude());
                        // markerDestination = updateItineraryMarker(null,
                        // mDestination, DEST_INDEX,
                        // R.string.destination, R.drawable.marker_destination,
                        // -1, null);

                        ArrayList<GeoPoint> viaPoints = data
                                .getParcelableArrayListExtra(FlagUpdates.FlagUpdate_VIAPOINT);
                        for (GeoPoint geoPoint : viaPoints) {
                            addViaPoint(geoPoint);
                        }

                        getRoadAsync();
                    }

                    GeoPoint newLocation = new GeoPoint(mDetectLocation);

                    if (!myLocationOverlay.isEnabled()) {
                        // we get the location for the first time:
                        myLocationOverlay.setEnabled(true);
                        mMapView.getController().animateTo(newLocation);
                    }

                    myLocationOverlay.setLocation(newLocation);
                    myLocationOverlay.setAccuracy(Accuracy);

                    mAzimuthAngleSpeed = data.getFloatExtra(
                            FlagUpdates.FlagUpdate_AZIMUTHANGLE, 0.0f);
                    // keep the map view centered on current location:
//					mLastLocation = newLocation;
                    mMapView.getController().animateTo(newLocation);
                    mMapView.setMapOrientation(-mAzimuthAngleSpeed);

                }
            }
        }
    }

    private class POILoadingTask extends AsyncTask<Object, Void, ArrayList<POI>> {
        String mFeatureTag;
        String message;

        protected ArrayList<POI> doInBackground(Object... params) {
            return null;
        }

        protected void onPostExecute(ArrayList<POI> pois) {
            mPOIs = pois;
            updateUIWithPOI(mPOIs, mFeatureTag);
        }
    }

    //draw road offline async
    private class UpdateRoadOfflineTask extends AsyncTask<Object, Void, Road> {
        @Override
        protected Road doInBackground(Object... params) {
            ArrayList<GeoPoint> waypoints = (ArrayList<GeoPoint>) params[0];

            Road road = new Road(waypoints);
            road.mStatus = Road.STATUS_TECHNICAL_ISSUE;

            return road;
        }

        @Override
        protected void onPostExecute(Road road) {
            mRoad = road;
            updateUIWithRoad(road);
            getPOIAsync("");

        }
    }

    /**
     * Get list road from server
     */
    private class UpdateRoadTask extends AsyncTask<Object, Void, Road> {
        protected Road doInBackground(Object... params) {
            @SuppressWarnings("unchecked")
            ArrayList<GeoPoint> waypoints = (ArrayList<GeoPoint>) params[0];
            RoadManager roadManager = null;
            roadManager = new OSRMRoadManager();
            return roadManager.getRoad(waypoints);
        }

        @Override
        protected void onPostExecute(Road road) {
            mRoad = road;
            updateUIWithRoad(road);
            getPOIAsync("");
        }
    }

    class MapOverlayListener extends Overlay {
        public MapOverlayListener(Context ctx) {
            super(ctx);
        }

        @Override
        protected void draw(Canvas arg0, MapView arg1, boolean arg2) {
        }

        private void makeOtherPoint() {
            // clear drawable point
            clear4NewPathInMultiplePath();

            // in navigate mode, startNode is user's location
            if (StaticVariable.NAVIGATE) {
                if (lastLocation != null) {
                    StaticVariable.START_NODE = new NodeDrawable(0,
                            lastLocation.getLongitude(),
                            lastLocation.getLatitude());
                }
            }
            Context context = getActivity().getApplicationContext();
            updateMarker(StaticVariable.DESTINATION_MARKER, lat, lon,
                    Constant.DESTINATION_POINT_MAKER);
            des_marker_exist = true;
            StaticVariable.DES_NODE = new NodeDrawable(0, lon, lat);

            if (StaticVariable.MULTIPLE_POINT) {
                StaticVariable.LIST_MULTIPLE_POINT.add(StaticVariable.DES_NODE);
                executeSegIDParser2(context, mMapView,
                        StaticVariable.LIST_MULTIPLE_POINT.size() - 1);
            }
            // ### request segment id of starting point and destination
            if (lastLocation == null && StaticVariable.NAVIGATE) {
                Toast.makeText(context, R.string.location_not_found,
                        Toast.LENGTH_SHORT).show();
            } else {
                executeSegIDParser(context, mMapView,
                        StaticVariable.START_NODE.lat,
                        StaticVariable.START_NODE.lon, 0);
                executeSegIDParser(context, mMapView,
                        StaticVariable.DES_NODE.lat,
                        StaticVariable.DES_NODE.lon, 1);
            }
        }

        /**
         * create starting point
         */
        private void makeStartingPoint() {
            updateMarker(StaticVariable.STARTING_MAKER, lat, lon,
                    Constant.STARTING_POINT_MAKER);
            start_marker_exist = true;
            StaticVariable.DES_SEG_ID_STATUS = false;
            StaticVariable.START_NODE = new NodeDrawable(0, lon, lat);
            StaticVariable.IV_ROUTE.setImageResource(R.drawable.routing_grey);

            // flag has true value when we try finding new path

            StaticVariable.LIST_MULTIPLE_POINT.clear();
            if (StaticVariable.MULTIPLE_POINT) {

                StaticVariable.START_NEW_PATH = false;
                if (StaticVariable.PATH_OVERLAY_EXIST) {
                    removePreviousMakingPoints(false);
                }
            }
            // add the first node to multiple point list
            StaticVariable.LIST_MULTIPLE_POINT.add(StaticVariable.START_NODE);
        }

        /**
         * double click in map view
         */
        @Override
        public boolean onDoubleTap(MotionEvent event, MapView mapView) {

            if (StaticVariable.FINISH_ROUTING) {// don't let user choose new
                // point while
                // routing
                // clear routing info text view
                clearRoutingInfo();

                // compute geographic point
                int x = (int) event.getX();
                int y = (int) event.getY();
                GeoPoint geopoint = (GeoPoint) mapView.getProjection()
                        .fromPixels(x, y);
                lat = geopoint.getLatitude();
                lon = geopoint.getLongitude();

                Context context = getActivity().getApplicationContext();

                // get the address of point
                String addressParserUrl = Constant.OSM_ADDRESS_SEARCH_URL;
                addressParserUrl += lat + "&lon=" + lon + "&zoom=18";

                // new AddressParser(context, false).execute(addressParserUrl);
                new AddressParser(getActivity(), false)
                        .execute(addressParserUrl);

                // ### remove old routing path when choose other destination
                if (StaticVariable.NAVIGATE
                        || (!StaticVariable.NAVIGATE && !add_des))
                    mOverlays.remove(StaticVariable.ROUTING_PATH);

                if (StaticVariable.MULTIPLE_POINT) {
                    // add marker
                    if (!StaticVariable.NAVIGATE) {

                        if (!StaticVariable.START_NEW_PATH
                                && start_marker_exist) {
                            Log.d("navigate_no", "Make other point");
                            makeOtherPoint();
                        } else {
                            Log.d("navigate_no", "Make start point");
                            makeStartingPoint();
                        }

                    } else {
                        Log.d("navigate", "Make other point");
                        makeOtherPoint();
                    }

                } else {
                    // add marker
                    if (!StaticVariable.NAVIGATE && !add_des) {
                        makeStartingPoint();
                        Log.d("navigate_no", "Make start point");
                    } else {
                        makeOtherPoint();
                        Log.d("navigate", "Make other point");
                    }

                    if (!StaticVariable.NAVIGATE)
                        add_des = !add_des;
                }

            }
            return true;
        }

        @Override
        public boolean onLongPress(MotionEvent event, MapView mapView) {
            // compute geographic point
            int x = (int) event.getX();
            int y = (int) event.getY();
            GeoPoint geopoint = (GeoPoint) mapView.getProjection().fromPixels(
                    x, y);
            lat = geopoint.getLatitude();
            lon = geopoint.getLongitude();
            StaticVariable.WARNING_POINT = new WarningDrawable(lat, lon);

            showContextMenu();

            return true;
        }

    }

    // ### Location listener
    public final class MyLocationListener implements LocationListener {
        public static final int OUT_OF_SERVICE = 0;
        public static final int TEMPORARILY_UNAVAILABLE = 1;
        public static final int AVAILABLE = 2;

        @Override
        public void onLocationChanged(Location loc) {
            // update right away if last location is null
            if (lastLocation == null)
                lastLocation = loc;

            if (lastStartingLocation == null)
                lastStartingLocation = loc;
            // KenK11 : period update Location
            // ###Use network location only when GPS doesn't respond for a long
            // period of time (15s)
            if (loc.getProvider() != LocationManager.NETWORK_PROVIDER
                    || loc.getTime() - lastTimeUpdateGPS > 15000) {

                GeoPoint lastLocationPoint = new GeoPoint(loc.getLatitude(),
                        loc.getLongitude());

                // ### set new user's location as map center
                if (StaticVariable.FOLLOW)
                    mMapController.animateTo(lastLocationPoint);
                // ### update user's position marker
                MapFragment.Instance.lastLocation = loc;
                updateUserLocationMarker(position_marker, loc.getLatitude(),
                        loc.getLongitude());
                // ### reroute
                GSPSender sendPacket = new GSPSender(mContext);
                sendPacket.execute(loc);

                // check if user have arrived at destination
                if (StaticVariable.PATH_OVERLAY_EXIST) {
                    NodeDrawable tmpNode = new NodeDrawable(0, loc.getLongitude(),
                            loc.getLatitude());
                    if (StaticVariable.DES_NODE != null) {
                        Log.wtf("MainActivity", "distance to destination: " + NodeDrawable.getDistance(tmpNode, StaticVariable.DES_NODE));
                        if (NodeDrawable.getDistance(tmpNode, StaticVariable.DES_NODE) < 60) {
                            String addressParserUrl = Constant.OSM_ADDRESS_SEARCH_URL;
                            addressParserUrl += StaticVariable.DES_NODE.lat + "&lon=" + StaticVariable.DES_NODE.lon + "&zoom=18";
                            new AddressParser(getActivity(), true).execute(addressParserUrl);
                            clearRoutingOverlay();
                        }
                    }
                }

                // ### check if new location satisfy the conditions to reroute
                double distance = Utilities.distance(loc, lastStartingLocation);

                if (StaticVariable.PATH_OVERLAY_EXIST
                        && StaticVariable.NAVIGATE
                        && (distance > 30)
                        && loc.getProvider() != LocationManager.NETWORK_PROVIDER) {
                    if (StaticVariable.FOLLOW)
                        mMapView.setLayoutParams(new LinearLayout.LayoutParams(
                                cross_line, cross_line));
                    Log.wtf("DISTANCE new ", distance + "");
                    lastStartingLocation = loc;
                    StaticVariable.START_NODE = new NodeDrawable(0, loc.getLongitude(), loc.getLatitude());
                    String urlStartSegID = Constant.SEGMENT_ID_URL
                            + StaticVariable.START_NODE.lat + "/" + StaticVariable.START_NODE.lon;
                    StaticVariable.START_SEG_ID_STATUS = false;
                    if (StaticVariable.DES_NODE != null)
                        new SegmentIDParser(mContext, mMapView, SegmentIDParser.REROUTE_MODE).execute(urlStartSegID);
                    lastTimeReroute = loc.getTime();
                }
                lastTimeUpdateGPS = loc.getTime();
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e("Status changed", provider + " " + status);
        }
    }

}
