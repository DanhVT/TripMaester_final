package cse.its.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * @author SinhHuynh
 * @Tag This adapter helps to show search (by address, search bar on top of main
 *      map) results
 */
public class SearchResultAdapter extends ArrayAdapter<String> {

	HashMap<String, Integer> mIdMap = new HashMap <String, Integer>();

	public SearchResultAdapter(Context context, int textViewResourceId,
			ArrayList<String> objects) {
		super(context, textViewResourceId, objects);
		for (int i = 0; i < objects.size(); ++i) {
			mIdMap.put(objects.get(i), i);
		}
	}

	@Override
	public long getItemId(int position) {
		String item = getItem(position);
		return mIdMap.get(item);
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

}