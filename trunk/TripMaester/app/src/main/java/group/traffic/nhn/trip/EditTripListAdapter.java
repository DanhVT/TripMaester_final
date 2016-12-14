package group.traffic.nhn.trip;

import vn.edu.hcmut.its.tripmaester.R;

import java.util.ArrayList;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EditTripListAdapter extends BaseAdapter{
//public class EditTripListAdapter extends ArrayAdapter<TripItem4EditList>{
	private LayoutInflater mInflater;
	private ArrayList<TripItem4EditList> mTrips;
	
	public EditTripListAdapter(Context context, int layout, ArrayList<TripItem4EditList> trips) {
//		super(context, layout, trips);
		Context mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.mTrips = trips;
	}
	
		
	@Override
	 public int getCount() {
		 int count = 0;
		 if (mTrips != null) {
			 count = mTrips.size();
		 }
		 return count;
	 }
	
	@Override
	 public long getItemId(int position) {
		return 0;
	 }
	 
	@Override
	 public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.trip_edit_item, null, false);
		}
	 
		ImageView icon = (ImageView)convertView.findViewById(R.id.list_edit_img_trip);
		TextView tripname = (TextView)convertView.findViewById(R.id.edit_trip_name);
		TextView createddate = (TextView)convertView.findViewById(R.id.edit_trip_created_date);
		
		TextView startdate = (TextView)convertView.findViewById(R.id.edit_trip_start_date);
		TextView finishdate = (TextView)convertView.findViewById(R.id.edit_trip_finish_date);
		
		TextView startplace = (TextView)convertView.findViewById(R.id.edit_trip_start_point);
		TextView finishplace = (TextView)convertView.findViewById(R.id.edit_trip_finish_point);
	
		TripItem4EditList item = mTrips.get(position);
		 
//		icon.setImageResource(item.getIcon());
		tripname.setText(item.getmTripName());
		createddate.setText(item.getmCreatedDate());
			
		startdate.setText(item.getmStartDate());
		finishdate.setText(item.getmFinishDate());
		
		startplace.setText(item.getmStartPlace());
		finishplace.setText(item.getmFinishPlace());
		
		return convertView;
		 
	 }


	@Override
	public Object getItem(int position) {
		return mTrips.get(position);
	}
}
