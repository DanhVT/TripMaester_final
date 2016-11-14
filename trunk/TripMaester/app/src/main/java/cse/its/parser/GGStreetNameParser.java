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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
/**
 * @author SinhHuynh
 * @Tag This class helps to get street name of a point, Google map api
 */
public class GGStreetNameParser extends AsyncTask<String, Void, String> {
	int index;
	Context context;
	public GGStreetNameParser(Context context, int index) {
		this.index = index;
		this.context = context;
	}

	@Override
	protected String doInBackground(String... params) {
		// ### json parse
		String json;
		Log.i("Url: ", params[0]);
		String street = "";

		// http connection
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(params[0]);

		int timeout = 10000;
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
		HttpConnectionParams.setSoTimeout(httpParams, timeout);
		httpGet.setParams(httpParams);

		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {// successful connection
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {// fail connection
				Log.e("Routing service", "Failed to get JSON");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		json = builder.toString();

		try {
			JSONObject jsonResource = new JSONObject(json);
			JSONArray address = jsonResource.getJSONArray("results")
					.getJSONObject(0).getJSONArray("address_components");
			// Log.wtf("Adress",
			// address.getJSONObject(1).getString("short_name"));
			boolean named = false;
			for (int i = 0; i < address.length(); ++i) {
				if (address.getJSONObject(i).getJSONArray("types").getString(0)
						.contains("route")) {
					named = true;
					street = address
							.getJSONObject(i).getString("short_name");
					RouteParser.mainStreetName.set(index, street);
				}
			}
			if (!named){
				street = "";
				RouteParser.mainStreetName.set(index, "unknown");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return street;
	}

	@Override
	protected void onPostExecute(String result) {
		
		super.onPostExecute(result);
		if(index == 0){
			String via = "<font color=#cc0029>" + context.getResources().getString(R.string.via) + "</font> "
					+ result;
			StaticVariable.TV_VIA.setText(Html.fromHtml(via));
		}
		
	}

	
	
	

}
