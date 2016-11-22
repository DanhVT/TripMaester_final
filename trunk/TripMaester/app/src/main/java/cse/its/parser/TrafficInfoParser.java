package cse.its.parser;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
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
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.PathOverlay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import cse.its.dbhelper.DBTrafficHelper;
import cse.its.dbhelper.DBTrafficSource;
import cse.its.dbhelper.NodeDrawable;
import cse.its.dbhelper.SegDrawable;
import cse.its.dbhelper.StrDrawable;
import cse.its.helper.Constant;
import group.traffice.nhn.common.StaticVariable;
import vn.edu.hcmut.its.tripmaester.R;

/**
 * @author SinhHuynh
 * @Tag get traffic condition (list of segments's speed) of a specific area in HCMC, ITS service
 */
public class TrafficInfoParser extends
		AsyncTask<String, Void, ArrayList<PathOverlay>> implements
		AnimationListener {
	Context context;
	private MapView mapView;
	ArrayList<NodeDrawable> mNode;
	ArrayList<SegDrawable> mSegment;
	ArrayList<StrDrawable> mStreet;
	private Cursor DBSegment;
	DBTrafficSource dataSource;
	HashMap<Long, Double> SegmentSpeed = new HashMap<Long, Double>();
	HashMap<Long, Long> SegmentStreet = new HashMap<Long, Long>();
	double lon, lat;
	int color;
	int segment_count = 0;
	ArrayList<Integer> segment_count_list = new ArrayList<Integer>();
	public static ArrayList<PathOverlay> listPath = new ArrayList<PathOverlay>();
	public static boolean finish_get_traffic_info = true;

	public TrafficInfoParser() {
		super();
	}

	public TrafficInfoParser(Context context, MapView mapView) {
		this.context = context;
		this.mapView = mapView;
	}

	@Override
	protected void onPreExecute() {
		finish_get_traffic_info = false;
		Log.wtf("TRAFFIC INFO REQUEST", "start: " + System.currentTimeMillis()
				% 10000);
		dataSource = new DBTrafficSource(context);
		mSegment = new ArrayList<SegDrawable>();
		mNode = new ArrayList<NodeDrawable>();
		mStreet = new ArrayList<StrDrawable>();
	}

	@Override
	protected ArrayList<PathOverlay> doInBackground(String... arg0) {
		Log.i("ON DOINBACKFROUND", " ");
		try {
			String json;
			String url = arg0[0];
//			url = "http://221.133.13.113/hcm/rest/rectangleSpeed?latTL=10.754263363641192&lonTL=106.64312839508057&latTR=10.754263363641192&lonTR=106.69806003570557&latBL=10.739717260666932&lonBL=106.64312839508057&latBR=10.739717260666932&lonBR=106.69806003570557&zoom=15";
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
					Log.e("Traffic info", "Failed to get JSON");
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				Log.i("Timeout", "timeout");
			} catch (IOException e) {
				e.printStackTrace();
				Log.i("Timeout", "timeout1");
			}

			json = builder.toString();

			// new parser for traffic info service from
			// traffic.hcmut.edu.vn/webapp

			JSONArray jsonArray = new JSONArray(json);
			for (int i = 0; i < jsonArray.length(); i++) {

				String info = jsonArray.getJSONObject(i).getString("v");
				String[] segInfo = info.split(",");
				mSegment.add(new SegDrawable(0, Double.parseDouble(segInfo[0]),
						Double.parseDouble(segInfo[1]), Double
								.parseDouble(segInfo[2]), Double
								.parseDouble(segInfo[3]), Double
								.parseDouble(segInfo[4]), 0));
//				Log.i("TRAFFIC INFO", info);
			}

			// ###############################################
			/*
			 * JSONObject jsonresource = new JSONObject(json);
			 * 
			 * JSONArray jsonArray = jsonresource.getJSONArray("resource"); //
			 * Log.i("entries:", "Number of entries " + jsonObject.length());
			 * 
			 * int index = 0; long[] streetID = new long[jsonArray.length()];
			 * for (int k = 0; k < jsonArray.length(); k++) { streetID[k] =
			 * jsonArray.getJSONObject(k).getLong("streetId"); //
			 * Log.i("Street id ", streetID[k] + " "); }
			 * 
			 * for (int count = 0; count < jsonArray.length(); count++) { long
			 * tmp_min_id = 0; for (int k = 0; k < jsonArray.length(); k++) { if
			 * (tmp_min_id == 0) { if (streetID[k] != 0) { index = k; tmp_min_id
			 * = streetID[k]; } } else { if (streetID[k] != 0 && streetID[k] <
			 * tmp_min_id) { index = k; tmp_min_id = streetID[k]; } } }
			 * streetID[index] = 0; JSONObject streetObject =
			 * jsonArray.getJSONObject(index); long StreetId =
			 * streetObject.getLong("streetId"); // Log.i("Street id index ",
			 * count + " : " + streetID.toString() // + " "); //
			 * Log.i("StreetID:", "StreetID: " + StreetId); JSONObject segment =
			 * streetObject.optJSONObject("segment"); JSONArray segmentArr;
			 * 
			 * if (segment == null) {// neu segmet la mot Array segmentArr =
			 * streetObject.optJSONArray("segment"); // Log.i("PATH",StreetId +
			 * " "); segment_count = 0; long temp_segment_id = 0; for (int segI
			 * = 0; segI < segmentArr.length(); segI++) { JSONObject segments =
			 * segmentArr.getJSONObject(segI); long id_segment =
			 * segments.getInt("segmentId"); double seg_Speed =
			 * segments.getDouble("speed"); if
			 * (!SegmentSpeed.containsKey(id_segment)) { if (segI == 0 ||
			 * id_segment == (temp_segment_id + 1)) {
			 * SegmentSpeed.put(id_segment, seg_Speed); // Log.i("add segment",
			 * id_segment + ""); segment_count++; } else break; }
			 * temp_segment_id = id_segment; }
			 * segment_count_list.add(segment_count); //
			 * Log.wtf("Real number of segment on the street", //
			 * segmentArr.length()+ ""); //
			 * Log.wtf("parsed number of segment on the street", //
			 * segment_count+ ""); } else {// neu segment la object: vi co 1 doi
			 * tuong long id_segment = segment.getInt("segmentId"); //
			 * Log.i("Segmentid", id_segment + " "); double seg_Speed =
			 * segment.getDouble("speed"); if
			 * (!SegmentSpeed.containsKey(id_segment)) {
			 * SegmentSpeed.put(id_segment, seg_Speed);
			 * SegmentStreet.put(id_segment, StreetId);
			 * segment_count_list.add(1); segment_count = 0; } } }
			 */

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		/*
		 * Log.wtf("TRAFFIC INFO REQUEST", "finish parse, start query: " +
		 * System.currentTimeMillis() % 10000); addToList(SegmentSpeed);
		 * Log.wtf("TRAFFIC INFO REQUEST", "finish query: " +
		 * System.currentTimeMillis() % 10000);*/
		 dataSource.close();
		 

		mapView.getOverlays().removeAll(listPath);
		listPath = new ArrayList<PathOverlay>();
		PathOverlay path;
		for (int i = 0; i < mSegment.size(); ++i) {
			path = new PathOverlay(Color.GREEN, context);
			
			int color = getSpeedState(mSegment.get(i).speed);	
			path = new PathOverlay(color, context);
			Paint paint = path.getPaint();
			paint.setStrokeWidth(getWidth());
			paint.setAlpha(150);
			paint.setDither(true);
			paint.setStrokeCap(Paint.Cap.ROUND);
			paint.setStrokeJoin(Paint.Join.ROUND);
			path.addPoint(new GeoPoint(mSegment.get(i).Slat,mSegment.get(i).Slong));
			path.addPoint(new GeoPoint(mSegment.get(i).Elat,mSegment.get(i).Elong));
			
			listPath.add(path);
		}
		/*
		 * int i = 0; int tmp = 0; for (int segment_count : segment_count_list)
		 * { path = new PathOverlay(Color.GREEN, context); tmp = i; if
		 * (segment_count + tmp < mSegment.size()) for (; i < segment_count +
		 * tmp; ++i) { int color = getSpeedState(mSegment.get(i).speed); if
		 * (path.getNumberOfPoints() >= 2) listPath.add(path); path = new
		 * PathOverlay(color, context); Paint paint = path.getPaint();
		 * paint.setStrokeWidth(getWidth()); paint.setAlpha(150);
		 * paint.setDither(true); paint.setStrokeCap(Paint.Cap.ROUND);
		 * paint.setStrokeJoin(Paint.Join.ROUND); path.addPoint(new
		 * GeoPoint(mSegment.get(i).Slat, mSegment .get(i).Slong));
		 * path.addPoint(new GeoPoint(mSegment.get(i).Elat, mSegment
		 * .get(i).Elong)); } if (path.getNumberOfPoints() >= 2) {
		 * listPath.add(path);
		 * 
		 * }
		 * 
		 * }
		 */

		return listPath;
	}

	public ArrayList<SegDrawable> getSegment() {
		return mSegment;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(ArrayList<PathOverlay> result) {
		Log.i("ON POSTEXECUTE", " ");
		Log.wtf("TRAFFIC INFO REQUEST", "finish: " + System.currentTimeMillis()
				% 10000);

		if (result.size() == 0)
			Toast.makeText(context, R.string.traffic_info_timeout,
					Toast.LENGTH_SHORT).show();
		Log.wtf("List path from post execute", result.size() + "");

		if (StaticVariable.SHOW_TRAFFIC_INFO)
			mapView.getOverlays().addAll(result);

		mapView.invalidate();
		super.onPostExecute(result);
		finish_get_traffic_info = true;
	}

	ArrayList<View> listAnimationDraw = new ArrayList<View>();

	// static Animation animBlink;
	//

	private void addToList(HashMap<Long, Double> _SegmentSpeed) {
		// chuyen List cac segment sang string
		ArrayList<Long> segmentList = new ArrayList<Long>(
				_SegmentSpeed.keySet());
		// Log.i("Segment List: ", segmentList.toString());
		DBSegment = dataSource.getSeg(buildString(segmentList));
		Log.i("querry count", DBSegment.getCount() + " " + segmentList.size());
		// lay data
		if (DBSegment != null) {
			int seg_st;
			boolean test = true;
			if (DBSegment.moveToFirst()) {
				do {
					long seg_id = DBSegment.getLong(DBSegment
							.getColumnIndex(DBTrafficHelper.SEGMENT_ID));
					double latE = DBSegment.getDouble(DBSegment
							.getColumnIndex(DBTrafficHelper.LATE));
					double lonE = DBSegment.getDouble(DBSegment
							.getColumnIndex(DBTrafficHelper.LONE));
					double latS = DBSegment.getDouble(DBSegment
							.getColumnIndex(DBTrafficHelper.LATS));
					double lonS = DBSegment.getDouble(DBSegment
							.getColumnIndex(DBTrafficHelper.LONS));
					seg_st = DBSegment.getInt(DBSegment
							.getColumnIndex(DBTrafficHelper.SEGMENT_STREET_ID));
					mSegment.add(new SegDrawable(seg_id, latE, lonE, latS,
							lonS, _SegmentSpeed.get(seg_id), seg_st));
					if (test) {
						// Log.i("First segment id ", seg_id + " ");
						test = false;
					}
				} while (DBSegment.moveToNext());
			}
		}
	}

	private String buildString(ArrayList<Long> _list) {
		StringBuilder builder = new StringBuilder("(");
		for (long aValue : _list)
			builder.append(aValue).append(",");
		if (_list.size() > 0)
			builder.deleteCharAt(builder.length() - 1);
		builder.append(")");
		String list = builder.toString();
		return list;
	}

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

	// method return speed state of a segment
	private int getSpeedState(double speed) {
		int r = 0, g = 0;

		if (speed < 10)
			return Color.argb(150, 255, 0, 0);
		else {
			if (speed < 20) {
				r = 255;
				g += 15 * speed;
				return Color.argb(150, r, g, 0);
			} else if (speed < 30) {
				r = 255;
				g = 215;
				r -= (speed - 15) * 7;
				g += (speed - 15) * 3;
			}
		}
		return Color.argb(100, 0, 255, 0);
	}

	// test if a node on any segment that received from server
	public SegDrawable searchSegment(NodeDrawable node) {
		Log.wtf("LONG PRESS CALL FROM MAIN", " ");
		for (int i = 0; i < mSegment.size(); ++i) {
			if (mSegment.get(i).nodeTest(node))
				return mSegment.get(i);
		}
		return null;
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
