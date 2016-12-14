package group.traffic.nhn.map;

import java.util.ArrayList;

import org.osmdroid.util.GeoPoint;

import vn.edu.hcmut.its.tripmaester.R;

import android.app.DialogFragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * this class create warning popup of map
 * 
 * @author Vo tinh
 *
 */
public class Popup4Map extends DialogFragment {
	private ArrayList<ContextMenuItem> mArrContextMenuItem = new ArrayList<>();
	private ListView mListView;
	private GeoPoint currentPoint = null;

	private Context mContext;

	public void setContext(Context ctx) {
		mContext = ctx;
	}

	public void setGeoPoint(GeoPoint point) {
		this.currentPoint = point;
	}

	public Popup4Map() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null == mListView) {
			mListView = (ListView) inflater.inflate(
					R.layout.listview_context_menu, container, false);

			mListView = (ListView) mListView.findViewById(R.id.listView_context_menu);

			String[] mMapPopItems = mContext.getResources().getStringArray(
					R.array.map_pop_items);
			TypedArray mMapPopIcons = mContext.getResources().obtainTypedArray(
					R.array.map_pop_icons);

			int size = mMapPopIcons.length();
			mArrContextMenuItem.clear();
			for (int i = 0; i < size; i++) {
				mArrContextMenuItem.add(new ContextMenuItem(mMapPopIcons
						.getDrawable(i), mMapPopItems[i]));
			}

			ContextMenuAdapter contextMenuAdapter = new ContextMenuAdapter(
					mContext, mArrContextMenuItem);
			mListView.setAdapter(contextMenuAdapter);
			mListView.setOnItemClickListener(new PopupItemClickListener());
		}
		return mListView;
	}

	private void itemClick(int pos) {
		mListView.setItemChecked(pos, true);
		double lat = currentPoint.getLatitude();
		double lg = currentPoint.getLongitude();
		String TAG = "Popup for map";
		Log.e(TAG, "lat=" + lat + "lg=" + lg);
		this.dismiss();

	}

	private class PopupItemClickListener implements
			ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			itemClick(position);
		}

	}

}

