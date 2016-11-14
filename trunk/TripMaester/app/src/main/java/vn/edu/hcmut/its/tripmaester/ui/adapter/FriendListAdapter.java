package vn.edu.hcmut.its.tripmaester.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import vn.edu.hcmut.its.tripmaester.R;
import group.traffic.nhn.user.FriendItem;

public class FriendListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<FriendItem> mFriends;

    public FriendListAdapter(Context context, ArrayList<FriendItem> friends) {
        super();
        this.mContext = context;
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
        return null == mFriends ? null : mFriends.get(pos);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.friend_list_item, null, false);
            }

            FriendItem item = mFriends.get(position);

            //set permision to get picture
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);


            String user_fb_id = item.getFBId();
            URL image_value = new URL("https://graph.facebook.com/" + user_fb_id + "/picture");
            InputStream input_stream = (InputStream) image_value.getContent();
            Bitmap user_fb_icon = BitmapFactory.decodeStream(input_stream);

            ImageView menuicon = (ImageView) convertView.findViewById(R.id.friend_item_icon);

            TextView menutext = (TextView) convertView.findViewById(R.id.friend_item_text);
            TextView menuTextCount = (TextView) convertView.findViewById(R.id.friend_item_invite);

            menuicon.setImageBitmap(user_fb_icon);
//		menuicon.setImageResource(item.getIcon());
            menutext.setText(item.getFriendName());

            // displaying count
            // check whether it set visible or not
            if (item.isInviteVisible()) {
                menuTextCount.setText(item.getInvitemessage());

            } else {
                // hide the counter view
                menuTextCount.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            Log.i("error", ex.getMessage());
        }

        return convertView;

    }

    public void loadData(ArrayList<FriendItem> lstFriends) {
        mFriends = lstFriends;
    }
}

