package group.traffic.nhn.message;

import vn.edu.hcmut.its.tripmaester.R;

import java.util.ArrayList;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MessageDetailAdapter extends BaseAdapter{
	private LayoutInflater mInflater;
	private ArrayList<MessageItem> mFriends;
	
	public MessageDetailAdapter(Context context, ArrayList<MessageItem> friends) {
		super();
		Context mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.mFriends = friends;
	}
	
		
	@Override
	 public int getCount() {
		 int count = 0;
		 if (mFriends != null) {
			 count = mFriends.size();
		 }
		 return count;
	 }
	 
	@Override
	 public Object getItem(int pos) {
	 	return  null == mFriends ? null : mFriends.get(pos);
	 }
	 
	@Override
	 public long getItemId(int position) {
		return 0;
	 }
	 
	@Override
	 public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.message_list_item, null, false);
		}
	 
		ImageView msgicon = (ImageView)convertView.findViewById(R.id.message_list_item_icon);
		TextView msgtext = (TextView)convertView.findViewById(R.id.message_list_item_text);
		RelativeLayout layout = (RelativeLayout)convertView.findViewById(R.id.layout_message_count_date);
	
		MessageItem item = mFriends.get(position);
		 
		msgicon.setImageResource(item.getIcon());
		msgtext.setText(item.getContent());
		
		if(null != layout){
			layout.setVisibility(View.GONE);
		}
		
		 
		return convertView;
		 
	 }
}
