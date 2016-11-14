package vn.edu.hcmut.its.tripmaester.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequest.GraphJSONArrayCallback;
import com.facebook.GraphResponse;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.facebook.share.widget.GameRequestDialog;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import group.traffic.nhn.user.FriendItem;
import vn.edu.hcmut.its.tripmaester.R;
import vn.edu.hcmut.its.tripmaester.controller.manager.LoginManager;
import vn.edu.hcmut.its.tripmaester.ui.adapter.FriendListAdapter;

public class FriendsFragment extends Fragment {

    private static final int URL_LOADER = 0;
    //    String deviceId = "" ;
    final String tag = "Your Logcat tag: ";
    //TODO: TL: This should be convert to COMMON CONSTANTS
    String URL = "http://www.traffic.hcmut.edu.vn/";
    String result = "";
    private ListView mListFriend;
    private ArrayList<FriendItem> mFriends;
    private FriendListAdapter mListAdapter;
    //	boolean isLoadDone = false;
    private ArrayList<FriendItem> friends;
    private Button btn_Invite;
    private ProgressDialog dialog;
    private CallbackManager callbackManager;
    private GameRequestDialog requestDialog;

    public FriendsFragment() {
    }

    public void addListenerOnButton(View view) {

        btn_Invite = (Button) view.findViewById(R.id.btn_invite);
        btn_Invite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //App Invite
                String appLinkUrl;

                //TODO: TL: PLEASE CHECK THIS
                appLinkUrl = "https://fb.me/402147653306033";
                // previewImageUrl = "https://www.mydomain.com/my_invite_image.jpg";

                if (AppInviteDialog.canShow()) {
                    AppInviteContent content = new AppInviteContent.Builder()
                            .setApplinkUrl(appLinkUrl)
                                    // .setPreviewImageUrl(previewImageUrl)
                            .build();
                    AppInviteDialog.show(getActivity(), content);
                }

                //GameInvite
//	            GameRequestContent content = new GameRequestContent.Builder()
//                .setMessage("Come play this level with me")
//                .build();
//	            requestDialog.show(content);
            }
        });

    }

    //TODO: TL: DO NOT CALL VIA THIS
    public void callWebService(String q) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet request = new HttpGet(URL + q);
        //request.addHeader("deviceId", deviceId);
        ResponseHandler<String> handler = new BasicResponseHandler();
        try {
            result = httpclient.execute(request, handler);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        httpclient.getConnectionManager().shutdown();
        Log.i(tag, result);
    } // end callWebService()

    /**
     * load fiends
     *
     * @return
     */
    private ArrayList<FriendItem> getFriendsOfUser() {
        mFriends = new ArrayList<FriendItem>();

        if (LoginManager.getInstance().isLogin()) {
            dialog = new ProgressDialog(this.getActivity());
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage(this.getString(R.string.login_wait_msg));
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            GraphRequest request = GraphRequest.newMyFriendsRequest(LoginManager.getInstance().getUserToken(), new GraphJSONArrayCallback() {

                @Override
                public void onCompleted(JSONArray objects, GraphResponse response) {
                    for (int i = 0; i < objects.length(); i++) {

                        try {
                            JSONObject friend = objects.getJSONObject(i);
                            if (friend != null) {
                                FriendItem item = new FriendItem(getActivity(), "", friend.getString("id"), false, friend.getString("name"));
                                mFriends.add(item);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if (objects.length() > 0) {

                        mListAdapter.loadData(mFriends);
                        LoginManager.getInstance().getUser().setFriends(mFriends);
                        mListAdapter.notifyDataSetChanged();
                    }

                    dialog.cancel();
                }
            });
            request.executeAsync();
        }

//		FriendItem item1 = new FriendItem(getActivity(),"Thanh Van", R.drawable.ic_user_profile, true);
//		FriendItem item2 = new FriendItem(getActivity(),"Tran Ha", R.drawable.ic_user_profile, true);
//		FriendItem item3 = new FriendItem(getActivity(),"Tien Nguyen", R.drawable.ic_user_profile, false);
//		FriendItem item4 = new FriendItem(getActivity(),"Hai Anh", R.drawable.ic_user_profile, true);
//		FriendItem item5 = new FriendItem(getActivity(),"Viet Thang", R.drawable.ic_user_profile, false);
//		FriendItem item6 = new FriendItem(getActivity(),"Hoang Nguyen", R.drawable.ic_user_profile, false);
//		FriendItem item7 = new FriendItem(getActivity(),"Tien Cua", R.drawable.ic_user_profile, false);
//		FriendItem item8 = new FriendItem(getActivity(),"Than Dong", R.drawable.ic_user_profile, false);
//
//		mFriends.add(item1);
//		mFriends.add(item2);
//		mFriends.add(item3);
//		mFriends.add(item4);
//		mFriends.add(item5);
//		mFriends.add(item6);
//		mFriends.add(item7);
//		mFriends.add(item8);
        return mFriends;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();
        requestDialog = new GameRequestDialog(this);
        requestDialog.registerCallback(callbackManager, new FacebookCallback<GameRequestDialog.Result>() {
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }

            public void onSuccess(GameRequestDialog.Result rs) {
                String id = rs.getRequestId();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);

        mListFriend = (ListView) rootView.findViewById(R.id.list_friends);
        friends = getFriendsOfUser();

        mListAdapter = new FriendListAdapter(this.getActivity().getApplicationContext(), friends);
        mListFriend.setAdapter(mListAdapter);
//        mListFriend.setOnItemClickListener(new FriendItemClickListener());

        mListFriend.setFocusable(false);
        addListenerOnButton(rootView);
//        getLoaderManager().initLoader(URL_LOADER, null,this);

        return rootView;
    }

    private void reloadAllData(ArrayList<FriendItem> listFriends) {
        mListAdapter = new FriendListAdapter(this.getActivity().getApplicationContext(), listFriends);
        // fire the event
        mListAdapter.notifyDataSetChanged();
    }

//	
//	private class FriendItemClickListener implements OnItemClickListener{
//		private LayoutInflater inflater = null;
//		private Button mBtnOkie;
//		private Button mBtnCancel;
//		private View mView;
//		private EditText mEditText;
//		private AlertDialog  mDialog = null;
//		private FriendItem item = null;
//		
//		public FriendItemClickListener(){
////			init();
//		}
//		private void init(){
//			inflater = LayoutInflater.from(getActivity().getApplicationContext());
//			mView = inflater.inflate(R.layout.layout_friend_invate_install, null);
//			mEditText = (EditText)mView.findViewById(R.id.txt_message);
//			mBtnOkie = (Button)mView.findViewById(R.id.btn_msg_okie);
//			mBtnCancel = (Button)mView.findViewById(R.id.btn_msg_cancel);
//			
//			mBtnOkie.setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					//TODO: implement
//					if(null != mDialog)
//						mDialog.dismiss();
//					
//					if(null != item){
//						mListAdapter.notifyDataSetChanged();
//					}
//				}
//			});
//			
//			mBtnCancel.setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					if(null != mDialog)
//						mDialog.dismiss();
//				}
//			});
//			
//		}
//		private void showMessageDialog(FriendItem friend){
//			init();
//			if(null != inflater) {
//				
//				mEditText.setText(friend.getContent() + getActivity().getResources().getString(R.string.msg_default));
//				friend.setInvitemessage(""+ getActivity().getResources().getString(R.string.msg_invited));
//				
//				AlertDialog.Builder alter = new AlertDialog.Builder(getActivity());
//				alter.setTitle(R.string.title_friend_msg);
//				alter.setView(mView);
//				mDialog = alter.create();
//				mDialog.show();
//			}
//			
//
//		}
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//			item = mFriends.get(position);
//			if(null != item && item.isInviteVisible())
//				showMessageDialog(item);
//		}
//		
//		
//	}


}

