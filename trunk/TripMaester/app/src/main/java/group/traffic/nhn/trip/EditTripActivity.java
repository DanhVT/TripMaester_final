package group.traffic.nhn.trip;

import java.util.ArrayList;

import vn.edu.hcmut.its.tripmaester.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class EditTripActivity extends Activity{

	private ImageButton mImgCancel;
	private ImageButton mImgOkie;
	private ArrayList<TripItem4EditList> mListTripEdit;
	private EditTripListAdapter mEditTripAdapter;
	private ListView mListEdit;
	private void initControls(){
		mImgCancel = (ImageButton)findViewById(R.id.btn_edit_cancel);
		mImgOkie = (ImageButton) findViewById(R.id.btn_edit_okie);
		mListEdit = (ListView)findViewById(R.id.edit_list_trips);

	}
	
	/**
	 * load trip for edit
	 */
	private void loadData(){
		
		mListTripEdit = new ArrayList<TripItem4EditList>();
		//TODO: KenK11 replace fake data with data from ITS server
		TripItem4EditList item1 = new TripItem4EditList("Du lich ha long", "20/10/2014", "22/10/2014", "30/10/2014", "Ho Chi Minh", "Ha Long");
		TripItem4EditList item2 = new TripItem4EditList("Vu tau cuoi tuan", "20/03/2015", "20/03/2015", "22/03/2015", "Ho Chi Minh", "Vung Tau");
		TripItem4EditList item3 = new TripItem4EditList("Du lich ha long", "20/10/2014", "22/10/2014", "30/10/2014", "Da Nang", "Nha Trang");
		TripItem4EditList item4 = new TripItem4EditList("Du lich ha long", "20/10/2014", "22/10/2014", "30/10/2014", "Binh Dinh", "Hue");
		TripItem4EditList item5 = new TripItem4EditList("Du lich ha long", "20/10/2014", "22/10/2014", "30/10/2014", "Ho Chi Minh", "Vung Tau");
		
		mListTripEdit.add(item1);
		mListTripEdit.add(item2);
		mListTripEdit.add(item3);
		mListTripEdit.add(item4);
		mListTripEdit.add(item5);
		
		
		mEditTripAdapter = new EditTripListAdapter(this.getApplicationContext(), mListTripEdit);
		mListEdit.setAdapter(mEditTripAdapter);
		mListEdit.setOnItemClickListener(new ItemClickListener());
		
		
	}
	
	private class ItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Toast.makeText(getApplicationContext(), "you click item " + position, Toast.LENGTH_LONG).show();
		}
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_edit_trips);
		
		initControls();
		
		//load data
		loadData();

		
//		
//		loadData();
		
//		mImgCancel.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});
//		
//		mImgOkie.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});
		
	}	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
}
