package cse.its.parser;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import vn.edu.hcmut.its.tripmaester.R;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
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
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

/**
 * @author SinhHuynh
 * @Tag GET ADDRESS FROM COORDINATE, OSM SERVICE
 */
public class AddressParser extends AsyncTask<String, Void, String> {
	Context context;
	String address = "";
	ProgressDialog PD;
	boolean arrivedNotify = false;
	private final OkHttpClient client = new OkHttpClient();

	Activity mActivity;
	public AddressParser(Activity activity, boolean arriveNotify){
		super();
		mActivity = activity;
		arrivedNotify = arriveNotify;
		context= mActivity.getWindow().getContext();
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		PD = new ProgressDialog(context);
		PD.setTitle(context.getResources().getString((R.string.loading)));
		PD.setMessage(context.getResources().getString(R.string.wait));
		PD.setCancelable(false);
		PD.show();
	}

	@Override
	protected String doInBackground(String... params) {
		String url = params[0];
		System.out.println(url);
		String xml = null;
		Request request = new Request.Builder()
				.url(url)
				.header("User-Agent", "OkHttp Headers.java")
				.addHeader("Accept", "application/json; q=0.5")
				.addHeader("Accept", "application/vnd.github.v3+json")
				.build();
		try {
			Response response = client.newCall(request).execute();
			xml = response.body().string();
		} catch (IOException e) {
			e.printStackTrace();
		}

		InputStream is = new ByteArrayInputStream(xml.getBytes());
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(is, null);
			parser.nextTag();
			parser.require(XmlPullParser.START_TAG, null, "reversegeocode");
			parser.nextTag();
			parser.require(XmlPullParser.START_TAG, null, "result");
			if (parser.next() == XmlPullParser.TEXT) {
				address = parser.getText();
				if (address.indexOf(", "
						+ context.getResources().getString(R.string.tp_hcm)) != -1)
					address = address.substring(
							0,
							address.indexOf(", "
									+ context.getResources().getString(
											R.string.tp_hcm)));
				
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Logger.t("xml").d(address);

		return address;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		PD.dismiss();

		if (arrivedNotify) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(context);
			dialog.setMessage(context.getString(R.string.arrive) + " " + result);
			dialog.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(
								DialogInterface paramDialogInterface,
								int paramInt) {
						}
					});

			dialog.show();
		}

		if (result == "")
			result = context.getResources().getString(
					R.string.search_location_not_found);

		Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
	}

}
