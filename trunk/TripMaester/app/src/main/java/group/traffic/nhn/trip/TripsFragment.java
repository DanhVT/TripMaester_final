package group.traffic.nhn.trip;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import group.traffic.nhn.message.MessageItem;
import group.traffic.nhn.message.MessageListAdapter;
import vn.edu.hcmut.its.tripmaester.R;
import vn.edu.hcmut.its.tripmaester.controller.ICallback;
import vn.edu.hcmut.its.tripmaester.controller.manager.LoginManager;
import vn.edu.hcmut.its.tripmaester.model.Trip;
import vn.edu.hcmut.its.tripmaester.service.http.HttpConstants;
import vn.edu.hcmut.its.tripmaester.service.http.HttpManager;
import vn.edu.hcmut.its.tripmaester.ui.activity.MainActivity;
import vn.edu.hcmut.its.tripmaester.ui.adapter.TripArrayAdapter;


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
            ArrayList<MessageItem> lstMessageTemp = new ArrayList<>();
            HttpManager
                    .getListCommentTrip(getActivity(), TripManager.lst_user_trip.get(pos).getTripId(), new ICallback<ArrayList<MessageItem>>() {
                        @Override
                        public void onCompleted(ArrayList<MessageItem> data, Object tag, Exception e) {

                        }
                    });


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
                    loadListMessage(lstMessageAdapter, pos);
                }
            }, 2000);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_trips, container, false);
        lstTripsInfor = (ListView) rootView.findViewById(R.id.list_trips_infor);
        this.mainActivity = (MainActivity) this.getActivity();

        // create trip adapter
        final TripArrayAdapter[] tripAdapter = {new TripArrayAdapter(getActivity()
                .getApplicationContext(),
                R.id.list_trips_infor,
                TripManager.lst_user_trip)};
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
        Log.d("LoadTrip", "Load trip");
        TripManager.addDataTrip(getActivity(), new TripManager.ITripCallback() {
            @Override
            public void onDataUpdated() {
                tripAdapter.notifyDataSetChanged();
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

    public void loadListMessage(final MessageListAdapter lstMessageAdapter, int pos){
        HttpManager
            .getListCommentTrip(getActivity(), TripManager.lst_user_trip.get(pos).getTripId(), new ICallback<ArrayList<MessageItem>>() {
                @Override
                public void onCompleted(ArrayList<MessageItem> data, Object tag, Exception e) {
                    lstMessageAdapter.setmFriends(data);
                }
            });
    }

}
