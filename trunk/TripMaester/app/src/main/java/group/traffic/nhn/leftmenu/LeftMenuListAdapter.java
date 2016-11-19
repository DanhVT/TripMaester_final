package group.traffic.nhn.leftmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import vn.edu.hcmut.its.tripmaester.R;

public class LeftMenuListAdapter extends BaseAdapter{
	private LayoutInflater mInflater;
	private Context mContext;
	private ArrayList<LeftMenuItem> mMenuItems;
	private TypedArray mMenuIcons;
	private ArrayList<LeftMenuItem> createItems(String [] itemsText){
		ArrayList<LeftMenuItem> result = new ArrayList<LeftMenuItem>();
		 
    	for(String strItem : itemsText){
    		LeftMenuItem item = new LeftMenuItem();
    		item.setIcon(R.array.left_menu_icons);
    		item.setTitle(strItem);
    		result.add(item);
    	}
    	return result;
    }

	public LeftMenuListAdapter(Context context, String[] items, TypedArray icons) {
		super();
		this.mContext = context;
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
			convertView = mInflater.inflate(R.layout.drawer_left_menu_item, parent, false);
		}
	 
		ImageView menuicon = (ImageView)convertView.findViewById(R.id.left_menu_item_icon);
		TextView menutext = (TextView)convertView.findViewById(R.id.left_menu_item_text);
		TextView menuTextCount = (TextView)convertView.findViewById(R.id.left_menu_item_counter);
		
		LeftMenuItem itemNavigation = mMenuItems.get(position);
		 
		menuicon.setImageResource(mMenuIcons.getResourceId(position, -1));
		menutext.setText(itemNavigation.getTitle());
		
		// displaying count
        // check whether it set visible or not
        if(mMenuItems.get(position).isCounterVisible()){
        	menuTextCount.setText(itemNavigation.getCount());
        }else{
        	// hide the counter view
        	menuTextCount.setVisibility(View.GONE);
        }
        
		 
		return convertView;
		 
	 }
}
