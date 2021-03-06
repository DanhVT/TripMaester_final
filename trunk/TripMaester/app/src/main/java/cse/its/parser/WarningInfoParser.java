package cse.its.parser;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cse.its.dbhelper.DBTrafficSource;
import cse.its.dbhelper.WarningDrawable;
import cse.its.helper.Constant;
import group.traffice.nhn.common.StaticVariable;
import vn.edu.hcmut.its.tripmaester.R;

/**
 * @author SinhHuynh
 * @Tag get traffic condition (list of segments's speed) of a specific area in HCMC, ITS service
 */
public class WarningInfoParser extends
		AsyncTask<String, Void, ArrayList<Overlay>> implements AnimationListener {
	Context context;
	private MapView mapView;
	
	ArrayList<WarningDrawable> mWarning;
	
	DBTrafficSource dataSource;
	HashMap<Long, Double> SegmentSpeed = new HashMap<Long, Double>();
	HashMap<Long, Long> SegmentStreet = new HashMap<Long, Long>();
	double lon, lat;
	int color;
	int segment_count = 0;
	
	public static ArrayList<Overlay> listWarning = new ArrayList<Overlay>();
	public static boolean finish_get_warning_info = true;

	public WarningInfoParser() {
		super();
	}

	public WarningInfoParser(Context context, MapView mapView) {
		this.context = context;
		this.mapView = mapView;
	}

	@Override
	protected void onPreExecute() {
		finish_get_warning_info = false;
		Log.wtf("TRAFFIC INFO REQUEST", "start: " + System.currentTimeMillis()% 10000);
		dataSource = new DBTrafficSource(context);
		mWarning = new ArrayList<WarningDrawable>();
	}

	@Override
	protected ArrayList<Overlay> doInBackground(String... arg0) {
		Log.i("ON DOINBACKFROUND", " ");
		boolean flag = false;
		ArrayList<Overlay> templistWarning = new ArrayList<Overlay>();
		try {
			
			
			
			
			String url = arg0[0];
			Log.i("Url: ", url);
			StringBuilder builder = new StringBuilder();
			HttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);

			int timeout = 6000;
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
			HttpConnectionParams.setSoTimeout(httpParams, timeout);
			httpGet.setParams(httpParams);

			try {
				HttpResponse response = client.execute(httpGet);
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode == 200) {
					HttpEntity entity = response.getEntity();
					InputStream content = entity.getContent();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(content));
					String line;
					while ((line = reader.readLine()) != null) {
						builder.append(line);
					}
				} else {
					Log.e("Warning info", "Failed to get JSON");
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				Log.i("Timeout", "timeout");
			} catch (IOException e) {
				e.printStackTrace();
				Log.i("Timeout", "timeout1");
			}

			String json  = builder.toString();

			// new parser for traffic info service from
			// traffic.hcmut.edu.vn/webapp

			JSONObject result = new JSONObject(json);
			JSONArray jsonArray = result.getJSONArray("dataModel");
			int size = jsonArray.length();
			
			templistWarning = new ArrayList<Overlay>();
			
			for (int i = 0; i < size; i++) {
				//create drawable items
				JSONObject obj = jsonArray.getJSONObject(i);
				Overlay item = createdMarker(obj);
				
				if(null != item){
					templistWarning.add(item);
				}
				
				mWarning.add( new WarningDrawable(obj));
			}
			flag = true;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		dataSource.close();
		 
		if (flag){
			mapView.getOverlays().removeAll(listWarning);
			listWarning = templistWarning;
		}
			

		return listWarning;
	}

	public ArrayList<WarningDrawable> getSegment() {
		return mWarning;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(ArrayList<Overlay> result) {
		Log.i("ON POSTEXECUTE", " ");
		Log.wtf("TRAFFIC INFO REQUEST", "finish: " + System.currentTimeMillis()
				% 10000);

		if (result.size() == 0)
			Toast.makeText(context, R.string.warning_info_timeout, Toast.LENGTH_SHORT).show();
		
		Log.wtf("List path from post execute", result.size() + "");

		if (StaticVariable.SHOW_WARNING_INFO)
			mapView.getOverlays().addAll(result);

		mapView.invalidate();
		super.onPostExecute(result);
		finish_get_warning_info = true;
	}

	ArrayList<View> listAnimationDraw = new ArrayList<View>();

	// Width stroke appropriate to zoom level
	public static int getWidth() {
		int width = 4;
		int zoomLevel = Constant.ZOOM_LEVEL;
		if (zoomLevel < 16 && zoomLevel >= 13)
			width = zoomLevel - 10;
		if (zoomLevel > 16)
			width = 6;
		return width;
	}
	

	//draw warning position
	private ItemizedOverlay<OverlayItem> createdMarker(JSONObject obj) {
		ItemizedOverlay<OverlayItem> result = null;
		try{
		double lat = Double.parseDouble(obj.getString("latitude"));
		double log = Double.parseDouble(obj.getString("longitude"));
		
		ResourceProxy mResourceProxy = new DefaultResourceProxyImpl(context);
		String timepost = obj.getString("timePost");
		
		GeoPoint point = new GeoPoint(lat, log);
		List<OverlayItem> items = new ArrayList<OverlayItem>();
		items.add(new OverlayItem(timepost, "", point));
		
		Drawable marker = StaticVariable.WARNING_ICON.get(obj.getString("type"));
		result = new ItemizedIconOverlay<OverlayItem>(
				items, marker,
				new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
					@Override
					public boolean onItemSingleTapUp(final int index,
							final OverlayItem item) {
						return true;
					}

					@Override
					public boolean onItemLongPress(int arg0, OverlayItem item) {
						return false;
					}
				}, mResourceProxy);
		
		
		}catch(Exception e){
			
		}
		
		return result;
	}
		

	@Override
	public void onAnimationEnd(Animation animation) {
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
	}

	@Override
	public void onAnimationStart(Animation animation) {
	}
}
