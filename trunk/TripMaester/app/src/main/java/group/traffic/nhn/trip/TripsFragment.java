package group.traffic.nhn.trip;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import vn.edu.hcmut.its.tripmaester.R;
import group.traffic.nhn.message.MessageItem;
import group.traffic.nhn.message.MessageListAdapter;
import vn.edu.hcmut.its.tripmaester.controller.ICallback;
import vn.edu.hcmut.its.tripmaester.controller.manager.LoginManager;
import vn.edu.hcmut.its.tripmaester.model.Trip;
import vn.edu.hcmut.its.tripmaester.service.http.HttpConstants;
import vn.edu.hcmut.its.tripmaester.ui.activity.MainActivity;
import vn.edu.hcmut.its.tripmaester.ui.adapter.TripArrayAdapter;
import vn.edu.hcmut.its.tripmaester.service.http.HttpManager;


public class TripsFragment extends Fragment {
    public MainActivity mainActivity;
    private Timer timer;
    private Timer timerTrip;
    private ListView lstTripsInfor;
    private Button bAddtrip;
    public static ArrayList<Trip> lst_user_trip = new ArrayList<Trip>();
    public void stopTimer() {
        if (timer != null) {
            timer.purge();
            timer.cancel();
        }
    }

    public TripsFragment() {
    }

    UpdateCommentAsync updateCommentAsync;

    // Async task to update comment
    class UpdateCommentAsync extends AsyncTask<String, Void, ArrayList<MessageItem>> {

        private MessageListAdapter lstMessageAdapter;
        private int pos;

        public void setMessageAdapter(MessageListAdapter lstMessageAdapter, int pos) {
            this.lstMessageAdapter = lstMessageAdapter;
            this.pos = pos;
        }

        protected ArrayList<MessageItem> doInBackground(String... params) {
            ArrayList<MessageItem> lstMessageTemp = HttpManager
                    .getListCommentTrip(TripManager.lst_user_trip
                            .get(pos)
                            .getTripId());

            return lstMessageTemp;
        }

        protected void onPostExecute(ArrayList<MessageItem> lsts) {
            lstMessageAdapter.setmFriends(lsts);
            lstMessageAdapter.notifyDataSetChanged();
            if (timer != null) {
                timer.purge();
                timer.cancel();
            }
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runAsyncTask(lstMessageAdapter, pos);
                }
            }, 1000);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_trips, container, false);
        lstTripsInfor = (ListView) rootView.findViewById(R.id.list_trips_infor);
        this.mainActivity = (MainActivity) this.getActivity();
        MainActivity.fab_search = (FloatingActionButton) mainActivity.findViewById(R.id.fab_search);
        MainActivity.fab_search.setVisibility(View.VISIBLE);

        // create trip adapter
        final TripArrayAdapter[] tripAdapter = {new TripArrayAdapter(getActivity()
                .getApplicationContext(),
                R.id.list_trips_infor,
                TripManager.lst_user_trip)};

        MainActivity.fab_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(mainActivity);
                View promptsView = li.inflate(
                        R.layout.search_trip_info_dialog, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        mainActivity);
                alertDialogBuilder.setView(promptsView);

                final EditText txt_startPlace = (EditText) promptsView
                        .findViewById(R.id.txtStartPlace);
                final EditText txt_endPlace = (EditText) promptsView
                        .findViewById(R.id.txtEndPlace);
                final Spinner spinner_trip_privacy = (Spinner) promptsView.findViewById(R.id.spinner_trip_privacy);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String URL = HttpConstants.HOST_NAME + "/ITS/rest/trip/SearchTrip";
                                        String startPlace = txt_startPlace.getText().toString();
                                        String endPlace = txt_endPlace.getText().toString();
                                        String privacy = spinner_trip_privacy.getSelectedItem().toString();

                                        HttpManager.searchTrip(startPlace, endPlace, privacy, new ICallback<JSONArray>(){
                                            @Override
                                            public void onCompleted(JSONArray data, Object tag, Exception e) {
                                                try {
                                                    for (int i = 0; i < data.length(); i++) {
                                                        JSONObject jsonobject = data.getJSONObject(i);
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
                                                        lst_user_trip.add(trip1);
                                                    }
                                                    tripAdapter[0] = new TripArrayAdapter(getActivity()
                                                            .getApplicationContext(),
                                                            R.id.list_trips_infor,
                                                            lst_user_trip);

                                                } catch (JSONException e1) {
                                                    Log.i("tag", "error json array");
                                                }
                                                catch (NullPointerException ex){
                                                    ex.printStackTrace();
                                                    Toast.makeText(getActivity(), "Can't get list search trip", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });
        tripAdapter[0].mainActivity = (MainActivity) this.getActivity();
        tripAdapter[0].tripsFragment = this;

        lstTripsInfor.setAdapter(tripAdapter[0]);

        // load trip
        loadTrips(tripAdapter[0]);
        return rootView;
    }

    // load trips
    private void loadTrips(final TripArrayAdapter tripAdapter) {
        //LOAD TRIPS FROM API
        TripManager.addDataTrip(getActivity(), new TripManager.ITripCallback() {
            @Override
            public void onDataUpdated() {
                tripAdapter.notifyDataSetChanged();
                lstTripsInfor.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadTrips(tripAdapter);
                    }
                }, 2000);
            }
        });
    }

    public void runAsyncTask(MessageListAdapter lstMessageAdapter, int pos) {
        if (updateCommentAsync != null) {
            updateCommentAsync.cancel(false);
        }

        Log.i("POS=====", String.valueOf(pos));
        updateCommentAsync = new UpdateCommentAsync();
        updateCommentAsync.setMessageAdapter(lstMessageAdapter, pos);
        updateCommentAsync.execute();
    }


}
