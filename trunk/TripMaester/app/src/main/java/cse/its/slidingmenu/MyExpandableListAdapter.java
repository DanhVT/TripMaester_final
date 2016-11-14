package cse.its.slidingmenu;

import vn.edu.hcmut.its.tripmaester.R;
import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author SinhHuynh
 * @Tag This expandable list adapter helps to handle menu list
 */
public class MyExpandableListAdapter extends BaseExpandableListAdapter {

	private final SparseArray<Group> groups;
	public LayoutInflater inflater;
	public Activity activity;

	public MyExpandableListAdapter(Activity act, SparseArray<Group> groups) {
		activity = act;
		this.groups = groups;
		inflater = act.getLayoutInflater();
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return groups.get(groupPosition).children.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, final ViewGroup parent) {
		final String children = (String) getChild(groupPosition, childPosition);
		TextView tv_detail = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_row_details, null);
		}
		tv_detail = (TextView) convertView.findViewById(R.id.tv_detail);
		tv_detail.setText(children);
		
		ImageView iv_detail = (ImageView) convertView.findViewById(R.id.iv_detail);
		if(groupPosition == 0){
			switch(childPosition){
			case 0:
				iv_detail.setImageResource(R.drawable.car);
				break;
			case 1:
				iv_detail.setImageResource(R.drawable.motorbike);
				break;
			case 2:
				iv_detail.setImageResource(R.drawable.bus);
				break;
			case 3:
				iv_detail.setImageResource(R.drawable.truck);
				break;
			}
		}
		if(groupPosition == 1){
			switch(childPosition){
			case 0:
				iv_detail.setImageResource(R.drawable.polyline);
				break;
			case 3:
				iv_detail.setImageResource(R.drawable.time);
				break;
			default:
				iv_detail.setImageResource(R.drawable.document);
				break;
			} 
		}
		if(groupPosition == 2){
			if(childPosition == 0) 
				iv_detail.setImageResource(R.drawable.english);
			else 
				iv_detail.setImageResource(R.drawable.vietnam);
		}
		
		convertView.setClickable(false);

		ImageView iv_check = (ImageView) convertView
				.findViewById(R.id.iv_check_detail);
		iv_check.setImageResource(R.drawable.check_detail);
		iv_check.setVisibility(View.INVISIBLE);
		if (childPosition == groups.get(groupPosition).check_index) {
			if (iv_check.getVisibility() == View.VISIBLE)
				iv_check.setVisibility(View.INVISIBLE);
			else if (iv_check.getVisibility() == View.INVISIBLE)
				iv_check.setVisibility(View.VISIBLE);
		}

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return groups.get(groupPosition).children.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.drawer_list_item, null);
		}
		Group group = (Group) getGroup(groupPosition);

		TextView tv_itle = (TextView) convertView.findViewById(R.id.title);
		ImageView iv_icon = (ImageView) convertView.findViewById(R.id.icon);
		ImageView iv_check = (ImageView) convertView.findViewById((R.id.iv_check));
		
		iv_check.setVisibility(View.INVISIBLE);
		//Set up check box for menu items
		if(groupPosition > 2){
			iv_check.setVisibility(View.VISIBLE);
			if(group.check)
				iv_check.setImageResource(R.drawable.ic_check);
			else 	iv_check.setImageResource(R.drawable.ic_uncheck);
		}
		if (groupPosition < 3) {	
			convertView.setActivated(false);
			if(isExpanded)
				iv_icon.setImageResource(R.drawable.ic_up);
			else iv_icon.setImageResource(R.drawable.ic_down);
		}
		switch(groupPosition){
			case 3:
				iv_icon.setImageResource(R.drawable.ic_micro);
				break;
			case 4:
				iv_icon.setImageResource(R.drawable.ic_navigate);
				break;
			case 5:
				iv_icon.setImageResource(R.drawable.ic_map);
				break;
			case 6:
				iv_icon.setImageResource(R.drawable.ic_warning);
				break;
		}
		

		tv_itle.setText(group.text);

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}