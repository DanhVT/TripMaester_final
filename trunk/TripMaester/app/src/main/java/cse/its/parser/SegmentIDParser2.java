package cse.its.parser;

import vn.edu.hcmut.its.tripmaester.R;
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

import cse.its.dbhelper.NodeDrawable;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * @author SinhHuynh
 * @Tag get id of the nearest segment of a node(point), ITS service
 */
public class SegmentIDParser2 extends AsyncTask<String, Void, Integer> {

	public Context context;
	public MapView mapView;
	public int index;

	String url = "";

	// 0: get segment id of starting point,
	// 1: get segment id of destination,
	// 2: used for refresh route: get new segment id of starting point -->
	// request routing after that

	public SegmentIDParser2() {
		super();
	}

	public SegmentIDParser2(Context context, MapView mapView, int index) {
		this.context = context;
		this.mapView = mapView;
		this.index = index;
	}

	@Override
	protected Integer doInBackground(String... arg0) {
		Integer segmentID = -1;
		url = arg0[0];
		Log.i("Url: ", arg0[0]);
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(arg0[0]);

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
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line = reader.readLine();
				//get segment ID
				if (line.length() > 2) {
					segmentID = Integer.parseInt(line.substring(15, line.length() - 2));
				} else
					segmentID = 0;

			} else {
				Log.e("Search Segment ID Service", "Failed to get SegmentID");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.i("Search segment id fail", "ClientProtocolException");
		} catch (IOException e) {
			e.printStackTrace();
			Log.i("Search segment id fail", "IOException");
		}
		return segmentID;
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		
		if (result != 0) {
			StaticVariable.IV_ROUTE.setImageResource(R.drawable.routing_black);
			NodeDrawable node = StaticVariable.LIST_MULTIPLE_POINT.get(index);
			node.setId(result);
			StaticVariable.LIST_MULTIPLE_POINT.set(index, node);
		} else {
			StaticVariable.DES_SEG_ID_STATUS = false;
			StaticVariable.IV_ROUTE.setImageResource(R.drawable.routing_grey);
			Toast.makeText(context, R.string.destination_not_found, Toast.LENGTH_SHORT).show();
		
		}
	}

}
