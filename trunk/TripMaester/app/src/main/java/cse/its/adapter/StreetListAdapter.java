package cse.its.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import vn.edu.hcmut.its.tripmaester.R;

/**
 * @author SinhHuynh
 * @Tag This adapter helps to show review of routing result which includes names
 *      of streets, distances and directions
 */
public class StreetListAdapter extends ArrayAdapter<String> {
	ArrayList<String> streetNameList;
	ArrayList<Integer> streetLengthList;
	ArrayList<Integer> directionList;
	Context context;
	int layoutId;

	public StreetListAdapter(Context context, int layoutId,
			ArrayList<String> streetNameList,
			ArrayList<Integer> streetLengthList,
			ArrayList<Integer> directionList) {
		super(context, layoutId);
		this.streetNameList = streetNameList;
		this.streetLengthList = streetLengthList;
		this.directionList = directionList;
		this.context = context;
		this.layoutId = layoutId;
		// Log.wtf("#street", "" + streetNameList.size());

	}

	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		ViewHolderItem viewHolderItem = new ViewHolderItem();
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.street_row, parent, false);
			viewHolderItem.tv_street_length = (TextView) convertView
					.findViewById(R.id.tv_street_length);
			viewHolderItem.tv_street_name = (TextView) convertView
					.findViewById(R.id.tv_street_name);
			viewHolderItem.iv_direction = (ImageView) convertView
					.findViewById(R.id.iv_direction);

			convertView.setTag(viewHolderItem);
		} else
			viewHolderItem = (ViewHolderItem) convertView.getTag();
		viewHolderItem.tv_street_name.setText((position + 1) + ". "
				+ streetNameList.get(position));
		viewHolderItem.tv_street_length.setText(streetLengthList.get(position)
				+ "m");
		switch (directionList.get(position)) {
		case 0:
			viewHolderItem.iv_direction
					.setImageResource(R.drawable.straight_icon);
			break;
		case 1:
			viewHolderItem.iv_direction.setImageResource(R.drawable.left_icon);
			break;
		case 2:
			viewHolderItem.iv_direction.setImageResource(R.drawable.right_icon);
			break;
		}

		return convertView;
	}

	@Override
	public int getCount() {
		return directionList.size();
	}

	static class ViewHolderItem {
		TextView tv_street_name;
		TextView tv_street_length;
		ImageView iv_direction;

		public ViewHolderItem() {
		}
	}

}
