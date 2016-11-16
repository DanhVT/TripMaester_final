package group.traffic.nhn.trip;

import vn.edu.hcmut.its.tripmaester.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class NewTripActivity extends Activity{

	private ImageButton mImgCancel;
	private ImageButton mImgOkie;
	
	private void initControls(){
		mImgCancel = (ImageButton)findViewById(R.id.btn_cancel);
		mImgOkie = (ImageButton) findViewById(R.id.btn_okie);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 
		setContentView(R.layout.activity_new_trip);
		
		initControls();
		
		mImgCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		mImgOkie.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
}
