package cse.its.parser;

import group.traffic.nhn.map.DynamicOverlay;

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
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
/**
 * @author SinhHuynh
 * @Tag GET NODE (coordinate) from a way(street) ID, only used in case that ID does not exist in local db, ITS service
 */
public class NodeParser extends AsyncTask<String, Void, Void> {
	Context context;
	MapView mapView;
	
	public NodeParser(Context context, MapView mapView){
		this.context = context;
		this.mapView = mapView;
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
				System.out.println(line);
				if(!line.contains("null")){
//					System.out.println(":" + line.subSequence(15, line.indexOf(",") - 1) + ".");
//					System.out.println(":" + line.substring(line.lastIndexOf(" ") + 1, line.length() -1) + ".");
					
					double lon = Double.parseDouble(line.subSequence(15, line.indexOf(",") - 1).toString());
					double lat = Double.parseDouble(line.substring(line.lastIndexOf(" ") + 1, line.length() -1));

					SearchParser.searchLoc = new DynamicOverlay(context, lon,
							lat, mapView, false);
					mapView.getOverlays().add(SearchParser.searchLoc);
					mapView.getController().animateTo(
							new GeoPoint(lat, lon));
				}

			} else {
				Log.e("Search node info Service", "Failed to get node info");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.i("Search node info Timeout", "timeout");
		} catch (IOException e) {
			e.printStackTrace();
			Log.i("Search node info Timeout", "timeout1");
		}
		return null;
	
	}

	@Override
	protected void onPostExecute(Void result) {
		mapView.invalidate();
		super.onPostExecute(result);
	}

}
