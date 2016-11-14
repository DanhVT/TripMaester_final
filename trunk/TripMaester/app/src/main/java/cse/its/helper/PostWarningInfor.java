package cse.its.helper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;

import cse.its.dbhelper.WarningDrawable;
import vn.edu.hcmut.its.tripmaester.R;

public class PostWarningInfor extends AsyncTask<WarningDrawable, Void, Boolean>{
	public Context context;
	
	public PostWarningInfor(Context context){
		this.context = context;
	}
	
	@Override
	protected Boolean doInBackground(WarningDrawable... params) {
		
		WarningDrawable warning = params[0];
		if(null == warning)
			return false;
		
		String url = formatURL(warning);
		
		Log.i("Url: ", url);
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);

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
				return true;
			} else {
//				Log.e("Search Segment ID Service", "Failed to get SegmentID");
				Log.e("SearchSegmentIDService", "Failed to get SegmentID");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.i("Search segment id fail", "ClientProtocolException");
		} catch (IOException e) {
			e.printStackTrace();
			Log.i("Search segment id fail", "IOException");
		}
		
		return false;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if(result){
			Toast.makeText(context, R.string.sharing_infor_success, Toast.LENGTH_SHORT).show();
		}else
			Toast.makeText(context, R.string.sharing_infor_fail, Toast.LENGTH_SHORT).show();
		
	}
	
	private String formatURL(WarningDrawable warning){
		String url = Constant.ROOT + "/hcm/police?";
		url += "latPos=" + String.valueOf(warning.getLat());
		url += "&longPos="+ String.valueOf(warning.getLog());
		url += "&type=" + warning.getType();
		
		return url;
	}



}
