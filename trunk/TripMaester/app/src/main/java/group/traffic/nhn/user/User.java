package group.traffic.nhn.user;

import android.util.Log;

import com.facebook.GraphRequest;
import com.facebook.GraphRequest.GraphJSONArrayCallback;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.edu.hcmut.its.tripmaester.controller.manager.LoginManager;
import vn.edu.hcmut.its.tripmaester.service.http.HttpManager;

public class User {
	private String id;
	String name;
	String first_name;
	String last_name;
	String cover;
	String picture;
	String birthday;
	String email;
	String updated_time;
	String gender;
	String local;
	String verified;
	String timezone;
	String link;
	String imei;
	private String tokenId;
	private boolean status;
	private ArrayList<FriendItem> mFriends = new ArrayList<FriendItem>();
	
	public User(){
		this.id = "";
		this.name = "";
		this.first_name = "";
		this.cover = "";
		this.picture = "";
		this.last_name = "";
		this.birthday = "";
		this.email = "";
		this.updated_time = "";
		this.gender = "";
		this.local = "";
		this.verified = "";
		this.timezone = "";
		this.link = "";
		this.imei = "";
		this.setTokenId("");
		this.setStatus(false);
	}
	public User(String tokenId,String id, String name, String first_name, String last_name, String cover, String picture,
			String birthday, String email, String updated_time, String gender,
			String local, String verified, String timezone, String link,
			String imei, boolean status) {
		super();
		if(id != null){
			this.id = id;
		}
		if(name != null){
			this.name = name;
		}
		if(first_name != null){
			this.first_name = first_name;
		}
		if(last_name != null){
			this.last_name = last_name;
		}
		if(picture != null){
			this.picture = picture;
		}
		if(cover != null){
			this.cover = cover;
		}
		if(birthday != null){
			this.birthday = birthday;
		}
		if(email != null){
			this.email = email;
		}
		if(updated_time != null){
			this.updated_time = updated_time;
		}
		if(gender != null){
			this.gender = gender;
		}
		if(local != null){
			this.local = local;
		}
		if(verified != null){
			this.verified = verified;
		}
		if(timezone != null){
			this.timezone = timezone;
		}
		if(link != null){
			this.link = link;
		}
		if(imei != null){
			this.imei = imei;
		}
		if(tokenId != null){
			this.tokenId = tokenId;
		}
		this.setStatus(status);
	}

	public String getBirthday() {
		return birthday;
	}

	public String getEmail() {
		return email;
	}

	public String getFirst_name() {
		return first_name;
	}

	public ArrayList<FriendItem> getFriends() {
		return mFriends;
	}

	public String getGender() {
		return gender;
	}

	public String getId() {
		return id;
	}

	public String getImei() {
		return imei;
	}

	public String getLast_name() {
		return last_name;
	}

	public String getLink() {
		return link;
	}

	public String getPicture() {
		return picture;
	}

	public void setListFriend() {
		GraphRequest request = GraphRequest.newMyFriendsRequest(LoginManager.getInstance().getUserToken(), new GraphJSONArrayCallback() {

			@Override
			public void onCompleted(JSONArray objects, GraphResponse response) {
				//				ArrayList<FriendItem> mFriends = new ArrayList<FriendItem>();
				for (int i = 0; i < objects.length(); i++) {

					try {
						JSONObject friend = objects.getJSONObject(i);
						if (friend != null) {
							FriendItem item = new FriendItem(null, friend.getString("id"), false, friend.getString("name"));
							mFriends.add(item);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				Log.d("friend", String.valueOf(mFriends));

				if (objects.length() > 0) {
					// TODO: 12/26/15 thuanle fix this
					LoginManager.getInstance().getUser().setFriends(mFriends);
//					HttpManager.saveFriends();
				}
			}
		});
		request.executeAsync();
	}

	public String getLocal() {
		return local;
	}

	public String getName() {
		return name;
	}

	public String getTimezone() {
		return timezone;
	}

	public String getTokenId() {
		return tokenId;
	}

	public String getUpdated_time() {
		return updated_time;
	}

	public String getVerified() {
		return verified;
	}

	public boolean isStatus() {
		return status;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public void setFriends(ArrayList<FriendItem> mFriends) {
		this.mFriends = mFriends;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public void setUpdated_time(String updated_time) {
		this.updated_time = updated_time;
	}

	public void setVerified(String verified) {
		this.verified = verified;
	}

}
