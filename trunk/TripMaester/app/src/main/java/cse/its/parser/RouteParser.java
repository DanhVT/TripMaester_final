package cse.its.parser;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.PathOverlay;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import cse.its.dbhelper.DBStaticLocHelper;
import cse.its.dbhelper.DBStaticLocSource;
import cse.its.dbhelper.NodeDrawable;
import cse.its.dbhelper.SegDrawable;
import cse.its.helper.Constant;
import cse.its.helper.ModeHelper;
import cse.its.voice.VietnameseVoice;
import group.traffice.nhn.common.StaticVariable;
import okhttp3.OkHttpClient;
import vn.edu.hcmut.its.tripmaester.R;
import vn.edu.hcmut.its.tripmaester.helper.ApiCall;

/**
 * @author SinhHuynh
 * @Tag get route(list of streets, segments) from ITS or Google maps api
 */
public class RouteParser extends AsyncTask<String, Void, PathOverlay> {
	private final OkHttpClient client = new OkHttpClient();
	public static final int ITS_API_MODE = 0;
	public static final int GOOGLE_API_MODE = 1;
	public Context context;
	boolean rerouteMode;
	int apiMode;
	public MapView mapView;
	Paint pathPaint = new Paint();
	// used in case checking if user's location on the route or not
	// main node is a beginning node of a returned street
	public static ArrayList<NodeDrawable> mainNodes = new ArrayList<>();
	// used to determine direction, last segment and beginning segment of each
	// returned street
	ArrayList<SegDrawable> mainSegs = new ArrayList<>();
	ArrayList<SegDrawable> firstSegList = new ArrayList<>();
	ArrayList<SegDrawable> lastSegList = new ArrayList<>();
	public static ArrayList<Integer> directionList = new ArrayList<>();
	public static ArrayList<String> mainStreetName = new ArrayList<>();
	public static ArrayList<Integer> mainStreetLength = new ArrayList<>();
	public static ArrayList<Long> mainStreetId = new ArrayList<>();
	// estimated time and distance for returned path
	int time = 0;
	double distance = 0;

	SegDrawable firstSeg;
	double angle_map_rotate;
	// distance of segment users have to go by before they can meet the turning
	// point
	public static int distance_to_next_turning_point = 0;
	ProgressDialog progressDialog;

	DBStaticLocSource dbStaticSource;
	Cursor cursor;

	public static String direction = "";// left, right, straight ahead

	public RouteParser() {
		super();
	}
	
//	public RouteParser(Activity activity, MapView mapView, boolean rerouteMode, int apiMode){
//		this.context = activity.getWindow().getContext();
//		this.mapView = mapView;
//		this.rerouteMode = rerouteMode;
//		this.apiMode = apiMode;
//		dbStaticSource = new DBStaticLocSource(context);
//	}
	
//	public RouteParser(Activity activity){
//		this.context = activity.getWindow().getContext();
//		this.dbStaticSource = new DBStaticLocSource(context);
//	}
//	
//	public void setRouteMode(MapView mapView, boolean routemode, int apimode){
//		this.rerouteMode = routemode;
//		this.apiMode = apimode;
//		this.mapView = mapView;
//		
//	}
	
	public RouteParser(Context context, MapView mapView, boolean rerouteMode,
			int apiMode) {
		this.context = context;
		this.mapView = mapView;
		this.rerouteMode = rerouteMode;
		this.apiMode = apiMode;
		dbStaticSource = new DBStaticLocSource(context);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = new ProgressDialog(context);
		progressDialog.setTitle(context.getResources().getString((R.string.loading)));
		progressDialog.setMessage(context.getResources().getString(R.string.wait));
		progressDialog.setCancelable(false);
		progressDialog.show();

	}

	@Override
	protected PathOverlay doInBackground(String... arg0) {
		// ### remove old routing path
		mapView.getOverlays().remove(StaticVariable.ROUTING_PATH);
		StaticVariable.PATH_OVERLAY_EXIST = false;
		
		// create new path
		StaticVariable.ROUTING_PATH = new PathOverlay(Color.BLUE, context);
		pathPaint = StaticVariable.ROUTING_PATH.getPaint();
		pathPaint.setStrokeWidth(8);
		pathPaint.setShadowLayer(0.1f, 0.1f, 0.1f, Color.BLACK);
		pathPaint.setARGB(100, 10, 120, 255);
		pathPaint.setDither(true);
		pathPaint.setStrokeCap(Paint.Cap.ROUND);
		pathPaint.setStrokeJoin(Paint.Join.ROUND);

		// ### renew main nodes, segments and streets array lists
		mainNodes = new ArrayList<>();
		mainSegs = new ArrayList<>();
		firstSegList = new ArrayList<>();
		lastSegList = new ArrayList<>();
		directionList = new ArrayList<>();
		mainStreetName = new ArrayList<>();
		mainStreetLength = new ArrayList<>();
		mainStreetId = new ArrayList<>();
		firstSeg = new SegDrawable();
		// ### json parse
		String json;
		try {
			json = ApiCall.GET(client, arg0[0]);


			// ### ITS API
			if (apiMode == ITS_API_MODE) {
				JSONArray jsonArray;
				if(StaticVariable.MULTIPLE_POINT)
					jsonArray = new JSONArray(json);
				else{
					jsonArray = new JSONArray();
					if (json != null && json.length() > 0) {
						JSONObject jsonResource = new JSONObject(json);
						jsonArray.put(jsonResource);
					}
				}

				int size = jsonArray.length();
				int tmp_street_length = 0;
				for(int k = 0 ; k < size ; k++){
					JSONObject jsonResource = jsonArray.getJSONObject(k);
					
					if (jsonResource.getDouble("realtime") < 0)
						time = -1;
					else {
						Calendar c = Calendar.getInstance();
						int current_minute = c.get(Calendar.MINUTE)+ c.get(Calendar.HOUR_OF_DAY) * 60;
						time += (int) (jsonResource.getDouble("realtime") - current_minute);
					}
					Locale locale = Locale.getDefault();
					NumberFormat format = NumberFormat.getInstance(locale);
					Number number = format.parse(String.format(Locale.getDefault(),"%.1f", jsonResource.getDouble("distance") / 1000));
					distance += number.doubleValue();
					Log.wtf("TIME: ", time + "  " + distance);
					// ###Path -> list streets
					JSONArray jsonPath = jsonResource.getJSONArray("path");
					
					for (int i = 0; i < jsonPath.length(); ++i) {
						// ### Street -> list segment
						JSONArray jsonStreet = jsonPath.getJSONObject(i)
								.getJSONArray("segments");
						// String street = jsonPath.getJSONObject(i).getString(
						// "street_name");
						long id = Long.parseLong(jsonPath.getJSONObject(i).getString("street_id"));
						mainStreetId.add(id);
						cursor = dbStaticSource.getObjectName("way", id);
						if (cursor.moveToFirst()) {
							// Log.wtf("NEW STREET", cursor.getString(cursor
							// .getColumnIndex(DBStaticLocHelper.NAME)));
							String street_name = cursor.getString(cursor
									.getColumnIndex(DBStaticLocHelper.NAME));
							mainStreetName.add(street_name);
						} else {
							if (mainStreetName.size() > 0) {
								String tmpStreetName = mainStreetName
										.get(mainStreetName.size() - 1);
								if (!tmpStreetName.contains(context
										.getString(R.string.hem)))
									tmpStreetName = context.getString(R.string.hem)
											+ " " + tmpStreetName;
								else
									Log.wtf("Alley name", tmpStreetName);
								mainStreetName.add(tmpStreetName);
							}

						}

						
						for (int j = 0; j < jsonStreet.length(); ++j) {
							JSONObject jsonSeg = jsonStreet.getJSONObject(j);
							// Log.i("Segment", jsonSeg.toString());
							tmp_street_length += jsonSeg.getDouble("distance");

							double lon1 = jsonSeg.getDouble("lng1");
							double lon2 = jsonSeg.getDouble("lng2");
							double lat1 = jsonSeg.getDouble("lat1");
							double lat2 = jsonSeg.getDouble("lat2");
							StaticVariable.ROUTING_PATH.addPoint(new GeoPoint(lat1,
									lon1));
							StaticVariable.ROUTING_PATH.addPoint(new GeoPoint(lat2,
									lon2));
							// add the first segment of street
							if (j == 0) {
								firstSegList.add(new SegDrawable(0, lat2, lon2,
										lat1, lon1, 0, 0));
							}
							// add the last segment of street
							if (j == jsonStreet.length() - 1) {
								lastSegList.add(new SegDrawable(0, lat2, lon2,
										lat1, lon1, 0, 0));
							}

							if (i == 0) {// first street of path
								if (j == 1) {
									mainNodes.add(new NodeDrawable(0, lon1, lat1));
									if (jsonPath.length() == 1)
										mainNodes.add(new NodeDrawable(0, lon2,
												lat2));
									// first segment, segment start from user's
									// location
									// is not counting
									firstSeg = new SegDrawable(0, lat2, lon2, lat1,
											lon1, 0, 0);
								}
								// first main segment is the last seg of the first
								// street
								if (j == (jsonStreet.length() - 1)) {
									mainSegs.add(new SegDrawable(0, lat2, lon2,
											lat1, lon1, 0, 0));
								}

							} else if (j == 0) {// first segment of street
								mainNodes.add(new NodeDrawable(0, lon1, lat1));
								mainSegs.add(new SegDrawable(0, lat2, lon2, lat1,
										lon1, 0, 0));
							}
						}

//						if (i == 0)
//							distance_to_next_turning_point = tmp_street_length;
						mainStreetLength.add(tmp_street_length);
					}
				}
				

				
			}
			// ### GOOGLE API
			else {
				JSONObject jsonResource = new JSONObject(json);

				// Log.wtf("GG ROUTE",jsonResource.getJSONArray("routes").getJSONObject(0).toString());
				JSONArray legs = jsonResource.getJSONArray("routes")
						.getJSONObject(0).getJSONArray("legs");

				Locale locale = Locale.getDefault();
				NumberFormat format = NumberFormat.getInstance(locale);
				Number number = format.parse(String.format(Locale.getDefault(), "%.1f",
						legs.getJSONObject(0).getJSONObject("distance")
								.getDouble("value") / 1000));
				distance = number.doubleValue();
				time = legs.getJSONObject(0).getJSONObject("duration")
						.getInt("value") / 60;
				JSONArray steps = legs.getJSONObject(0).getJSONArray("steps");
				for (int i = 0; i < steps.length(); ++i) {
					// Log.wtf("Step", steps.getString(i));
					JSONObject step = steps.getJSONObject(i);
					double lon1 = step.getJSONObject("start_location")
							.getDouble("lng");
					double lon2 = step.getJSONObject("end_location").getDouble(
							"lng");
					double lat1 = step.getJSONObject("start_location")
							.getDouble("lat");
					double lat2 = step.getJSONObject("end_location").getDouble(
							"lat");

					String decodedPolyline = step.getJSONObject("polyline")
							.getString("points");
					ArrayList<GeoPoint> points;
					points = decodePoly(decodedPolyline);

					StaticVariable.ROUTING_PATH.addPoint(new GeoPoint(lat1, lon1));
					for (GeoPoint point : points) {
						StaticVariable.ROUTING_PATH.addPoint(point);
						// Log.wtf("point", point.toString());
					}

					StaticVariable.ROUTING_PATH.addPoint(new GeoPoint(lat2, lon2));

					mainStreetName.add(context.getResources().getString(
							R.string.loading));

					new GGStreetNameParser(context, i)
							.execute(Constant.GOOGLE_STREET_NAME + lat1 + "," + lon1);

					String tmpDirection = "";
					if (!step.isNull("maneuver")) {
						tmpDirection = step.getString("maneuver");
						// Log.wtf("MANEUVER", tmpDirection);
					}

					if (tmpDirection.contains("turn-right"))
						directionList.add(SegDrawable.RIGHT);
					else if (tmpDirection.contains("turn-left"))
						directionList.add(SegDrawable.LEFT);
					else
						directionList.add(SegDrawable.STRAIGTH);
					mainStreetLength.add(steps.getJSONObject(i)
							.getJSONObject("distance").getInt("value"));
				}

			}
			if(mainStreetLength.size() > 0)
			distance_to_next_turning_point = mainStreetLength.get(0);

		} catch (JSONException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// ### Direction notification
		if (mainNodes.size() > 1 && StaticVariable.NAVIGATE) {
			// ### Rotate map view
			SegDrawable pivotSeg = new SegDrawable(0, 0, 0, 1, 0, 0, 0);
			NodeDrawable node0 = mainNodes.get(0);
			NodeDrawable node1 = mainNodes.get(1);
			// ### Calculate Angle to rotate map
			angle_map_rotate = SegDrawable.angle(pivotSeg, new SegDrawable(
					node0, node1));
			if (node1.lon > node0.lon) {
				if (node1.lat > node0.lat && angle_map_rotate > 90)
					angle_map_rotate = 180 - angle_map_rotate;
				if (node1.lat <= node0.lat && angle_map_rotate <= 90)
					angle_map_rotate = 180 - angle_map_rotate;
			} else {
				angle_map_rotate = -angle_map_rotate;
				if (node1.lat > node0.lat && angle_map_rotate < -90)
					angle_map_rotate = -180 - angle_map_rotate;
				if (node1.lat <= node0.lat && angle_map_rotate >= -90)
					angle_map_rotate = -180 - angle_map_rotate;
			}
			Log.wtf("Angle", "" + angle_map_rotate);

			if (mainSegs.size() > 1 && mainStreetName.size() > 1)
				Log.wtf("Direction", distance + " "
						+ mainSegs.get(0).direction(mainSegs.get(1), context)
						+ " from " + mainStreetName.get(0) + " to "
						+ mainStreetName.get(1));
		}

		return StaticVariable.ROUTING_PATH;
	}

	@Override
	protected void onPostExecute(PathOverlay result) {
		super.onPostExecute(result);
		// Direction list (list of turning point on the route)
		if (apiMode == ITS_API_MODE) {
			for (int i = 1; i < firstSegList.size(); ++i) {
				directionList.add(lastSegList.get(i - 1).directionMode(
						firstSegList.get(i), context));
			}
			if (directionList.size() < mainStreetName.size()) {
				directionList.add(0, SegDrawable.STRAIGTH);
			}
		}
		Log.wtf("Route Parser", "directionList: " + directionList.size() + " mainStreetName " + mainStreetName.size());
		progressDialog.dismiss();
		dbStaticSource.close();
		// notify finish doing background
		StaticVariable.FINISH_ROUTING = true;
		if (StaticVariable.ROUTING_PATH.getNumberOfPoints() == 0) {
			Toast.makeText(context, R.string.path_not_found, Toast.LENGTH_SHORT).show();
			if (rerouteMode)
				StaticVariable.PATH_OVERLAY_EXIST = true;
		} else {
			
			mapView.getOverlays().add(StaticVariable.ROUTING_PATH);
			mapView.invalidate();
			StaticVariable.PATH_OVERLAY_EXIST = true;
			
			// ### Routing info update
			String tmp = context.getResources().getString(R.string.distance);
			String time_distance = "<font color=#cc0029>" + tmp + ":</font> "
					+ distance + " km";
			tmp = context.getResources().getString(R.string.time);
			if (time != -1)
				time_distance += "  <font color=#cc0029>" + tmp + ":</font> "
						+ time + " min";
			if (time >= 2)
				time_distance += "s";
			StaticVariable.TV_TIME_DESTANCE.setText(Html.fromHtml(time_distance));
			StaticVariable.TV_TIME_DESTANCE.setVisibility(View.VISIBLE);

			if (mainStreetName.size() > 0) {
				tmp = context.getResources().getString(R.string.via);
				String via = "<font color=#cc0029>" + tmp + "</font> "
						+ mainStreetName.get(0);
				if (mainStreetName.get(0).length() == 0)
					via += "...";
				if (via.length() > 60)
					via = via.substring(0, 60) + "...";
				else if (mainStreetName.size() > 2)
					via += "...";
				
				StaticVariable.TV_VIA.setText(Html.fromHtml(via));
				StaticVariable.TV_VIA.setVisibility(View.VISIBLE);
				StaticVariable.TV_ROUTING_DETAIL.setVisibility(View.VISIBLE);

			}
		}
		if (StaticVariable.FOLLOW && mainSegs.size() > 1
				&& mainStreetName.size() > 1) {
			mapView.setRotation((float) -angle_map_rotate);
			// mapView.invalidate();
			String nextStreet = mainStreetName.get(1);
			if (nextStreet.startsWith(context.getResources().getString(
					R.string.vong_xoay))
					|| nextStreet.startsWith(context.getResources().getString(
							R.string.bung_binh))
					|| nextStreet.startsWith(context.getResources().getString(
							R.string.nga)))
				direction = context.getString(R.string.go_straight_ahead);
			else
				direction = mainSegs.get(0).direction(mainSegs.get(1), context);

			// Show guidance
			makeToast(context, distance_to_next_turning_point);

			// Direction speaker
			speakOut(context, distance_to_next_turning_point);

		}

	}

	public static void makeToast(Context context,
			int distance_to_next_turning_point) {
		String guidance = direction;
		// add next street's name
		if (mainStreetName.size() > 1)
			guidance += " " + context.getString(R.string.from) + " "
					+ mainStreetName.get(0) + " "
					+ context.getString(R.string.to) + " "
					+ mainStreetName.get(1);
		if (!direction.contains("Ä�i") && !direction.contains("Go")) {
			guidance = context.getString(R.string.go_straight_ahead) + " "
					+ distance_to_next_turning_point + "m, " + guidance;
		}

		Toast.makeText(context, guidance, Toast.LENGTH_LONG).show();
	}

	public static void speakOut(Context context,
			int distance_to_next_turning_point) {
		if (StaticVariable.VOICE) {
			// English
			//if (StaticVariable.INT_LANGUAGE_MODE MainActivity.language == ModeHelper.ENGLISH_MODE) {
			if (StaticVariable.INT_LANGUAGE_MODE == ModeHelper.ENGLISH_MODE) {
				String message = context.getString(R.string.go_straight_ahead)
						+ " " + distance_to_next_turning_point + "m ";
				if (direction.contains("left") || direction.contains("right"))
					message += ", then  " + direction;
				if (!StaticVariable.TEXT_TO_SPEECH.isSpeaking())
					StaticVariable.TEXT_TO_SPEECH.speak(message, TextToSpeech.QUEUE_FLUSH,
							null);
			}
			// Vietnamese
			else {
				Log.wtf("Vietnamese message", direction);
				int direction_mode = VietnameseVoice.AHEAD;
				if (direction.contains(context.getResources().getString(
						R.string.trai)))
					direction_mode = VietnameseVoice.LEFT;
				else if (direction.contains(context.getResources().getString(
						R.string.phai)))
					direction_mode = VietnameseVoice.RIGHT;

				VietnameseVoice vV = new VietnameseVoice(context,
						direction_mode, distance_to_next_turning_point, null);
				vV.speak();
			}
		}

	}

	/**
	 * decode polyline of google map 
	 * @param encoded: encoded string
	 * @return: list of Geopoint 
	 */
	private ArrayList<GeoPoint> decodePoly(String encoded) {

		ArrayList<GeoPoint> poly = new ArrayList<>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;

		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;

			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			GeoPoint p = new GeoPoint((((double) lat / 1E5)),
					(((double) lng / 1E5)));
			poly.add(p);
		}

		return poly;
	}

}
