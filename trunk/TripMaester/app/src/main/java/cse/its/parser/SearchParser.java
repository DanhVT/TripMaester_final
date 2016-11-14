package cse.its.parser;

import vn.edu.hcmut.its.tripmaester.R;
import group.traffic.nhn.map.DynamicOverlay;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import cse.its.adapter.SearchResultAdapter;
import cse.its.dbhelper.DBTrafficHelper;
import cse.its.dbhelper.DBTrafficSource;

/**
 * @author SinhHuynh
 * @Tag get address searching results, OSM service
 */
public class SearchParser extends AsyncTask<String, Void, ArrayList<String>> {

	Context context;
	ArrayList<String> address_list = new ArrayList<String>();
	ArrayList<Long> way_id_list = new ArrayList<Long>();
	DBTrafficSource dataSource;
	MapView mapView;
	ProgressDialog PD;
	public static DynamicOverlay searchLoc;

//	public SearchParser(Context context, MapView mapView) {
//		super();
//		this.context = context;
//		this.mapView = mapView;
//	}
	
	public SearchParser(Activity activity, MapView mapView){
		super();
		this.context = activity.getWindow().getContext();
		this.mapView = mapView;
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
		mapView.getOverlays().remove(searchLoc);
	}

	@Override
	protected ArrayList<String> doInBackground(String... params) {
		String url = params[0];
		System.out.println(url);
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (doc != null) {
			Elements links = doc.select("a[href]");
			for (Element link : links) {
				if (link.text().contains(context.getString(R.string.hcm))) {

					String id = link.attr("href");
					id = id.substring(id.lastIndexOf("/") + 1);
					way_id_list.add(Long.parseLong(id));

					String address = link.text();

					if (address
							.indexOf(", "
									+ context.getResources().getString(
											R.string.tp_hcm)) != -1)
						address = address.substring(
								0,
								address.indexOf(", "
										+ context.getResources().getString(
												R.string.tp_hcm)));
					address_list.add(address);
				}
			}
		}

		for (int i = 1; i < address_list.size(); ++i) {
			if (address_list.get(0).equalsIgnoreCase(address_list.get(i))) {
				way_id_list.remove(i);
				address_list.remove(i);
				--i;
				System.out.println("Remove " + i);
			}
		}
		return address_list;
	}

	@Override
	protected void onPostExecute(ArrayList<String> result) {
		super.onPostExecute(result);
		PD.dismiss();
		if (result.size() == 0)
			result.add(context.getResources().getString(
					R.string.search_location_not_found));
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.dialog_search_result);
		dialog.setTitle(context.getResources()
				.getString(R.string.search_result));
		ListView lv_search_result = (ListView) dialog
				.findViewById(R.id.lv_search_result);

		final SearchResultAdapter adapter = new SearchResultAdapter(context,
				android.R.layout.simple_list_item_1, result);
		lv_search_result.setAdapter(adapter);

		lv_search_result
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent,
							final View view, int position, long id) {
						if (!address_list.get(position).equalsIgnoreCase(
								context.getResources().getString(R.string.search_location_not_found))
								) {
							dataSource = new DBTrafficSource(context);
							System.out.println("ID: "
									+ way_id_list.get(position));
							Cursor segmentCursor = dataSource
									.getSegbyStreetID(way_id_list.get(position));

							if (segmentCursor.moveToFirst()) {
								double lat = segmentCursor.getDouble(segmentCursor
										.getColumnIndex(DBTrafficHelper.LATE));
								double lon = segmentCursor.getDouble(segmentCursor
										.getColumnIndex(DBTrafficHelper.LONE));
								mapView.getController().animateTo(
										new GeoPoint(lat, lon));
								searchLoc = new DynamicOverlay(context, lon,
										lat, mapView, false);
								mapView.getOverlays().add(searchLoc);
								mapView.invalidate();
							} else {
								Log.wtf("Node id: ", way_id_list.get(position)
										+ "");
								NodeParser nodeParser = new NodeParser(context,
										mapView);
								String node_url = "http://traffic.hcmut.edu.vn/MobileService/rest/get_node/"
										+ way_id_list.get(position);
								nodeParser.execute(node_url);
							}
							dataSource.close();
						}

						dialog.dismiss();
					}

				});

		dialog.show();

	}

}
