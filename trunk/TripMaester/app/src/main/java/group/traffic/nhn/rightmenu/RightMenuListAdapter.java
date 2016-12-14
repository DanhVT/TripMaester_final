package group.traffic.nhn.rightmenu;

import vn.edu.hcmut.its.tripmaester.R;

import java.util.ArrayList;


import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RightMenuListAdapter extends BaseAdapter{
	private LayoutInflater mInflater;
	private ArrayList<RightMenuItem> mMenuItems;
	private TypedArray mMenuIcons;
	private ArrayList<RightMenuItem> createItems(String [] itemsText){
		ArrayList<RightMenuItem> result = new ArrayList<>();
		 
    	for(String strItem : itemsText){
    		RightMenuItem item = new RightMenuItem();
    		item.setIcon(R.array.left_menu_icons);
    		item.setTitle(strItem);
    		result.add(item);
    	}
    	return result;
    }

	public RightMenuListAdapter(Context context, String[] items, TypedArray icons) {
		super();
		Context mContext = context;
		this.mMenuItems =  createItems(items);
		this.mMenuIcons = icons;
		mInflater = LayoutInflater.from(mContext);
	}
	
		
	@Override
	 public int getCount() {
		 int count = 0;
		 if (mMenuItems != null) {
			 count = mMenuItems.size();
		 }
		 return count;
	 }
	 
	@Override
	 public Object getItem(int pos) {
	 	return  null == mMenuItems ? null : mMenuItems.get(pos);
	 }
	 
	@Override
	 public long getItemId(int position) {
		return 0;
	 }
	 
	@Override
	 public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.drawer_right_menu_item, parent, false);
		}
	 
		ImageView menuicon = (ImageView)convertView.findViewById(R.id.right_menu_item_icon);
		TextView menutext = (TextView)convertView.findViewById(R.id.right_menu_item_text);
		TextView menucounter = (TextView)convertView.findViewById(R.id.right_menu_item_counter);
		
		RightMenuItem itemNavigation = mMenuItems.get(position);
		 
		menuicon.setImageResource(mMenuIcons.getResourceId(position, -1));
		menutext.setText(itemNavigation.getTitle());
		
		if(itemNavigation.isCounterVisible()){
			menucounter.setText(itemNavigation.getCount());
		}else{
			menucounter.setVisibility(View.GONE);
		}
		return convertView;
		 
	 }
}
