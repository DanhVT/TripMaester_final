package group.traffic.nhn.trip;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
                        final Trip trip1 = new Trip();
                        if (!jsonobject.isNull("startTime")) {
                            trip1.setTimeStartTrip(jsonobject.getString("startTime"));
                        }
                        if (!jsonobject.isNull("fromDescription")) {
                            trip1.setPlaceStartTrip(jsonobject.getString("fromDescription"));
                        }
                        if (!jsonobject.isNull("endTime")) {
                            trip1.setTimeEndTrip(jsonobject.getString("endTime"));
                        }
                        if (!jsonobject.isNull("tripName")) {
                            trip1.setDateOpenTrip(jsonobject.getString("tripName"));
                        }
                        if (!jsonobject.isNull("toDescription")) {
                            trip1.setPlaceEndTrip(jsonobject.getString("toDescription"));
                        }
                        if (!jsonobject.isNull("tripId")) {
                            trip1.setTripId(jsonobject.getString("tripId"));
                        }

                        trip1.setUserName(LoginManager.getInstance().getUser().getName());
                        trip1.setAvaUserCreateTrip(R.drawable.ic_user_profile);

                        HttpManager.countLikeTrip(trip1.getTripId(), context, new ICallback<JSONObject>() {
                            @Override
                            public void onCompleted(JSONObject jsonLike,  Object tag,Exception e) {
                                if (!jsonLike.isNull("numLike")) {
                                    try {
                                        trip1.setNumberLikeTrip(jsonLike.getString("numLike") + " likes");
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                }

                            }
                        });//numLike
                        //FIXME: get list comment, count of likes

                        trip1.setNumberCommentTrip("0 comments");
                        //======

                        addTrip(trip1);
//					lst_user_trip.addTrip(trip1);

                    }
                    callback.onDataUpdated();
                } catch (JSONException e1) {
                    Log.i("tag", "error json array");
                }
                catch (NullPointerException ex){
                    ex.printStackTrace();
                    Toast.makeText(context, "Can't get list trip", Toast.LENGTH_SHORT).show();
                }
            }
        });


        HttpManager.getListPrivateTrip(context, new ICallback<JSONArray>() {
            @Override
            public void onCompleted(JSONArray jsonarray,  Object tag,Exception e) {
                try {
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        final Trip trip1 = new Trip();
                        if (!jsonobject.isNull("startTime")) {
                            trip1.setTimeStartTrip(jsonobject.getString("startTime"));
                        }
                        if (!jsonobject.isNull("fromDescription")) {
                            trip1.setPlaceStartTrip(jsonobject.getString("fromDescription"));
                        }
                        if (!jsonobject.isNull("endTime")) {
                            trip1.setTimeEndTrip(jsonobject.getString("endTime"));
                        }
                        if (!jsonobject.isNull("tripName")) {
                            trip1.setDateOpenTrip(jsonobject.getString("tripName"));
                        }
                        if (!jsonobject.isNull("toDescription")) {
                            trip1.setPlaceEndTrip(jsonobject.getString("toDescription"));
                        }
                        if (!jsonobject.isNull("tripId")) {
                            trip1.setTripId(jsonobject.getString("tripId"));
                        }

                        trip1.setUserName(LoginManager.getInstance().getUser().getName());
                        trip1.setAvaUserCreateTrip(R.drawable.ic_user_profile);

                        HttpManager.countLikeTrip(trip1.getTripId(), context, new ICallback<JSONObject>() {
                            @Override
                            public void onCompleted(JSONObject jsonLike,  Object tag,Exception e) {
                                if (!jsonLike.isNull("numLike")) {
                                    try {
                                        trip1.setNumberLikeTrip(jsonLike.getString("numLike") + " likes");
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                }

                            }
                        });//numLike
                        // FIXME: get list comment, count of likes

                        trip1.setNumberCommentTrip("0 comments");
                        //======

                        addTrip(trip1);
//					lst_user_trip.addTrip(trip1);

                    }
                    callback.onDataUpdated();
                } catch (JSONException e1) {
                    Log.i("tag", "error json array");
                }
                catch (NullPointerException ex){
                    ex.printStackTrace();
                    Toast.makeText(context, "Can't get list private trip", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //FIXME: get list share trip
        HttpManager
                .getListSharedTrip(context, new ICallback<JSONArray>() {
                    @Override
                    public void onCompleted(JSONArray jsonarray,  Object tag,Exception e) {
                        try {
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                final Trip trip1 = new Trip();
                                if (!jsonobject.isNull("startTime")) {
                                    trip1.setTimeStartTrip(jsonobject.getString("startTime"));
                                }
                                if (!jsonobject.isNull("fromDescription")) {
                                    trip1.setPlaceStartTrip(jsonobject.getString("fromDescription"));
                                }
                                if (!jsonobject.isNull("endTime")) {
                                    trip1.setTimeEndTrip(jsonobject.getString("endTime"));
                                }
                                if (!jsonobject.isNull("tripName")) {
                                    trip1.setDateOpenTrip(jsonobject.getString("tripName"));
                                }
                                if (!jsonobject.isNull("toDescription")) {
                                    trip1.setPlaceEndTrip(jsonobject.getString("toDescription"));
                                }
                                if (!jsonobject.isNull("tripId")) {
                                    trip1.setTripId(jsonobject.getString("tripId"));
                                }
                                if (!jsonobject.isNull("userId")) {
                                    trip1.setUserIdOwner(jsonobject.getString("userId"));
                                }

                                HttpManager.getUserInfo(trip1.getUserIdOwner(), context, new ICallback<JSONObject>() {
                                    @Override
                                    public void onCompleted(JSONObject jsonobjOwner,  Object tag,Exception e) {
                                        String ownerName = "";
                                        if (!jsonobjOwner.isNull("name")) {
                                            try {
                                                ownerName = jsonobjOwner.getString("name");
                                            } catch (JSONException e1) {
                                                e1.printStackTrace();
                                            }
                                        }
                                        trip1.setUserName(ownerName);
                                        trip1.setAvaUserCreateTrip(R.drawable.ic_user_profile);

                                    }
                                });

                                //FIXME: get count comments, count of likes
//					HttpManager.getListCommentTrip(trip1.getTripId());

                                trip1.setNumberLikeTrip("0 likes");
                                trip1.setNumberCommentTrip("0 comments");
                                //======
//					lst_user_trip.addTrip(trip1);
                                addTrip(trip1);

                            }
                            callback.onDataUpdated();
                        } catch (Exception ex) {
                            Log.i("tag", "error json array");
                        }

                    }
                });

        //FIXME: get list public trip

    }

    public interface ITripCallback {
        void onDataUpdated();
    }

    public static void addTrip(Trip trip) {
        boolean isHave = false;
        for (int i = 0; i < lst_user_trip.size(); i++) {
            if (lst_user_trip.get(i).getTripId().compareTo(trip.getTripId()) == 0) {
                isHave = true;
                break;
            }
        }
        if (!isHave)
            lst_user_trip.add(trip);
    }
}
