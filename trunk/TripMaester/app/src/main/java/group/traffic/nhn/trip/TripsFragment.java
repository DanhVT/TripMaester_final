package group.traffic.nhn.trip;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import vn.edu.hcmut.its.tripmaester.R;
import group.traffic.nhn.message.MessageItem;
import group.traffic.nhn.message.MessageListAdapter;
import vn.edu.hcmut.its.tripmaester.ui.activity.MainActivity;
import vn.edu.hcmut.its.tripmaester.ui.adapter.TripArrayAdapter;
import vn.edu.hcmut.its.tripmaester.service.http.HttpManager;


public class TripsFragment extends Fragment {
    private Timer timer;
    private Timer timerTrip;
    private ListView lstTripsInfor;
    private LinearLayout noTripWrap;
    private Button bAddtrip;
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
        noTripWrap= (LinearLayout) rootView.findViewById(R.id.no_trip_wrap);
        bAddtrip = (Button) rootView.findViewById(R.id.bAddtrip);
        // create trip adapter
        TripArrayAdapter tripAdapter = new TripArrayAdapter(getActivity()
                .getApplicationContext(),
                R.id.list_trips_infor,
                TripManager.lst_user_trip);

        bAddtrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trip = new Intent(getActivity(), NewTripActivity.class);
                startActivity(trip);
            }
        });
        tripAdapter.mainActivity = (MainActivity) this.getActivity();
        tripAdapter.tripsFragment = this;

        lstTripsInfor.setAdapter(tripAdapter);

        // load trip
        loadTrips(tripAdapter);
        return rootView;
    }

    // load trips
    private void loadTrips(final TripArrayAdapter tripAdapter) {
        //LOAD TRIPS FROM API
        TripManager.addDataTrip(getActivity(), new TripManager.ITripCallback() {
            @Override
            public void onDataUpdated() {
                tripAdapter.notifyDataSetChanged();
                if(TripManager.lst_user_trip.size() == 0 ){
                    noTripWrap.setVisibility(View.VISIBLE);
                }
                else{
                    noTripWrap.setVisibility(View.GONE);
                }
                lstTripsInfor.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadTrips(tripAdapter);
                    }
                }, 10000);
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
