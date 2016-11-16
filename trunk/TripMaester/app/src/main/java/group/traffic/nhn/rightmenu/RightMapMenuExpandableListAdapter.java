package group.traffic.nhn.rightmenu;

import java.util.ArrayList;

import vn.edu.hcmut.its.tripmaester.R;
import group.traffice.nhn.common.StaticVariable;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * right menu of map
 * @author Vo tinh
 *
 */
public class RightMapMenuExpandableListAdapter extends BaseExpandableListAdapter {

//	private ArrayList<RightMenuGroup> mGroups;
	public LayoutInflater mInflater;
	private Context mContext;

	/**
	 * load menu content
	 */
	public RightMapMenuExpandableListAdapter(Context context, ArrayList<RightMenuGroup> groups) {
		this.mContext = context;
//		this.mGroups = groups; // is null, do not use
		this.mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return StaticVariable.GROUP_MAP_MENU_ITEMS.get(groupPosition).children.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition, 
			boolean isLastChild, View convertView, final ViewGroup parent) {
		
		final String children = (String) getChild(groupPosition, childPosition);
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.drawer_right_map_menu_item, null);
		}
		
		TextView menuItemText = (TextView) convertView.findViewById(R.id.right_map_menu_item_text);
		ImageView menuItemImage = (ImageView) convertView.findViewById(R.id.right_map_menu_item_icon);
		ImageView menuItemCheck = (ImageView) convertView.findViewById(R.id.right_map_menu_item_check);
		
		//set Text menu item
		menuItemText.setText(children);
		
		//load image
		switch (groupPosition){
			case 0:
				menuItemImage.setImageResource(getImageVehicle(childPosition));
				break;
			case 1:
				menuItemImage.setImageResource(getImageOfAlgorithm(childPosition));
			break;
		}
		
		convertView.setClickable(false);

		
		menuItemCheck.setImageResource(R.drawable.ic_check_detail);
		menuItemCheck.setVisibility(View.INVISIBLE);
		
		if (childPosition == StaticVariable.GROUP_MAP_MENU_ITEMS.get(groupPosition).check_index) {
			if (menuItemCheck.getVisibility() == View.VISIBLE)
				menuItemCheck.setVisibility(View.INVISIBLE);
			else if (menuItemCheck.getVisibility() == View.INVISIBLE)
				menuItemCheck.setVisibility(View.VISIBLE);
		}

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return StaticVariable.GROUP_MAP_MENU_ITEMS.get(groupPosition).children.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return StaticVariable.GROUP_MAP_MENU_ITEMS.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return StaticVariable.GROUP_MAP_MENU_ITEMS.size();
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
			convertView = mInflater.inflate(R.layout.drawer_right_map_menu, null);
		}
		
		RightMenuGroup group = (RightMenuGroup) getGroup(groupPosition);

		TextView menuItemText = (TextView) convertView.findViewById(R.id.right_map_menu_group_text);
		ImageView menuItemIcon = (ImageView) convertView.findViewById(R.id.right_map_menu_group_icon);
		ImageView menuItemCheck = (ImageView) convertView.findViewById((R.id.right_map_menu_group_check));		
		
		menuItemText.setText(group.text);
		
		menuItemCheck.setVisibility(View.VISIBLE);
		switch(groupPosition){
			case 0:
			case 1:
				menuItemCheck.setVisibility(View.INVISIBLE);
				convertView.setActivated(false);
				if(isExpanded)
					menuItemIcon.setImageResource(R.drawable.ic_down);
				else 
					menuItemIcon.setImageResource(R.drawable.ic_up);
				break;
			case 2:
				menuItemIcon.setImageResource(R.drawable.ic_micro);
				break;
			case 3:
				menuItemIcon.setImageResource(R.drawable.ic_navigate);
				break;
			case 4:
				menuItemIcon.setImageResource(R.drawable.ic_map);
				break;
			case 5:
				menuItemIcon.setImageResource(R.drawable.ic_warning);
				break;
		}
		
		if(groupPosition > 1){
			if(group.check)
				menuItemCheck.setImageResource(R.drawable.ic_check);
			else 
				menuItemCheck.setImageResource(R.drawable.ic_uncheck);
		}

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
	
	/**
	 * get image of vehicle
	 * @param position
	 * @return
	 */
	private int getImageVehicle(int position){
		int result = -1;
		switch(position){
		case 0:
			result = R.drawable.ic_car;
			break;
		case 1:
			result = R.drawable.ic_motorbike;
			break;
		case 2: 
			result = R.drawable.ic_bus;
			break;
		case 3: 
			result = R.drawable.ic_truck;
			break;
		default:
			result = R.drawable.ic_login;
		}
		return result;
	}
	
	/**
	 * get image of algorithm
	 * @param position
	 * @return
	 */
	private int getImageOfAlgorithm(int position){
		int result = -1;
		switch(position){
		case 0:
			result = R.drawable.ic_polyline;
			break;
		case 1:
			result = R.drawable.ic_time;
			break;
		case 2: 
			result = R.drawable.ic_polyline;
			break;
		case 3: 
			result = R.drawable.ic_wont;
			break;
		default:
			result = R.drawable.ic_warning;
		}
		return result;
	}
}