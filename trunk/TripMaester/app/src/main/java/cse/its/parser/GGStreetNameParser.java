package cse.its.parser;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import group.traffice.nhn.common.StaticVariable;
import okhttp3.OkHttpClient;
import vn.edu.hcmut.its.tripmaester.R;
import vn.edu.hcmut.its.tripmaester.helper.ApiCall;
import vn.edu.hcmut.its.tripmaester.service.http.HttpConnection;

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
		String json = null;
		Log.i("Url: ", params[0]);
		String street = "";
		try {
			HttpConnection connection = new HttpConnection();
			connection.doGet(params[0]);
			InputStream stream = connection.getStream();
			if (stream == null){
				return null;
			}
			JSONObject jsonResource = new JSONObject(json);
			JSONArray address = jsonResource.getJSONArray("results")
					.getJSONObject(0).getJSONArray("address_components");
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
