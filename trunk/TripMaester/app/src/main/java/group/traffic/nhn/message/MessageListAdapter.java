package group.traffic.nhn.message;

import vn.edu.hcmut.its.tripmaester.R;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageListAdapter extends BaseAdapter{
	private LayoutInflater mInflater;
	private Context mContext;
	private ArrayList<MessageItem> mMessages;
	private ArrayList<String> lstFbIdUsed = new ArrayList<String>();
	private ArrayList<Bitmap> lstBitmapFbId = new ArrayList<Bitmap>();
	
	public MessageListAdapter(Context context, ArrayList<MessageItem> friends) {
		super();
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.setmFriends(friends);
	}
	
	@Override
	 public int getCount() {
		 int count = 0;
		 if (getmFriends() != null) {
			 count = getmFriends().size();
		 }
		 return count;
	 }
	 
	@Override
	 public Object getItem(int pos) {
	 	return  null == getmFriends() ? null : getmFriends().get(pos);
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

		try{
			Bitmap user_fb_icon = null;
			String user_fb_id = mMessages.get(position).getFbIdAva();
			
			
			if(findFbAvatar(user_fb_id) != null){
				user_fb_icon = findFbAvatar(user_fb_id);
			}else{
				
				URL image_value = new URL("https://graph.facebook.com/"+user_fb_id+"/picture" );
				InputStream input_stream = (InputStream) image_value.getContent();
				user_fb_icon = BitmapFactory.decodeStream(input_stream);
				lstBitmapFbId.add(user_fb_icon);
				lstFbIdUsed.add(user_fb_id);
			}
			
			ImageView msgicon = (ImageView)convertView.findViewById(R.id.message_list_item_icon);
			TextView msgtext = (TextView)convertView.findViewById(R.id.message_list_item_text);
			TextView msgcount = (TextView)convertView.findViewById(R.id.message_list_item_count);
			TextView msgdate = (TextView)convertView.findViewById(R.id.message_list_item_date);
		
			MessageItem item = getmFriends().get(position);
			 
			msgicon.setImageBitmap(user_fb_icon);
			msgtext.setText(item.getContent());
			if(item.isNew()){
				msgtext.setTextSize(14);
			}
			msgcount.setText(""+item.getNumber());
			msgdate.setText(item.getDate());
		}catch(Exception ex){
			ex.printStackTrace();
		}

		 
		return convertView;
		 
	 }

	public ArrayList<MessageItem> getmFriends() {
		return mMessages;
	}

	public void setmFriends(ArrayList<MessageItem> mMes) {
		this.mMessages = mMes;
	}

	public ArrayList<String> getLstFbIdUsed() {
		return lstFbIdUsed;
	}

	public void setLstFbIdUsed(ArrayList<String> lstFbIdUsed) {
		this.lstFbIdUsed = lstFbIdUsed;
	}

	public ArrayList<Bitmap> getLstBitmapFbId() {
		return lstBitmapFbId;
	}

	public void setLstBitmapFbId(ArrayList<Bitmap> lstBitmapFbId) {
		this.lstBitmapFbId = lstBitmapFbId;
	}
	
	public Bitmap findFbAvatar(String fbId){
		Bitmap bitmap = null;
		
		for(int i =0;i < lstFbIdUsed.size();i++){
			if(lstFbIdUsed.get(i).compareTo(fbId) == 0){
				bitmap = lstBitmapFbId.get(i);
			}
		}
		
		return bitmap;
	}
}

