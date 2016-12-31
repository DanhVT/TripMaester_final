package vn.edu.hcmut.its.tripmaester.service.http;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import group.traffic.nhn.map.MapFragment;
import group.traffic.nhn.message.MessageItem;
import group.traffic.nhn.trip.PointItem;
import group.traffic.nhn.user.FriendItem;
import group.traffic.nhn.user.User;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import vn.edu.hcmut.its.tripmaester.R;
import vn.edu.hcmut.its.tripmaester.controller.ICallback;
import vn.edu.hcmut.its.tripmaester.controller.manager.LoginManager;
import vn.edu.hcmut.its.tripmaester.helper.ApiCall;
import vn.edu.hcmut.its.tripmaester.model.Trip;
import vn.edu.hcmut.its.tripmaester.utility.GraphicUtils;

import static group.traffic.nhn.map.MapFragment.MEDIA_TYPE_VIDEO;
import static group.traffic.nhn.map.MapFragment.TYPE_TEXT;
import static group.traffic.nhn.map.MapFragment.mMapView;
import static vn.edu.hcmut.its.tripmaester.helper.CameraHelper.MEDIA_TYPE_IMAGE;
import static vn.edu.hcmut.its.tripmaester.ui.activity.MainActivity.storageRef;

// TODO: 12/18/15 THUANLE: TO BE REMOVED
@Deprecated
public class HttpManager {
    static final String URL_GET_LIKE_INFO_TRIP = HttpConstants.HOST_NAME + "/ITS/rest/like/GetListLikerOnTrip";
    static final String URL_CREATE_POINT_TRIP = HttpConstants.HOST_NAME + "/ITS/rest/tripdetails/CreatePointOnTrip";
    static final String URL_CREATE_TRIP = HttpConstants.HOST_NAME + "/ITS/rest/trip/CreateTrip";// tokenID/[list
    static final String URL_GET_COMMENTS_TRIP = HttpConstants.HOST_NAME + "/ITS/rest/comment/GetListCommentOnTrip";
    static final String URL_GET_FRIENDS = HttpConstants.HOST_NAME + "/ITS/rest/friend/GetListFriend";
    static final String URL_GET_LIST_POINT_ON_TRIP = HttpConstants.HOST_NAME + "/ITS/rest/tripdetails/GetListPointOnTrip";
    static final String URL_GET_LIST_SHARE_TRIP = HttpConstants.HOST_NAME + "/ITS/rest/trip/GetListShareTrip";
    static final String URL_GET_LIST_TRIP = HttpConstants.HOST_NAME + "/ITS/rest/trip/GetListTrip";
    static final String URL_GET_LIST_PRIVATE_TRIP = HttpConstants.HOST_NAME + "/ITS/rest/trip/GetListPrivateTrip";
    static final String URL_GET_LIST_PUBLIC_TRIP = HttpConstants.HOST_NAME + "/ITS/rest/trip/GetListPublicTrip";
    static final String URL_GET_LIST_TRP_SEARCH = HttpConstants.HOST_NAME + "/ITS/rest/trip/GetListTripSearch";
    static final String URL_GET_LOGIN = HttpConstants.HOST_NAME + "/ITS/rest/user/GETlogin/";
    static final String URL_GET_SHARE_TRIP = HttpConstants.HOST_NAME + "/ITS/rest/share/GetShareOnTrip";
    static final String URL_GET_TRIP_INFO = HttpConstants.HOST_NAME + "/ITS/rest/trip/GetTripInfo";
    static final String URL_GET_USER_INFO = HttpConstants.HOST_NAME + "/ITS/rest/user/GetUserInfo";
    static final String URL_LIKE_TRIP = HttpConstants.HOST_NAME + "/ITS/rest/like/LikeTrip";
    static final String URL_LOGIN = HttpConstants.HOST_NAME + "/ITS/rest/user/Login";
    static final String URL_LOGOUT = HttpConstants.HOST_NAME + "/ITS/rest/user/Logout";
    static final String URL_SAVE_FRIENDS = HttpConstants.HOST_NAME + "/ITS/rest/friend/SaveListFriend";
    static final String URL_SAVE_SHARE_TRIP = HttpConstants.HOST_NAME + "/ITS/rest/share/SaveShareOnTrip";
    private static final String TAG = HttpManager.class.getName();
    private static OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    //TripComment trip
    /*
        + url: /ITS/rest/comment/SaveTripComment
		+ IRequest data: { tokenId: "...", tripId: "...", content:"..."}
		+ Response data: {code:“...”, description:“...”}
		moved to request
	 */
    @Deprecated
    public static void commentTrip(String tripID, String content, Context context, final ICallback<JSONObject> callback) {
        Ion.with(context).load(CommentTripController.URL_SAVE_COMMENT_TRIP)
                .setBodyParameter("tokenId", LoginManager.getInstance().getUser().getTokenId())
                .setBodyParameter("content", content)
                .setBodyParameter("tripId", tripID)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String str_response) {
                        if (e == null) {
                            try {
                                callback.onCompleted(new JSONObject(str_response), null, null);
                            } catch (JSONException e1) {
                                callback.onCompleted(null, null, e1);
                            }
                        } else {
                            callback.onCompleted(null, null, e);
                        }
                    }
                });
    }

    //count like trip
    /*
        + url:
		+ IRequest data: { tokenId: "...", tripId:"..."}
		+ Response data:
	 */
    public static void getLikeInfoOnTrip(String tripID, Context context, final ICallback<JSONObject> callback) {
        Ion.with(context).load(URL_GET_LIKE_INFO_TRIP)
                .setBodyParameter("tokenId", LoginManager.getInstance().getUser().getTokenId())
                .setBodyParameter("tripId", tripID)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String str_response) {
                        if (e == null) {
                            try {
                                callback.onCompleted(new JSONObject(str_response), null, null);

                            } catch (Exception ex) {
                                callback.onCompleted(null, null, ex);
                            }
                        } else {
                            callback.onCompleted(null, null, e);
                        }
                    }
                });

    }

    //create point on trip
    /*
     * + url:
	   + IRequest data: { tokenId: "ed16a4cd-1627-43c1-a6b3-9b3b925742ea", tripId: "317e5cc8-b2e4-48e5-93e3-8efaa4a02cfe", x:"a0"
		, y: "a0", z:"a0", fromZ:"a0", pointDescription:"a0",order: "a0"}
	   + Response data:
	 */
    public static void createPointOnTrip(String tripId, PointItem point, Context context, final ICallback<JSONObject> callback) {
        Ion.with(context).load(URL_CREATE_POINT_TRIP)
                .setBodyParameter("tokenId", LoginManager.getInstance().getUser().getTokenId())
                .setBodyParameter("tripId", tripId)
                .setBodyParameter("x", String.valueOf(point.getX_Lat()))
                .setBodyParameter("y", String.valueOf(point.getY_Long()))
                .setBodyParameter("z", String.valueOf(point.getZ()))
                .setBodyParameter("fromZ", String.valueOf(point.getFromZ()))
                .setBodyParameter("pointDescription", point.getPointDescription())
                .setBodyParameter("order", point.getOrder())
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        try {
                            JSONObject response_json = new JSONObject(result);
                            callback.onCompleted(response_json, null, null);
                        } catch (JSONException e1) {
                            callback.onCompleted(null, null, e1);
                        }
                    }
                });
    }

    //create trip
    /*
     * + url: /ITS/rest/trip/CreateTrip
	   + IRequest data: { tokenId: "...", startTime: "...", endTime:"...", from: "...", to: "..."}
	   + Response data: {code:“...”, description:“...”}
	 */
    public static void createTrip(Trip trip_item, Context context, final ICallback<JSONObject> callback) {
        Ion.with(context).load(URL_CREATE_TRIP)
                .setBodyParameter("tokenId", LoginManager.getInstance().getUser().getTokenId())
                .setBodyParameter("tripName", trip_item.getTripName())
                .setBodyParameter("startTime", trip_item.getTimeStartTrip())
                .setBodyParameter("endTime", trip_item.getTimeEndTrip())
                .setBodyParameter("fromLocationName", trip_item.getPlaceStartTrip())
                .setBodyParameter("toLocationName", trip_item.getPlaceEndTrip())
                .setBodyParameter("dateTime", trip_item.getDateTime())//span time of trip
                .setBodyParameter("privacy", trip_item.getPrivacy())
                .setBodyParameter("emotion", trip_item.getEmotion())
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e == null) {
                            try {
                                callback.onCompleted(new JSONObject(result), null, e);
                            } catch (JSONException e1) {
                                callback.onCompleted(null, null, e1);
                            }
                        } else {
                            callback.onCompleted(null, null, e);
                        }
                    }
                });
    }

    public static void getListCommentTrip(Context context, String tripId, final ICallback<ArrayList<MessageItem>> callback ) {        // http://traffic.hcmut.edu.vn/ITS/rest/user/login

        Ion.with(context).load(URL_GET_COMMENTS_TRIP)
                .setBodyParameter("tokenId", LoginManager.getInstance().getUser().getTokenId())
                .setBodyParameter("tripId", tripId)
                .asString()
                .setCallback(new FutureCallback<String>() {
                                 @Override
                                 public void onCompleted(Exception e, String str_response) {
                                     JSONObject jsonObj = null;
                                     ArrayList<MessageItem> lstMessages = new ArrayList<MessageItem>();
                                     try {
                                         jsonObj = new JSONObject(str_response);
                                         JSONArray response_json;

                                         if (!jsonObj.isNull("listComment")) {
                                             response_json = new JSONArray(jsonObj.getString("listComment"));
                                             for (int i = 0; i < response_json.length(); i++) {
                                                 JSONObject jObj = new JSONObject(response_json.getString(i));
                                                 if (null != jObj) {
                                                     MessageItem item = new MessageItem(jObj.getString("content"), R.drawable.user1, true, 10, jObj.getString("userId"), jObj.getString("dateTime"));
                                                     lstMessages.add(item);
                                                 }
                                             }
                                         }
                                         for (int i = 0; i < lstMessages.size() - 1; i++) {
                                             Date d1 = cse.its.helper.Utilities.convertStringToDateTime(lstMessages.get(i).getDate());
                                             if (d1 != null) {
                                                 for (int j = i + 1; j < lstMessages.size(); j++) {
                                                     Date d2 = cse.its.helper.Utilities.convertStringToDateTime(lstMessages.get(j).getDate());
                                                     if (d2.after(d1)) {
                                                         MessageItem tempMessegeItem = lstMessages.get(i);
                                                         lstMessages.set(i, lstMessages.get(j));
                                                         lstMessages.set(j, tempMessegeItem);
                                                     }
                                                 }
                                             }
                                         }

                                     } catch (JSONException e1) {
                                         e1.printStackTrace();
                                     }
                                     callback.onCompleted(lstMessages, null, e);
                                 }
                             });

            // && !jsonObj.isNull("shareList")){
            //arrange the list according to dateTime
    }

    //get list friend
    /*
	 * + url:
	   + IRequest data:
	   + Response data:
	 */
    public static JSONArray getListFriend() {
        JSONArray response_json = new JSONArray();
        try {
            MultipartBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("tokenId", LoginManager.getInstance().getUser().getTokenId())
                    .build();
            String str_response = null;

            str_response = ApiCall.POST(client, URL_GET_FRIENDS, requestBody);

            JSONObject jsonObj = new JSONObject(str_response);
            if (!jsonObj.isNull("listFriend")) {
                response_json = new JSONArray(jsonObj.getString("listFriend"));
            }
            // if (response_json.isNull("tokenID")) {
            // String tokenId = response_json.get("tokenID").toString();
            // StaticVariable.user.setTokenId(tokenId);
            // }
            // if (response_json.isNull("status")) {
            // boolean status = response_json.getBoolean("status");
            // StaticVariable.user.setStatus(status);
            // }
        } catch (Exception ex) {
            Log.i(TAG, ex.getMessage());
        }
        return response_json;
    }

    public static void getListPointOnTrip(String tripId, Context context, final ICallback<ArrayList<GeoPoint>> callback) {
        if(context !=null) {
            Ion.with(context).load(URL_GET_LIST_POINT_ON_TRIP)
                    .setBodyParameter("tokenId", LoginManager.getInstance().getUser().getTokenId())
                    .setBodyParameter("tripId", tripId)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String str_response) {
                            final ArrayList<GeoPoint> lstGeoPoints = new ArrayList<GeoPoint>();
                            try {
                                JSONArray listPoint = new JSONArray();
                                JSONObject jsonObj = new JSONObject(str_response);
                                if (!jsonObj.isNull("listPoint")) {
                                    listPoint = new JSONArray(jsonObj.getString("listPoint"));
                                }
                                Log.d("listPoint", String.valueOf(listPoint.length()));
                                MapFragment.listMarkerTrip = new ArrayList<MapFragment.MyMarker>();

                                for (int i = 0; i < listPoint.length(); i++) {
                                    Log.d("listPoint", String.valueOf(i));
                                    JSONObject pointJson = new JSONObject(listPoint.getString(i));
                                    if (!pointJson.isNull("x") && !pointJson.isNull("y")) {
                                        double x = Double.valueOf(pointJson.getString("x"));
                                        double y = Double.valueOf(pointJson.getString("y"));

                                        GeoPoint geoPoint = new GeoPoint(x, y);

                                        lstGeoPoints.add(geoPoint);

                                        if (!jsonObj.isNull("listMedia")) {
                                            JSONArray listMedia = new JSONArray(pointJson.getString("listMedia"));
                                            for (int j = 0; j < listMedia.length(); j++) {
                                                JSONObject imgJSon = new JSONObject(listMedia.getString(i));

                                                MapFragment.MyMarker startMarker = new MapFragment.MyMarker(mMapView);

                                                startMarker.getMarker().setPosition(geoPoint);
                                                startMarker.setData(imgJSon.getString("url"));
                                                startMarker.setType(Integer.parseInt(imgJSon.getString("type")));
                                                startMarker.setIndex(j);

                                                MapFragment.listMarkerTrip.add(startMarker);
                                            }
                                        }

                                    }

                                }
                            } catch (Exception ex) {
                                Log.e(TAG, ex.getMessage());
                            }

                            callback.onCompleted(lstGeoPoints, null, e);
                        }
                    });
        }
    }

    /*
	 * + IRequest data (in json): { userId: "...", name: "...", frist_name:"...", last_name:"...",birthday:"...", email:"...", update_time:"...",gender:"...", local:"...", verified:"...", timezone:"...", link:"...", imei:"..."}
 	   + Response data: {tokenId:“...”, status:“...”
	 */
    public static void getListPrivateTrip(Context context, final ICallback<JSONArray> callback) {
        if(context !=null) {
            Ion.with(context).load(URL_GET_LIST_PRIVATE_TRIP)
                    .setBodyParameter("tokenId", LoginManager.getInstance().getUser().getTokenId())
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String str_response) {
                            if (e == null) {
                                JSONObject jsonObj = null;
                                try {
                                    jsonObj = new JSONObject(str_response);
                                    if (!jsonObj.isNull("listPrivateTrip")) {

                                        callback.onCompleted(new JSONArray(jsonObj.getString("listPrivateTrip")), null, null);
                                    }
                                } catch (JSONException e1) {
                                    callback.onCompleted(null, null, e1);
                                }
                            } else {
                                callback.onCompleted(null, null, e);
                            }
                        }
                    });
        }
    }

    public static void getListPublicTrip(Context context, final ICallback<JSONArray> callback) {
        if(context !=null) {
            Ion.with(context).load(URL_GET_LIST_PUBLIC_TRIP)
                    .setBodyParameter("tokenId", LoginManager.getInstance().getUser().getTokenId())
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String str_response) {
                            if (e == null) {
                                JSONObject jsonObj = null;
                                try {
                                    jsonObj = new JSONObject(str_response);
                                    if (!jsonObj.isNull("listTrip")) {
                                        callback.onCompleted(new JSONArray(jsonObj.getString("listTrip")), null, null);
                                    }
                                } catch (JSONException e1) {
                                    callback.onCompleted(null, null, e1);
                                }
                            } else {
                                callback.onCompleted(null, null, e);
                            }
                        }
                    });
        }
    }


    public static void searchTrip(String startPlace, String endPlace, String privacy, final ICallback<JSONArray> callback){
        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("tokenId", LoginManager.getInstance().getUser().getTokenId())
                .addFormDataPart("startPlace", startPlace)
                .addFormDataPart("endPlace", endPlace)
                .addFormDataPart("privacy", privacy)
                .build();
        try {
            String str_response  = ApiCall.POST(client, URL_GET_LIST_TRP_SEARCH, requestBody);
            JSONObject jsonObj = new JSONObject(str_response);
            try {
                if (!jsonObj.isNull("listTrip")) {
                    callback.onCompleted(new JSONArray(jsonObj.getString("listTrip")), null, null);
                }
            }
            catch(Exception ex) {
                callback.onCompleted(null, null, ex);
                ex.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    // service get List share Trip
	/*
	 * + IRequest data (in json): { userId: "...", name: "...", frist_name:"...", last_name:"...",birthday:"...", email:"...", update_time:"...",gender:"...", local:"...", verified:"...", timezone:"...", link:"...", imei:"..."}
 	   + Response data: {tokenId:“...”, status:“...”
	 */
    public static void getListSharedTrip(Context context, final ICallback<JSONArray> callback) {
        if(context !=null) {
            Ion.with(context).load(URL_GET_LIST_SHARE_TRIP)
                    .setBodyParameter("tokenId", LoginManager.getInstance().getUser().getTokenId())
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String str_response) {
                            if (e == null) {
                                JSONArray response_json = new JSONArray();
                                try {
                                    JSONObject jsonObj = new JSONObject(str_response);
                                    if (!jsonObj.isNull("listTrip")) {
                                        Log.d("listTrip ", jsonObj.getString("listTrip"));
                                        response_json = new JSONArray(jsonObj.getString("listTrip"));

                                        callback.onCompleted(response_json, null, null);
                                    }
                                } catch (Exception ex) {
                                    Log.i(TAG, ex.getMessage());
                                    callback.onCompleted(null, null, ex);
                                }
                            } else {
                                callback.onCompleted(null, null, e);
                            }

                        }
                    });
        }
    }

    // service get List Trip
	/*
	 * + IRequest data (in json): { userId: "...", name: "...", frist_name:"...", last_name:"...",birthday:"...", email:"...", update_time:"...",gender:"...", local:"...", verified:"...", timezone:"...", link:"...", imei:"..."}
 	   + Response data: {tokenId:“...”, status:“...”
	 */
    public static void getListTrip(Context context, final ICallback<JSONArray> callback) {
        if(context !=null) {
            Ion.with(context).load(URL_GET_LIST_TRIP)
                    .setBodyParameter("tokenId", LoginManager.getInstance().getUser().getTokenId())
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String str_response) {
                            if (e == null) {
                                JSONObject jsonObj = null;
                                try {
                                    jsonObj = new JSONObject(str_response);
                                    if (!jsonObj.isNull("listTrip")) {
                                        callback.onCompleted(new JSONArray(jsonObj.getString("listTrip")), null, null);
                                    }
                                } catch (JSONException e1) {
                                    callback.onCompleted(null, null, e1);
                                }
                            } else {
                                callback.onCompleted(null, null, e);
                            }
                        }
                    });
        }
    }

    // service get login
	/*
	 * + url:/ITS/rest/user/GETlogin/{userId}/{name}/{first_name}/{last_name}/ {birthday}/{email}/{update_time}/{gender}/{local}/{verified}/{timezone}/{link}/{imei}
	   + Response data: {tokenId:“...”, status:“...”
	 */
    public static JSONObject getLogin(User user) {
        JSONObject response_json = new JSONObject();
        String http_get_string = URL_GET_LOGIN + "/" + user.getId() + "/" + user.getName() + "/" + user.getFirst_name() +
                "/" + user.getLast_name() + "/" + user.getBirthday() + "/" + user.getEmail()
                + "/" + user.getUpdated_time() + "/" + user.getGender() + "/" + user.getLocal()
                + "/" + user.getVerified() + "/" + user.getTimezone() + "/" + user.getLink()
                + "/" + user.getImei();

        try {

            HttpConnection connection = new HttpConnection();
            connection.doGet(http_get_string);
            String str_response = connection.getContentAsString();

            response_json = new JSONObject(str_response);
            // if (response_json.isNull("tokenID")) {
            // String tokenId = response_json.get("tokenID").toString();
            // StaticVariable.user.setTokenId(tokenId);
            // }
            // if (response_json.isNull("status")) {
            // boolean status = response_json.getBoolean("status");
            // StaticVariable.user.setStatus(status);
            // }
        } catch (Exception ex) {
            Log.i(TAG, ex.getMessage());
        }
        return response_json;
    }

    //get share on trip
	/*
	 * + url:
	   + IRequest data:
	   + Response data:
	 */
    public static void getShareOnTrip(String tripId, Context context, final ICallback<ArrayList<FriendItem>> callback) {
        Ion.with(context).load(URL_GET_SHARE_TRIP)
                .setBodyParameter("tokenId", LoginManager.getInstance().getUser().getTokenId())
                .setBodyParameter("tripId", tripId)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String str_response) {
                        if (e == null) {
                            try {
                                ArrayList<FriendItem> lstShareTrip = new ArrayList<>();
                                JSONArray jsonObjArray = new JSONArray(str_response);  //TODO: Danhbkit
                                for (int i = 0; i < jsonObjArray.length(); i++) {
                                    JSONObject jObj = new JSONObject(jsonObjArray.getString(i));

                                    lstShareTrip.add(new FriendItem(null, jObj.getString("userId"), false, ""));
                                }
                                callback.onCompleted(lstShareTrip, null, null);
                            } catch (JSONException e1) {
                                callback.onCompleted(null, null, e1);
                            }
                            //FIXME: parse array shareList
                        } else {
                            callback.onCompleted(null, null, e);
                        }
                    }
                });
    }

    //get trip info
	/*
		+ url: /ITS/rest/trip/GetTripInfo
		+ IRequest data: { tokenId: "...", tripId: "..."}
		+ Response data:
	 */
    public static JSONObject getTripInfo(String tripID, String tokenId) {
        JSONObject response_json = new JSONObject();
        try {
            MultipartBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("tokenId", LoginManager.getInstance().getUser().getTokenId())
                    .addFormDataPart("tripId", tripID)
                    .build();
            String str_response = null;

            str_response = ApiCall.POST(client, URL_GET_TRIP_INFO, requestBody);
            JSONObject jsonObj = new JSONObject(str_response);

            // if (response_json.isNull("tokenID")) {
            // String tokenId = response_json.get("tokenID").toString();
            // StaticVariable.user.setTokenId(tokenId);
            // }
            // if (response_json.isNull("status")) {
            // boolean status = response_json.getBoolean("status");
            // StaticVariable.user.setStatus(status);
            // }
        } catch (Exception ex) {
            Log.i(TAG, ex.getMessage());
        }
        return response_json;

    }
//    1040954309383084

//    10fd6aca-8cc3-44e0-9850-cbbb9040d1a4
    // service get user info
	/*
	 * + IRequest data (in json): { userId: "...", name: "...", frist_name:"...", last_name:"...",birthday:"...", email:"...", update_time:"...",gender:"...", local:"...", verified:"...", timezone:"...", link:"...", imei:"..."}
 	   + Response data: {tokenId:“...”, status:“...”
	 */
    public static void getUserInfo(String userId, Context context, final ICallback<JSONObject> callback) {
        Ion.with(context).load(URL_GET_USER_INFO)
                .setBodyParameter("userId", userId)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String str_response) {
                        if (e == null) {
                            try {
                                callback.onCompleted(new JSONObject(str_response), null, e);
                            } catch (Exception ex) {
                                callback.onCompleted(null, null, ex);
                            }
                        } else {
                            callback.onCompleted(null, null, e);
                        }
                    }
                });
    }

    //like trip
	/*
		+ url: /ITS/rest/like/LikeTrip
		+ IRequest data: { tokenId: "...", tripId:"..."}
		+ Response data: {code:“...”, description:“...”}
	 */
    public static void likeTrip(String tripID, Context context) {
        Ion.with(context).load(URL_LIKE_TRIP)
            .setBodyParameter("tokenId", LoginManager.getInstance().getUser().getTokenId())
            .setBodyParameter("tripId", tripID)
            .asString()
            .setCallback(new FutureCallback<String>() {
                @Override
                public void onCompleted(Exception e, String str_response) {
                    if (e != null) {
                        Log.e(TAG, "likeTrip", e);
                    }
                }
            });
    }

    // service login
    /*
     * + IRequest data (in json): { userId: "...", name: "...", frist_name:"...", last_name:"...",birthday:"...", email:"...", update_time:"...",gender:"...", local:"...", verified:"...", timezone:"...", link:"...", imei:"..."}
 	   + Response data: {tokenId:“...”, status:“...”
	 */
    @Deprecated
    public static void login(Context context, final ICallback<JSONObject> callback) {
        JSONObject response_json = new JSONObject();
        try {
            Ion.with(context).load(URL_LOGIN)
<<<<<<< Updated upstream
                    .setBodyParameter("name", LoginManager.getInstance().getUser().getName())
                    .setBodyParameter("userId", LoginManager.getInstance().getUser().getId())
                    .setBodyParameter("firstName", LoginManager.getInstance().getUser().getFirst_name())
                    .setBodyParameter("lastName", LoginManager.getInstance().getUser().getLast_name())
                    .setBodyParameter("birthday", LoginManager.getInstance().getUser().getBirthday())
                    .setBodyParameter("email", LoginManager.getInstance().getUser().getEmail())
                    .setBodyParameter("updatedime", LoginManager.getInstance().getUser().getUpdated_time())
                    .setBodyParameter("gender", LoginManager.getInstance().getUser().getGender())
                    .setBodyParameter("local", LoginManager.getInstance().getUser().getLocal())
                    .setBodyParameter("verified", LoginManager.getInstance().getUser().getVerified())
                    .setBodyParameter("link", LoginManager.getInstance().getUser().getPicture())
                    .setBodyParameter("timezone", LoginManager.getInstance().getUser().getTimezone())
                    .setBodyParameter("imei", LoginManager.getInstance().getUser().getImei())
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String str_response) {
                            if (e == null) {
                                try {
                                    Log.d("loginTest", str_response);
                                    callback.onCompleted(new JSONObject(str_response), null, e);
                                } catch (Exception ex) {
                                    callback.onCompleted(null, null, ex);
                                }
                            } else {
                                callback.onCompleted(null, null, e);
=======
                .setBodyParameter("name", LoginManager.getInstance().getUser().getName())
                .setBodyParameter("userId", LoginManager.getInstance().getUser().getId())
                .setBodyParameter("firstName", LoginManager.getInstance().getUser().getFirst_name())
                .setBodyParameter("lastName", LoginManager.getInstance().getUser().getLast_name())
                .setBodyParameter("birthday", LoginManager.getInstance().getUser().getBirthday())
                .setBodyParameter("email", LoginManager.getInstance().getUser().getEmail())
                .setBodyParameter("updatedime", LoginManager.getInstance().getUser().getUpdated_time())
                .setBodyParameter("gender", LoginManager.getInstance().getUser().getGender())
                .setBodyParameter("local", LoginManager.getInstance().getUser().getLocal())
                .setBodyParameter("verified", LoginManager.getInstance().getUser().getVerified())
                .setBodyParameter("link", LoginManager.getInstance().getUser().getPicture())
                .setBodyParameter("timezone", LoginManager.getInstance().getUser().getTimezone())
                .setBodyParameter("imei", LoginManager.getInstance().getUser().getImei())
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String str_response) {
                        if (e == null) {
                            try {
                                callback.onCompleted(new JSONObject(str_response), null, e);
                            } catch (Exception ex) {
                                callback.onCompleted(null, null, ex);
>>>>>>> Stashed changes
                            }
                        } else {
                            callback.onCompleted(null, null, e);
                        }
                    }
                });
        } catch (Exception ex) {
            Log.d("loginTest", "Login fail");
            Log.i(TAG+"/Login", ex.getMessage());
        }

    }

    // service logout
    /*
	 * + IRequest data (in json):
 	   + Response data:
	 */
    @Deprecated
    public static JSONObject logout() {
        JSONObject response_json = new JSONObject();
        try {
            MultipartBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("tokenId", LoginManager.getInstance().getUser().getTokenId())
                    .build();

            String str_response = ApiCall.POST(client, URL_LOGOUT, requestBody);

            response_json = new JSONObject(str_response);
        } catch (Exception ex) {
            Log.i(TAG, ex.getMessage());
        }
        return response_json;
    }

    //save friend
	/*
	 * + url:
	   + IRequest data:
	   + Response data:
	 */
    //FIXME: correct
    public static JSONObject saveFriends(List<String> lstFriendId) {
        JSONObject response_json = new JSONObject();
        try {
            // http://traffic.hcmut.edu.vn/ITS/rest/user/login
            String json = "{'tokenId':'"+LoginManager.getInstance().getUser().getTokenId()+"',"
                    +"'fromSocialNetwork':'"+LoginManager.getInstance().getUser().getTokenId()+"'"
                    +"'listFriend':'"+lstFriendId+"'}";
            RequestBody body = RequestBody.create(JSON, json);

            Request request = new Request.Builder()
                    .url(URL_SAVE_FRIENDS)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();

            response_json = new JSONObject( response.body().string());
            // if (response_json.isNull("tokenID")) {
            // String tokenId = response_json.get("tokenID").toString();
            // StaticVariable.user.setTokenId(tokenId);
            // }
            // if (response_json.isNull("status")) {
            // boolean status = response_json.getBoolean("status");
            // StaticVariable.user.setStatus(status);
            // }
        } catch (Exception ex) {
            Log.i(TAG, ex.getMessage());
        }
        return response_json;
    }

    //save share on trip
	/*
	 * + url: 
	   + IRequest data:
	   + Response data: 
	 */
    public static void saveShareTrip(List<String> lstUserIdShare, String tripId, Context context, final ICallback<JSONObject> callback) {
        JSONArray arraySharedId = new JSONArray();
        for (int i = 0; i < lstUserIdShare.size(); i++) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("userId", lstUserIdShare.get(i));
            arraySharedId.put(jsonObject.toString());
        }
        Ion.with(context).load(URL_SAVE_SHARE_TRIP)
                .setBodyParameter("tokenId", LoginManager.getInstance().getUser().getTokenId())
                .setBodyParameter("tripId", tripId)
                .setBodyParameter("shareList", arraySharedId.toString())
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String str_response) {
                        if (e == null) {
                            try {
                                callback.onCompleted(new JSONObject(str_response), null, null);
                            } catch (JSONException e1) {
                                callback.onCompleted(null, null, e1);
                            }
                        } else {
                            callback.onCompleted(null, null, e);
                        }
                    }
                });
    }

    /*
    * KenK11
    * POST: Upload image
    * filename
    * pointID
    * tokenID
    * dataImage : image convert to string base64
    * */

    //TODO: DANHVT - CHANGE TO OKHTTP _ NOT CHECK
    public static JSONObject uploadImage(File file, String fileName, String MIME, String pointId) {
        String URL_UPLOAD = "http://traffic.hcmut.edu.vn/ITS/rest/upload/UploadImageToPoint";
        String POINT_ID = pointId;
        MediaType MEDIA_TYPE = MediaType.parse(MIME);
        String format = MIME.split("/")[1];
        JSONObject responseJson = new JSONObject();
        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("filename", fileName)
                .addFormDataPart("pointId", POINT_ID)
                .addFormDataPart("tokenId", LoginManager.getInstance().getUser().getTokenId())
                .addFormDataPart("dataImage", fileName+"."+format, RequestBody.create(MEDIA_TYPE, file))
                .build();
        String str_response = null;
        try {
            str_response = ApiCall.POST(client, URL_UPLOAD, requestBody);
            responseJson = new JSONObject(str_response);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseJson;
    }

    public static void uploadImageToServer(final Context context,final String filePath, final String pointId, final ICallback<JSONObject> callback) {
        final String URL_UPLOAD = "http://traffic.hcmut.edu.vn/ITS/rest/upload/UploadImageStringToPoint";
            File f  = new File(filePath);
//                String content_type  = getMimeType(f.getPath());
        String encryptFile = null;

        encryptFile = GraphicUtils.convertImage2Base64(filePath);
        Log.e("base64", String.valueOf(encryptFile.length()));

        int idx = filePath.replaceAll("\\\\", "/").lastIndexOf("/");
        String fileName = idx >= 0 ? filePath.substring(idx + 1) : filePath;

        Ion.with(context).load(URL_UPLOAD)
                .setBodyParameter("filename", fileName)
                .setBodyParameter("pointId", pointId)
                .setBodyParameter("type", String.valueOf(MEDIA_TYPE_IMAGE))
                .setBodyParameter("tokenId", LoginManager.getInstance().getUser().getTokenId())
                .setBodyParameter("dataImage", encryptFile)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String str_response) {
                        if (e == null) {
                            try {
                                callback.onCompleted(new JSONObject(str_response), null, null);
                            } catch (JSONException e1) {
                                callback.onCompleted(null, null, e1);
                            }
                        } else {
                            callback.onCompleted(null, null, e);
                        }
                    }
                });
    }

    public static void uploadVideoToServer(final Context context,final String linkDownload, final String pointId, final ICallback<JSONObject> callback){
        final String URL_UPLOAD = "http://traffic.hcmut.edu.vn/ITS/rest/upload/UploadVideoLinkToPoint";

        Ion.with(context).load(URL_UPLOAD)
                .setBodyParameter("pointId", pointId)
                .setBodyParameter("type", String.valueOf(MEDIA_TYPE_VIDEO))
                .setBodyParameter("tokenId", LoginManager.getInstance().getUser().getTokenId())
                .setBodyParameter("downloadLink", linkDownload)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String str_response) {
                        if (e == null) {
                            try {
                                callback.onCompleted(new JSONObject(str_response), null, null);
                            } catch (JSONException e1) {
                                callback.onCompleted(null, null, e1);
                            }
                        } else {
                            callback.onCompleted(null, null, e);
                        }
                    }
                });
    }

    public static void uploadVideoToFireBag(String filePath, final String pointId, final Context context){
        Log.d("RunApp", "At Httpmain");
        Uri file = Uri.fromFile(new File(filePath));
        StorageReference riversRef = storageRef.child("video/"+file.getLastPathSegment());
        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Log.d("Firebase", downloadUrl.getPath());
                        uploadVideoToServer(context, downloadUrl.getPath(), pointId, new ICallback<JSONObject>() {
                            @Override
                            public void onCompleted(JSONObject data, Object tag, Exception e) {
                                if (e != null || data == null){
                                    Log.e(TAG,"Error when create trip",e);
                                }
                                Log.d("uploadLink","Upload Video Link to Server");
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }
    public static void uploadTextRate( final Context context, final String data, final String pointId, final ICallback<JSONObject> callback ){
        final String URL_UPLOAD = "http://traffic.hcmut.edu.vn/ITS/rest/upload/UploadTextRateToPoint";
            Ion.with(context).load(URL_UPLOAD)
                .setBodyParameter("tokenId", LoginManager.getInstance().getUser().getTokenId())
                .setBodyParameter("pointId", pointId)
                .setBodyParameter("type", String.valueOf(TYPE_TEXT))
                .setBodyParameter("data", data)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String str_response) {
                    if (e == null) {
                        try {
                            callback.onCompleted(new JSONObject(str_response), null, null);
                        } catch (JSONException e1) {
                            callback.onCompleted(null, null, e1);
                        }
                    } else {
                        callback.onCompleted(null, null, e);
                    }
                    }
                });
    }

//    @Deprecated
//    public static InputStream sendJson_HttpPost(List<BasicNameValuePair> nameValuePairs, String URL) {
//        // Thread t = new Thread() {
//        InputStream in = null;
//        // public void run() {
//        // Looper.prepare(); //For Preparing Message Pool for the child Thread
//        HttpClient client = new DefaultHttpClient();
//        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
//        // Limit
//        HttpResponse response;
//
//        try {
//            HttpPost post = new HttpPost(URL);
//
//            post.setHeader(new BasicHeader(HTTP.CONTENT_TYPE,
//                    "application/x-www-form-urlencoded"));
//            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//            response = client.execute(post);
//
//			/* Checking response */
//            if (response != null) {
//                in = response.getEntity().getContent(); // Get the data in the
//                // entity
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.i("Error_HttpPost", e.getMessage());
//        }
//
//        // Looper.loop(); //Loop in the message queue
//        // }
//        // };
//
//        // t.start();
//        return in;
//    }
//
//    public static JSONObject uploadImage(String filePath) {
//        String URL_UPLOAD = "http://traffic.hcmut.edu.vn/ITS/rest/upload/UploadImageToPoint";
//        String POINT_ID = "726ea016-128c-4f97-873d-2db0dcc275d7";
//
//        JSONObject responseJson = new JSONObject();
//
//        List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(2);
//        nameValuePairs.add(new BasicNameValuePair("filename", filePath));
//        nameValuePairs.add(new BasicNameValuePair("pointId", POINT_ID));
//        nameValuePairs.add(new BasicNameValuePair("tokenId", LoginManager.getInstance().getUser().getTokenId()));
//        nameValuePairs.add(new BasicNameValuePair("dataImage", GraphicUtils.convertImage2Base64(filePath)));
//
//        Log.e("base64", GraphicUtils.convertImage2Base64(filePath));
//
//        InputStream response_stream = sendJson_HttpPost(nameValuePairs, URL_UPLOAD);
//        String str_response = Utilities.readStringFromInputStream(response_stream);
//        try {
//            responseJson = new JSONObject(str_response);
//        } catch (JSONException e) {
//            Log.i(TAG, e.getMessage());
//        }
//        return responseJson;
//    }

}
