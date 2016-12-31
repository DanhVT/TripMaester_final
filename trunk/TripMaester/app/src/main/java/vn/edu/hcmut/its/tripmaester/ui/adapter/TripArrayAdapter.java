package vn.edu.hcmut.its.tripmaester.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import group.traffic.nhn.message.MessageItem;
import group.traffic.nhn.message.MessageListAdapter;
import group.traffic.nhn.trip.TripsFragment;
import group.traffic.nhn.user.FriendItem;
import vn.edu.hcmut.its.tripmaester.R;
import vn.edu.hcmut.its.tripmaester.controller.ICallback;
import vn.edu.hcmut.its.tripmaester.controller.manager.LoginManager;
import vn.edu.hcmut.its.tripmaester.helper.ImageLoaderHelper;
import vn.edu.hcmut.its.tripmaester.model.Trip;
import vn.edu.hcmut.its.tripmaester.service.http.HttpManager;
import vn.edu.hcmut.its.tripmaester.ui.IMainScreen;

public class TripArrayAdapter extends ArrayAdapter<Trip> {
    private static final String TAG = TripArrayAdapter.class.getSimpleName();
    public IMainScreen mainActivity;
    public TripsFragment tripsFragment;
    boolean flag = false;
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Trip> mTrips;
    private View rowView;

    //    private ArrayList<MessageListAdapter> lstMessageAdapter = new ArrayList<MessageListAdapter>();
//    private Thread thread;
    private ViewGroup parentViewGroup;

    // private ListView mViewDialogContent;
    // private ListView mViewDialogContentShare;
    // private AlertDialog mDialog = null;

    public TripArrayAdapter(Context context, int resource,
                            ArrayList<Trip> trips) {
        super(context, resource, trips);
        this.mContext = context;
        this.mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.setTrips(trips);
    }

    public ArrayList<Trip> getTrips() {
        return mTrips;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        rowView = convertView;
        final ViewHolder viewHolder;
        parentViewGroup = parent;

        final int pos = position;
        if (null == convertView) {
            rowView = mInflater.inflate(R.layout.trip_item, null, false);

            viewHolder = new ViewHolder();
            viewHolder.txtTripName = (TextView) rowView.findViewById(R.id.txtTripName);
            viewHolder.txtUserNameCreateTrip = (TextView) rowView
                    .findViewById(R.id.txtUserNameCreateTrip);
            viewHolder.txtDateCreateTrip = (TextView) rowView
                    .findViewById(R.id.txtDateCreateTrip);
            viewHolder.imgAvaUserCreateTrip = (ImageView) rowView
                    .findViewById(R.id.imgAvaUserCreateTrip);
            viewHolder.txtTimeStartTrip = (TextView) rowView
                    .findViewById(R.id.txt_Item_list_trip_Time_Start_trip);
            viewHolder.txtTimeEndTrip = (TextView) rowView
                    .findViewById(R.id.txt_Item_list_trip_Time_End_trip);
            viewHolder.txtPlaceStartTrip = (TextView) rowView
                    .findViewById(R.id.txt_Item_list_trip_Place_Start_trip);
            viewHolder.txtPlaceEndTrip = (TextView) rowView
                    .findViewById(R.id.txt_Item_list_trip_Place_End_trip);
            viewHolder.txtNumberLikesTrip = (TextView) rowView
                    .findViewById(R.id.txt_item_list_trip_number_likes);
            viewHolder.likeButton = (TextView) rowView
                    .findViewById(R.id.likeButton);
            viewHolder.txtNumberCommentsTrip = (TextView) rowView
                    .findViewById(R.id.txt_item_list_trip_number_comments);
            viewHolder.txt_emotion = (TextView) rowView.findViewById(R.id.txt_emotion);


            rowView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        try{
            //KenK11 update trip's info
            Trip mTrip = getTrips().get(position);
            viewHolder.txtTripName.setText(mTrip.getTripName());
            viewHolder.txtUserNameCreateTrip.setText(mTrip.getUserName());
            viewHolder.txtDateCreateTrip.setText(mTrip.getDateTime());
            viewHolder.txtTimeStartTrip.setText(mTrip.getTimeStartTrip());
            viewHolder.txtTimeEndTrip.setText(mTrip.getTimeEndTrip());
            viewHolder.txtPlaceStartTrip.setText(mTrip.getPlaceStartTrip());
            Log.d("getPlaceStartTrip", mTrip.getPlaceStartTrip() );

            viewHolder.txtPlaceEndTrip.setText(mTrip.getPlaceEndTrip());

            viewHolder.txtNumberLikesTrip.setText(mTrip.getNumberLikeTrip());
            viewHolder.txtNumberCommentsTrip.setText(mTrip.getNumberCommentTrip());

            viewHolder.txt_emotion.setText(mTrip.getEmotion());

            if(mTrip.isUserLikeTrip(LoginManager.getInstance().getUser().getId())){
                viewHolder.likeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_thumb_up_light_blue_a700_24dp,0,0,0);
            }
            else{
                viewHolder.likeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like,0,0,0);
            }


//            viewHolder.imgAvaUserCreateTrip.setImageResource(mTrip.getLinkImage());
            ImageLoaderHelper.displayImage(mTrip.getLinkImage(), viewHolder.imgAvaUserCreateTrip );
            //share trip to friends
            LinearLayout share_button = (LinearLayout) rowView
                    .findViewById(R.id.item_list_trip_button_share);
            share_button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // convert list string to char sequence
                    // get all friend's user in app
                    final ArrayList<FriendItem> mFriends = LoginManager.getInstance().getUser().getFriends();
                    // get list friend not share of trip

                    // get list shared friend of trip
                    HttpManager.getShareOnTrip(getTrips().get(pos).getTripId(), getContext(), new ICallback<ArrayList<FriendItem>>() {
                        @Override
                        public void onCompleted(ArrayList<FriendItem> lstFriendShared, Object tag, Exception e) {
                            if (e == null) {
                                final ArrayList<FriendItem> mListFriendsNotShare = new ArrayList<>();
                                for (int i = 0; i < mFriends.size(); i++) {
                                    if (lstFriendShared.size() > 0) {
                                        boolean isAddShareList = true;
                                        for (int j = 0; j < lstFriendShared.size(); j++) {
                                            if (mFriends.get(i).getFBId()
                                                    .equals(lstFriendShared.get(j).getFBId())) {
                                                isAddShareList = false;
                                                break;
                                            }
                                        }
                                        if (isAddShareList) {
                                            mListFriendsNotShare.add(mFriends.get(i));
                                        }
                                    } else {
                                        mListFriendsNotShare.add(mFriends.get(i));
                                    }
                                }
                                List<String> listItems = new ArrayList<String>();
                                for (int i = 0; i < mListFriendsNotShare.size(); i++) {
                                    listItems.add(mListFriendsNotShare.get(i).getFriendName());
                                }
                                final CharSequence[] charSequenceItems = listItems
                                        .toArray(new CharSequence[listItems.size()]);

                                new AlertDialog.Builder(parentViewGroup.getContext())
                                        .setTitle("SHARE")
                                        .setMultiChoiceItems(
                                                charSequenceItems,
                                                //TODO: TL: WTF IS THIS?
                                                new boolean[]{false, false, false, false, false, false, false},
                                                new DialogInterface.OnMultiChoiceClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialog,
                                                            int whichButton,
                                                            boolean isChecked) {

                                                        mListFriendsNotShare.get(whichButton).setChecked(isChecked);
                                                    }
                                                })
                                        .setPositiveButton(R.string.alert_dialog_share,
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialog,
                                                            int whichButton) {

                                                        // call share api
                                                        List<String> lstUserSharedId = new ArrayList<String>();
                                                        for (int i = 0; i < mListFriendsNotShare.size(); i++) {
                                                            if (mListFriendsNotShare.get(i).isChecked()) {
                                                                lstUserSharedId
                                                                        .add(mListFriendsNotShare
                                                                                .get(i)
                                                                                .getFBId());
                                                            }
                                                        }
                                                        HttpManager.saveShareTrip(
                                                                lstUserSharedId, getTrips().get(pos).getTripId(),
                                                                getContext(),
                                                                new ICallback<JSONObject>() {
                                                                    @Override
                                                                    public void onCompleted(JSONObject data, Object tag, Exception e) {
                                                                        if (e != null) {
                                                                            Log.e(TAG, "saveShareTrip", e);
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                })
                                        .setNegativeButton(R.string.alert_dialog_cancel,
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialog,
                                                            int whichButton) {

										/* User clicked No so do some stuff */
                                                    }
                                                }).show();

                            } else {
                                Log.e(TAG, "Can not getShareOnTrip", e);
                            }
                        }
                    });// mTrips.get(position).getLstFriendsShared();


                    // show multi-checkboxes dialog
                }
            });
            // ================

            //like button
            final LinearLayout likeButton = (LinearLayout) rowView.findViewById(R.id.item_list_trip_button_like);

            likeButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String strLike = getTrips().get(pos).getNumberLikeTrip();
                    String[] temp = strLike.split(" ");
                    if (temp.length > 1) {
                        int trip = Integer.parseInt(temp[0]);
//                    if(likeButton.getResources().getDrawable())
//                        if(Use){
//
//                        }

                        if(getTrips().get(pos).isUserLikeTrip(LoginManager.getInstance().getUser().getId())){
                            trip--;
                            viewHolder.likeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like,0,0,0);
                        }
                        else{
                            trip++;
                            viewHolder.likeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_thumb_up_light_blue_a700_24dp,0,0,0);
                        }

                        String tmpStr10 = String.valueOf(trip);
                        viewHolder.txtNumberLikesTrip.setText(tmpStr10 + " likes");

                        HttpManager.likeTrip(getTrips().get(pos).getTripId(), getContext());
//                        getTrips().get(pos).setNumberLikeTrip(String.valueOf(trip) + " likes");
                    }

                }
            });

            // button commnent click
            LinearLayout comment_button = (LinearLayout) rowView.findViewById(R.id.item_list_trip_button_comment);
            comment_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View comment_dialog = mInflater.inflate(R.layout.comment_dialog, null);
                    final ListView mViewDialogContent = (ListView) comment_dialog.findViewById(R.id.context_menu_comment);

                    ArrayList<MessageItem> lstMessage = new ArrayList<MessageItem>();
                    final MessageListAdapter mMessageDetailAdapter = new MessageListAdapter(mContext, lstMessage);

                    mViewDialogContent.setAdapter(mMessageDetailAdapter);
                    tripsFragment.loadListMessage(mMessageDetailAdapter, pos);
                    Button btn_comment = (Button) comment_dialog.findViewById(R.id.btn_comment);

                    final EditText txt_comment = (EditText) comment_dialog
                            .findViewById(R.id.txt_comment);

                    btn_comment.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // FIXME: Service to run in background
                            String content = txt_comment.getText().toString();
                            HttpManager.commentTrip(getTrips().get(pos)
                                    .getTripId(), content, getContext(), new ICallback<JSONObject>() {
                                @Override
                                public void onCompleted(JSONObject data, Object tag, Exception e) {
                                    if (e == null) {

                                    } else {
                                        Log.e(TAG, "Cannot comment trip", e);
                                    }
                                }
                            });
//
//						ArrayList<MessageItem> lstMessage = HttpManager
//								.getListCommentTrip(getTrips().get(pos)
//										.getTripId());
//
//						mMessageDetailAdapter.setmFriends(lstMessage);
                        }
                    });

                    AlertDialog.Builder alter = new AlertDialog.Builder(
                            parentViewGroup.getContext());
                    alter.setTitle(R.string.title_friend_msg);

                    // FIXME: Load trip message
//				loadMessageOfOneUser(pos, lstMessage);
                    HttpManager.getListCommentTrip(mContext, getTrips().get(pos).getTripId(), new ICallback<ArrayList<MessageItem>>() {
                        @Override
                        public void onCompleted(ArrayList<MessageItem> data, Object tag, Exception e) {
                            mMessageDetailAdapter.setmFriends(data);
                        }
                    });

                    // comment_dialog = mInflater.inflate(
                    // R.layout.comment_dialog, null);
                    // if (!flag) {
                    alter.setView(comment_dialog).setNegativeButton(
                            R.string.alert_dialog_close,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    tripsFragment.stopTimer();
                                }
                            });
                    flag = false;
                    AlertDialog mDialog = alter.create();
                    mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    mViewDialogContent.setAdapter(mMessageDetailAdapter); //Change DANH_VO

                    mMessageDetailAdapter.notifyDataSetChanged();

                    mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    mDialog.show();
                    // }

                }
            });
            // ================
            //TODO: KenK11 change LinearLayout to Button
            // View route of trip on map
            LinearLayout viewOnMap = (LinearLayout) rowView
                    .findViewById(R.id.item_list_trip_button_view);
            viewOnMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HttpManager.getListPointOnTrip(getTrips().get(pos).getTripId(), getContext(), new ICallback<ArrayList<GeoPoint>>() {
                        @Override
                        public void onCompleted(ArrayList<GeoPoint> waypoints, Object tag, Exception e) {
                            if (waypoints.size() > 0) {
                                getTrips().get(pos).setLstWayPoints(waypoints);
                                ArrayList<Road> lstRoads = new ArrayList<Road>();
                                Road road1 = new Road(getTrips().get(pos).getLstWayPoints());
                                lstRoads.add(road1);
                                mainActivity.changeToMapFragments(getTrips().get(pos));
                            } else {
                                new AlertDialog.Builder(parentViewGroup.getContext())
                                        .setTitle("View Trip On Map Error")
                                        .setMessage("No trip's road data.")
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // do nothing
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }
                            // ================

                        }
                    });


                }
            });
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
        return rowView;
    }

    private void loadMessageOfOneUser(int position,
                                      ArrayList<MessageItem> lstMessage) {
        // lstMessage =
        // HttpManager.getListCommentTrip(mTrips.get(position).getTripId());
        // mMessageDetailAdapter = new MessageListAdapter(mContext,lstMessage);

    }

    public void setTrips(ArrayList<Trip> mTrips) {
        this.mTrips = mTrips;
    }

    private class ViewHolder {
        TextView txtTripName;
        TextView txtUserNameCreateTrip;
        TextView txtDateCreateTrip;
        TextView txtTimeStartTrip;
        TextView txtTimeEndTrip;
        TextView txtPlaceStartTrip;
        TextView txtPlaceEndTrip;
        TextView txtNumberLikesTrip;
        TextView txtNumberCommentsTrip;
        ImageView imgAvaUserCreateTrip;
        TextView likeButton;
        TextView txt_emotion;
    }
}
