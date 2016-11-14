package cse.its.parser;

import group.traffic.nhn.map.MapFragment;
import group.traffice.nhn.common.StaticVariable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
import org.osmdroid.views.MapView;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import cse.its.dbhelper.DBStaticLocHelper;
import cse.its.dbhelper.DBStaticLocSource;
import cse.its.dbhelper.NodeDrawable;

/**
 * @author SinhHuynh
 * @Tag get Street ID of a segment (ITS service), check whether user's current
 *      location is on route, if not -> call reroute
 */
public class StreetIDParser extends AsyncTask<String, Void, Void> {
	Context context;
	MapView mapView;
	boolean on_the_road = false;
	Double lat, lon;
	int distance_to_next_turning_point = 0;
	boolean give_guidance = false;

	static boolean still_on_the_road = true;

	public StreetIDParser(Context context, MapView mapView, Double lat,
			Double lon) {
		this.context = context;
		this.mapView = mapView;
		this.lat = lat;
		this.lon = lon;
	}

	@Override
	protected Void doInBackground(String... params) {

		Log.i("Url: ", params[0]);
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(params[0]);

		int timeout = 3000;
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
				String line = reader.readLine();
				System.out.println("StreetID: " + line);
				if (!line.contains("null")) {
					Long current_streetid = Long.parseLong(line);
					on_the_road = false;
					/*
					 * for(Long streetid: RouteParser.mainStreetId){
					 * System.out.println("Main street id " + streetid);
					 * if(streetid == current_streetid){ on_the_road = true;
					 * break; } }
					 */

					Cursor cursor = new DBStaticLocSource(context).getObjectName("way", current_streetid);
					if (cursor.moveToFirst()) {
						// Log.wtf("NEW STREET", cursor.getString(cursor
						// .getColumnIndex(DBStaticLocHelper.NAME)));
						String current_street_name = cursor.getString(cursor
								.getColumnIndex(DBStaticLocHelper.NAME));
						System.out.println("No. of node: "
								+ RouteParser.mainNodes.size()
								+ " No. of street: "
								+ RouteParser.mainStreetName.size());
						// for (int i = 0; i <
						// RouteParser.mainStreetName.size(); ++i) {

						String streetName = RouteParser.mainStreetName.get(0);
						// System.out.println("Main street id " +
						// streetName);
						if (streetName.contains(current_street_name)) {
							on_the_road = true;
							if (RouteParser.mainNodes.size() > 1) {
								distance_to_next_turning_point = (int) NodeDrawable
										.getDistance(
												RouteParser.mainNodes.get(1),
												new NodeDrawable(0, lon, lat));
								give_guidance = true;
								System.out
										.println("New distance to next turning point  "
												+ distance_to_next_turning_point);
							}

						}

					}

					if (RouteParser.mainStreetName.size() == 0)
						on_the_road = true;
					System.out.println("On the road " + on_the_road);
				}

			} else {
				Log.e("StreetIDParser", "Failed to get node info");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.i("StreetIDParser", "ClientProtocolException");
		} catch (IOException e) {
			e.printStackTrace();
			Log.i("StreetIDParser", "IOException");
		}
		return null;

	}

	@Override
	protected void onPostExecute(Void result) {

		if (!on_the_road) {// user's not following the suggested route
			Log.wtf("Still on the road", still_on_the_road + "");
			// ### DOUBLE CHECK IF USER IS ON THE PATH
			if (!still_on_the_road) {
				// ### Reroute
				MapFragment.requestRouting(context, mapView, true);
				still_on_the_road = true;
			} else
				still_on_the_road = false;

		} else {// user's on the suggested route
			still_on_the_road = true;
			if (give_guidance) {
				// check if user follow inverse direction
				if (distance_to_next_turning_point > RouteParser.distance_to_next_turning_point + 100) {
					MapFragment.requestRouting(context, mapView, true);
				} else {// check if user have passed the turning point
					int distance_form_first_node = (int) NodeDrawable
							.getDistance(RouteParser.mainNodes.get(0),
									new NodeDrawable(0, lon, lat));
					if (distance_form_first_node > RouteParser.distance_to_next_turning_point) {
						MapFragment.requestRouting(context, mapView, true);

					} else {// user approaches the next turning point
						RouteParser.makeToast(context,
								distance_to_next_turning_point);
						if ((distance_to_next_turning_point < 200 && distance_to_next_turning_point > 150)
								|| (distance_to_next_turning_point < 100 && distance_to_next_turning_point > 50)) {
							RouteParser.speakOut(context,
									distance_to_next_turning_point);
						} else if (System.currentTimeMillis()
								- StaticVariable.LAST_TIME_REQUEST_ROUTING > 45000) {
							MapFragment.requestRouting(context, mapView, true);
						}
					}
				}

			}

		}
		super.onPostExecute(result);
	}
}
