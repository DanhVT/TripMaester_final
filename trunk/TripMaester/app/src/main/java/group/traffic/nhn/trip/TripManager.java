package group.traffic.nhn.trip;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import vn.edu.hcmut.its.tripmaester.R;
import vn.edu.hcmut.its.tripmaester.controller.manager.LoginManager;
import vn.edu.hcmut.its.tripmaester.model.Trip;
import vn.edu.hcmut.its.tripmaester.service.http.HttpManager;
import vn.edu.hcmut.its.tripmaester.controller.ICallback;

public class TripManager {
    public static ArrayList<Trip> lst_user_trip = new ArrayList<Trip>();

    // for demo only
    public static void addDataTrip(final Context context, final ITripCallback callback) {
        //load trip
//		 lst_user_trip = new ArrayList<Trip>();
        //get list trip's of user
        HttpManager.getListTrip(context, new ICallback<JSONArray>() {
            @Override
            public void onCompleted(JSONArray jsonarray, Object tag, Exception e) {
                try {
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);

                        if (null != jsonobject) {
                            final Trip trip1 = new Trip();
                            if (!jsonobject.isNull("tripId")) {
                                trip1.setTripId(jsonobject.getString("tripId"));
                            }
                            if (!jsonobject.isNull("userId")) {
                                trip1.setUserIdOwner(jsonobject.getString("userId"));
                            }
                            if (!jsonobject.isNull("userName")) {
                                trip1.setUserName(jsonobject.getString("userName"));
                            }
                            if (!jsonobject.isNull("link")) {
                                trip1.setLinkImage(jsonobject.getString("link"));
                            }
                            if (!jsonobject.isNull("tripName")) {
                                trip1.setTripName(jsonobject.getString("tripName"));
                            }
                            if (!jsonobject.isNull("startTime")) {
                                trip1.setTimeStartTrip(jsonobject.getString("startTime"));
                            }
                            if (!jsonobject.isNull("endTime")) {
                                trip1.setTimeEndTrip(jsonobject.getString("endTime"));
                            }
                            if (!jsonobject.isNull("fromLocationName")) {
                                trip1.setPlaceStartTrip(jsonobject.getString("fromLocationName"));
                            }
                            if (!jsonobject.isNull("endTime")) {
                                trip1.setTimeEndTrip(jsonobject.getString("endTime"));
                            }
                            if (!jsonobject.isNull("dateTime")) {
                                trip1.setDateOpenTrip(jsonobject.getString("dateTime"));
                            }
                            if (!jsonobject.isNull("toLocationName")) {
                                trip1.setPlaceEndTrip(jsonobject.getString("toLocationName"));
                            }
                            if (!jsonobject.isNull("emotion")) {
                                trip1.setEmotion(jsonobject.getString("emotion"));
                            }
                            trip1.setAvaUserCreateTrip(R.drawable.icon_friend);

                            if(!jsonobject.isNull("listLiker")){
                                trip1.setLsitUserIdLike(new ArrayList<String>(Arrays.asList(jsonobject.getString("listLiker").split(","))));  //TODO: CHECK;
                                trip1.setNumberLikeTrip(trip1.lstUserIdLike.size() + " likes");
                            }

                            //FIXME: get list comment, count of likes

                            if(!jsonobject.isNull("numComments")){
                                trip1.setNumberCommentTrip(jsonobject.getInt("numComments") + " comments");
                            }

                            addTrip(trip1);
//					lst_user_trip.addTrip(trip1);
                        }
                    }
                    callback.onDataUpdated();
                } catch (JSONException e1) {
                    Log.i("tag", "error json array");
                }
                catch (NullPointerException ex){
                    Log.i("tag", "error null pointer");
                }
            }
        });


//        HttpManager.getListPrivateTrip(context, new ICallback<JSONArray>() {
//            @Override
//            public void onCompleted(JSONArray jsonarray,  Object tag,Exception e) {
//                try {
//                    for (int i = 0; i < jsonarray.length(); i++) {
//                        JSONObject jsonobject = jsonarray.getJSONObject(i);
//                        final Trip trip1 = new Trip();
//                        if (null != jsonobject) {
//                            if (!jsonobject.isNull("tripId")) {
//                                trip1.setTripId(jsonobject.getString("tripId"));
//                            }
//                            if (!jsonobject.isNull("tripName")) {
//                                trip1.setTripName(jsonobject.getString("tripName"));
//                            }
//                            if (!jsonobject.isNull("startTime")) {
//                                trip1.setTimeStartTrip(jsonobject.getString("startTime"));
//                            }
//                            if (!jsonobject.isNull("endTime")) {
//                                trip1.setTimeEndTrip(jsonobject.getString("endTime"));
//                            }
//                            if (!jsonobject.isNull("fromLocationName")) {
//                                trip1.setPlaceStartTrip(jsonobject.getString("fromLocationName"));
//                            }
//                            if (!jsonobject.isNull("toLocationName")) {
//                                trip1.setPlaceEndTrip(jsonobject.getString("toLocationName"));
//                            }
//                            if (!jsonobject.isNull("dateTime")) {
//                                trip1.setDateOpenTrip(jsonobject.getString("dateTime"));
//                            }
//                            if (!jsonobject.isNull("emotion")) {
//                                trip1.setEmotion(jsonobject.getString("emotion"));
//                            }
//                            trip1.setUserName(LoginManager.getInstance().getUser().getName());
//                            trip1.setAvaUserCreateTrip(R.drawable.ic_user_profile);
//
//                            if(!jsonobject.isNull("listLiker")){
//                                trip1.setLsitUserIdLike(new ArrayList<String>(Arrays.asList(jsonobject.getString("listLiker").split(","))));  //TODO: CHECK;
//                                trip1.setNumberLikeTrip(trip1.lstUserIdLike.size() + " likes");
//                            }
//                            // FIXME: get list comment, count of likes
//
//                            if(!jsonobject.isNull("numComments")){
//                                trip1.setNumberCommentTrip(jsonobject.getInt("numComments") + " comments");
//                            }
//
//                            addTrip(trip1);
////					lst_user_trip.addTrip(trip1);
//                        }
//                    }
//                    callback.onDataUpdated();
//                } catch (JSONException e1) {
//                    Log.i("tag", "error json array");
//                }
//                catch (NullPointerException e1) {
//                    Log.i("tag", "error null pointer");
//                }
//            }
//        });

        //FIXME: get list share trip
        HttpManager
                .getListSharedTrip(context, new ICallback<JSONArray>() {
                    @Override
                    public void onCompleted(JSONArray jsonarray,  Object tag,Exception e) {
                        try {
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);

                                if (null != jsonobject) {
                                    final Trip trip1 = new Trip();
                                    if (!jsonobject.isNull("tripId")) {
                                        trip1.setTripId(jsonobject.getString("tripId"));
                                    }
                                    if (!jsonobject.isNull("userId")) {
                                        trip1.setUserIdOwner(jsonobject.getString("userId"));
                                    }
                                    if (!jsonobject.isNull("tripName")) {
                                        trip1.setTripName(jsonobject.getString("tripName"));
                                    }
                                    if (!jsonobject.isNull("userName")) {
                                        trip1.setUserName(jsonobject.getString("userName"));
                                    }
                                    if (!jsonobject.isNull("link")) {
                                        trip1.setLinkImage(jsonobject.getString("link"));
                                    }
                                    if (!jsonobject.isNull("startTime")) {
                                        trip1.setTimeStartTrip(jsonobject.getString("startTime"));
                                    }
                                    if (!jsonobject.isNull("endTime")) {
                                        trip1.setTimeEndTrip(jsonobject.getString("endTime"));
                                    }
                                    if (!jsonobject.isNull("fromDescription")) {
                                        trip1.setPlaceStartTrip(jsonobject.getString("fromDescription"));
                                    }
                                    if (!jsonobject.isNull("toDescription")) {
                                        trip1.setPlaceEndTrip(jsonobject.getString("toDescription"));
                                    }
                                    if (!jsonobject.isNull("dateTime")) {
                                        trip1.setDateOpenTrip(jsonobject.getString("dateTime"));
                                    }
                                    if (!jsonobject.isNull("emotion")) {
                                        trip1.setEmotion(jsonobject.getString("emotion"));
                                    }

                                    if(!jsonobject.isNull("listLiker")){
                                        trip1.setLsitUserIdLike(new ArrayList<String>(Arrays.asList(jsonobject.getString("listLiker").split(","))));  //TODO: CHECK;
                                        trip1.setNumberLikeTrip(trip1.lstUserIdLike.size() + " likes");
                                    }

                                    trip1.setAvaUserCreateTrip(R.drawable.icon_user_share);
                                    //FIXME: get count comments, count of likes

                                    if(!jsonobject.isNull("numComments")){
                                        trip1.setNumberCommentTrip(jsonobject.getInt("numComments") + " comments");
                                    }
                                    //======
//					lst_user_trip.addTrip(trip1);
                                    addTrip(trip1);
                                }
                            }
                            callback.onDataUpdated();
                        } catch (Exception ex) {
                            Log.i("tag", "error json array");
                        }
                    }
                });

        //FIXME: get list public trip
//        HttpManager.getListPublicTrip(context, new ICallback<JSONArray>() {
//
//                     @Override
//                     public void onCompleted(JSONArray jsonarray, Object tag, Exception e) {
//                 try {
//                     for (int i = 0; i < jsonarray.length(); i++) {
//                         JSONObject jsonobject = jsonarray.getJSONObject(i);
//                         final Trip trip1 = new Trip();
//
//                         if (!jsonobject.isNull("tripName")) {
//                             trip1.setTripName(jsonobject.getString("tripName"));
//                         }
//
//                         if (!jsonobject.isNull("startTime")) {
//                             trip1.setTimeStartTrip(jsonobject.getString("startTime"));
//                         }
//                         if (!jsonobject.isNull("fromDescription")) {
//                             trip1.setPlaceStartTrip(jsonobject.getString("fromDescription"));
//                         }
//                         if (!jsonobject.isNull("endTime")) {
//                             trip1.setTimeEndTrip(jsonobject.getString("endTime"));
//                         }
//                         if (!jsonobject.isNull("dateTime")) {
//                             trip1.setDateOpenTrip(jsonobject.getString("dateTime"));
//                         }
//                         if (!jsonobject.isNull("toDescription")) {
//                             trip1.setPlaceEndTrip(jsonobject.getString("toDescription"));
//                         }
//                         if (!jsonobject.isNull("tripId")) {
//                             trip1.setTripId(jsonobject.getString("tripId"));
//                         }
//                         if (!jsonobject.isNull("userId")) {
//                             trip1.setUserIdOwner(jsonobject.getString("userId"));
//                         }
//                         if (!jsonobject.isNull("emotion")) {
//                             trip1.setEmotion(jsonobject.getString("emotion"));
//                         }
//
//                         if(!jsonobject.isNull("listLiker")){
//                             trip1.setLsitUserIdLike(new ArrayList<String>(Arrays.asList(jsonobject.getString("listLiker").split(","))));  //TODO: CHECK;
//                             trip1.setNumberLikeTrip(trip1.lstUserIdLike.size() + " likes");
//                         }
//                         if(!jsonobject.isNull("numComments")){
//                             trip1.setNumberCommentTrip(jsonobject.getInt("numComments") + " comments");
//                         }
//
// //                                HttpManager.getLikeInfoOnTrip(trip1.getTripId(), context, new ICallback<JSONObject>() {
// //                                    @Override
// //                                    public void onCompleted(JSONObject LikeInfo,  Object tag,Exception e) {
// //                                        try {
// //                                            if (!LikeInfo.isNull("listLiker")) {
// //                                                JSONArray listLiker =  new JSONArray(LikeInfo.getString("listLiker"));
// //                                                trip1.setNumberLikeTrip(listLiker.length() + " likes");
// //                                                ArrayList<String> likers = new ArrayList<String>();
// //                                                for (int k =0; k< listLiker.length() ; k++){
// //                                                    JSONObject json = new JSONObject(listLiker.getString(k));
// //                                                    likers.add(json.getString("userId"));
// //                                                }
// //                                                trip1.setLsitUserIdLike(likers);
// //
// //                                            }
// //                                        } catch (JSONException e1) {
// //                                            e1.printStackTrace();
// //                                        }catch (NullPointerException ex){
// //                                            ex.printStackTrace();
// //                                        }
// //
// //                                    }
// //                                });//numLike
//
//
//                                 addTrip(trip1);
//                             }
//                         }
//                         catch (Exception ex){
//                             Log.i("tag", "error json array");
//                         }
//                     }
//                 });

    }

    public interface ITripCallback {
        void onDataUpdated();
    }

    public static void addTrip(Trip trip) {
        boolean isHave = false;
        for (int i = 0; i < lst_user_trip.size(); i++) {
            if (lst_user_trip.get(i).getTripId().compareTo(trip.getTripId()) == 0) {
                isHave = true;
                trip = null;
                break;
            }
        }
        if (!isHave)
            lst_user_trip.add(trip);
    }
}