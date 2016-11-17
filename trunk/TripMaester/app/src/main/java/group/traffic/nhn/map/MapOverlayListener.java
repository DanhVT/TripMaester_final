package group.traffic.nhn.map;//package group.traffic.nhn.map;
//
//import vn.edu.hcmut.its.tripmaester.R;
//
//import java.util.ArrayList;
//
//import org.osmdroid.util.GeoPoint;
//import org.osmdroid.views.MapView;
//import org.osmdroid.views.overlay.Overlay;
//
//import cse.its.helper.Constant;
//import cse.its.helper.StaticVariable;
//import cse.its.parser.AddressParser;
//
//import android.app.ActionBar.LayoutParams;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.res.TypedArray;
//import android.graphics.Canvas;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ListView;
//import android.widget.PopupMenu;
//import android.widget.PopupWindow;
//
//public class MapOverlayListener extends Overlay {
//	private ArrayList<ContextMenuItem> mArrContextMenuItem = new ArrayList<ContextMenuItem>();
//	private LayoutInflater mInflater;
//	private Context mContext;
//	private View mView;
//	private ListView mListView;
//	private String[] mMapPopItems;
//	private TypedArray mMapPopIcons;
//
//	private Activity mActivity;
//
//	public MapOverlayListener(Activity activity) {
//		super(activity.getApplicationContext());
//		mActivity = activity;
//		mContext = activity.getApplicationContext();
//		mInflater = (LayoutInflater) mContext
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	}
//
//	/**
//	 * double click in map view
//	 */
//	@Override
//	public boolean onDoubleTap(MotionEvent event, MapView mapView) {
//
//		if (StaticVariable.FINISH_ROUTING) {// don't let user choose new point
//											// while
//			// routing
//			// clear routing info text view
//			MapFragment.clearRoutingInfo();
//
//			// compute geographic point
//			int x = (int) event.getX();
//			int y = (int) event.getY();
//			GeoPoint geopoint = (GeoPoint) mapView.getProjection().fromPixels(
//					x, y);
//			double lat = geopoint.getLatitude();
//			double lon = geopoint.getLongitude();
//
//			// get the address of point
//			String addressParserUrl = Constant.OSM_ADDRESS_SEARCH_URL;
//			addressParserUrl += lat + "&lon=" + lon + "&zoom=18";
//			new AddressParser(context, false).execute(addressParserUrl);
//
//			// ### remove old routing path when choose other destination
//			if (StaticVariable.NAVIGATE
//					|| (!StaticVariable.NAVIGATE && !add_des))
//				mapOverlayList.remove(StaticVariable.ROUTING_PATH);
//
//			if (StaticVariable.MULTIPLE_POINT) {
//				// add marker
//				if (!StaticVariable.NAVIGATE) {
//
//					if (!StaticVariable.START_NEW_PATH && start_marker_exist) {
//						makeOtherPoint();
//					} else {
//						makeStartingPoint();
//					}
//
//				} else {
//					makeOtherPoint();
//				}
//
//			} else {
//				// add marker
//				if (!StaticVariable.NAVIGATE && !add_des) {
//					makeStartingPoint();
//				} else {
//					makeOtherPoint();
//				}
//
//				if (!StaticVariable.NAVIGATE)
//					add_des = !add_des;
//			}
//
//		}
//		return true;
//	}
//
//	@Override
//	protected void draw(Canvas arg0, MapView arg1, boolean arg2) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public boolean onLongPress(MotionEvent event, MapView mapView) {
//		// compute geographic point
//		int x = (int) event.getX();
//		int y = (int) event.getY();
//		GeoPoint geopoint = (GeoPoint) mapView.getProjection().fromPixels(x, y);
//
//		Popup4Map mDialog = new Popup4Map();
//
//		mDialog.setContext(mContext);
//		mDialog.setGeoPoint(geopoint); // set current location
//
//		mDialog.show(mActivity.getFragmentManager(), "Warning");
//
//		return true;
//	}
//
//}