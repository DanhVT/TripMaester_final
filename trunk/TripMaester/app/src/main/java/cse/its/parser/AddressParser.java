package cse.its.parser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Xml;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

import vn.edu.hcmut.its.tripmaester.R;
import vn.edu.hcmut.its.tripmaester.service.http.HttpConnection;

/**
 * @author SinhHuynh
 * @Tag GET ADDRESS FROM COORDINATE, OSM SERVICE
 */
public class AddressParser extends AsyncTask<String, Void, String> {
	Context context;
	String address = "";
	ProgressDialog PD;
	boolean arrivedNotify = false;

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
		System.out.println("address:"+url);


		try {
			HttpConnection connection = new HttpConnection();
			connection.doGet(url);

			InputStream stream = connection.getStream();
			if (stream == null){
				return null;
			}
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(stream, null);
			parser.nextTag();
			parser.require(XmlPullParser.START_TAG, null, "reversegeocode");
			parser.nextTag();
			parser.require(XmlPullParser.START_TAG, null, "result");
			if (parser.next() == XmlPullParser.TEXT) {
				address = parser.getText();
				if (address.contains(", "
						+ context.getResources().getString(R.string.tp_hcm)))
					address = address.substring(
							0,
							address.indexOf(", "
									+ context.getResources().getString(
											R.string.tp_hcm)));
				
			}
			connection.close();
		} catch (XmlPullParserException | NullPointerException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
