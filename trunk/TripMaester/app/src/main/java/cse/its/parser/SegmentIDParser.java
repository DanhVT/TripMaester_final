package cse.its.parser;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.orhanobut.logger.Logger;
import org.osmdroid.views.MapView;
import java.io.IOException;
import group.traffice.nhn.common.StaticVariable;
import okhttp3.OkHttpClient;
import vn.edu.hcmut.its.tripmaester.R;
import vn.edu.hcmut.its.tripmaester.helper.ApiCall;

/**
 * @author SinhHuynh
 * @Tag get id of the nearest segment of a node(point), ITS service
 */
public class SegmentIDParser extends AsyncTask<String, Void, Integer> {

	public Context context;
	public MapView mapView;
	public int mode;
	private final OkHttpClient client = new OkHttpClient();
	public static final int STARTING_POINT_MODE = 0;
	public static final int DESINATION_MODE = 1;
	public static final int REROUTE_MODE = 2;
	String url = "";

	// 0: get segment id of starting point,
	// 1: get segment id of destination,
	// 2: used for refresh route: get new segment id of starting point -->
	// request routing after that

	public SegmentIDParser() {
		super();
	}

	public SegmentIDParser(Context context, MapView mapView, int mode) {
		this.context = context;
		this.mapView = mapView;
		this.mode = mode;
	}

	@Override
	protected Integer doInBackground(String... arg0) {
		Integer segmentID = -1;
		url = arg0[0];
		Log.i("Url: ", arg0[0]);
		try {
			String response = ApiCall.GET(client, url);
			if (mode == DESINATION_MODE)
				Log.i("DES Segment id", response);
			else
				Log.i("START Segment id", response);
			if (response.length() > 2) {
				segmentID = Integer.parseInt(response.substring(15, response.length() - 2));
			} else
				segmentID = 0;
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch(NumberFormatException ex){
			ex.printStackTrace();
		}
		return segmentID;
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		Logger.t("result").d(result);
		// Log.wtf("SegmentId", "Mode " + mode + " " + result);
		if (mode == DESINATION_MODE) {// destination segment
			StaticVariable.DES_SEGMENT_ID = result;
			if (result != 0) {
				StaticVariable.DES_SEG_ID_STATUS = true;
				if (StaticVariable.START_SEG_ID_STATUS)// set up when both des and
													// start segment are found,
													// in case start is found
													// first
					StaticVariable.IV_ROUTE.setImageResource(R.drawable.routing_black);
			} else {
				StaticVariable.DES_SEG_ID_STATUS = false;
				StaticVariable.IV_ROUTE.setImageResource(R.drawable.routing_grey);
				Toast.makeText(context, R.string.destination_not_found, Toast.LENGTH_SHORT).show();
			}
		} else {// starting segment
			StaticVariable.START_SEGMENT_ID = result;
			if (result != 0) {
				StaticVariable.START_SEG_ID_STATUS = true;
				if (StaticVariable.DES_SEG_ID_STATUS)// set up when both destination
												// and
												// starting segment are found
					StaticVariable.IV_ROUTE.setImageResource(R.drawable.routing_black);

				// ### execute REROUTE, check if user's current location is on
				// the route by calling streetIdParser
				if (mode == REROUTE_MODE) {
					// MainActivity.requestRouting(context, mapView);
					String[] tmp = url.split("/");
					Double lat = Double.parseDouble(tmp[6]);
					Double lon = Double.parseDouble(tmp[7]);
					System.out.println("SegmentIDPARSER : " + tmp[6] + "  "
							+ tmp[7]);
					String streetId_url = "http://traffic.hcmut.edu.vn/MobileService/rest/get_street_id/"
							+ result;
					new StreetIDParser(context, mapView, lat, lon)
							.execute(streetId_url);

				}

			} else {
				StaticVariable.START_SEG_ID_STATUS = false;
				StaticVariable.IV_ROUTE.setImageResource(R.drawable.routing_grey);
				Toast.makeText(context, R.string.starting_point_not_found, Toast.LENGTH_SHORT).show();
			}
		}

	}

}
