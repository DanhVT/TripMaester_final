package cse.its.parser;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.osmdroid.views.MapView;

import java.io.IOException;

import cse.its.dbhelper.NodeDrawable;
import group.traffice.nhn.common.StaticVariable;
import okhttp3.OkHttpClient;
import vn.edu.hcmut.its.tripmaester.R;
import vn.edu.hcmut.its.tripmaester.helper.ApiCall;

/**
 * @author SinhHuynh
 * @Tag get id of the nearest segment of a node(point), ITS service
 */
public class SegmentIDParser2 extends AsyncTask<String, Void, Integer> {

	public Context context;
	public MapView mapView;
	public int index;
	private final OkHttpClient client = new OkHttpClient();
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
		try {
			String response = ApiCall.GET(client, url);
			if (response.length() > 2) {
				segmentID = Integer.parseInt(response.substring(15, response.length() - 2));
			} else
				segmentID = 0;
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch(NumberFormatException e){
			e.printStackTrace();
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
