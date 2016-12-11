package cse.its.helper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import cse.its.dbhelper.WarningDrawable;
import vn.edu.hcmut.its.tripmaester.R;
import vn.edu.hcmut.its.tripmaester.service.http.HttpConnection;

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

		HttpConnection connection = new HttpConnection();
		connection.doGet(url);
		String stream = connection.getContentAsString();
		if (stream != null) {
            return true;
        } else {
//				Log.e("Search Segment ID Service", "Failed to get SegmentID");
            Log.e("SearchSegmentIDService", "Failed to get SegmentID");
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
