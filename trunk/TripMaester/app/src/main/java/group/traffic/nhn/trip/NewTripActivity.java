package group.traffic.nhn.trip;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import vn.edu.hcmut.its.tripmaester.R;

public class NewTripActivity extends Activity{

	private ImageButton mImgCancel;
	private ImageButton mImgOkie;
	private Button buttonStartDate, buttonFinishDate;
	private int yearStart, monthStart, dayStart,
				yearFinish, monthFinish, dayFinish;
	private TextView textViewDayStart, textViewMonthStart, textViewYearStart,
					 textViewDayFinish, textViewMonthFinish, textViewYearFinish;
	private static final int DILOG_ID_START = 0, DILOG_ID_FINISH = 1;
	
	private void initControls(){
		mImgCancel = (ImageButton)findViewById(R.id.btn_cancel);
		mImgOkie = (ImageButton) findViewById(R.id.btn_okie);

		buttonStartDate = (Button) findViewById(R.id.buttonStartDate);
		buttonFinishDate = (Button) findViewById(R.id.buttonFinishDate);

		textViewDayStart = (TextView) findViewById(R.id.textViewDayStart);
		textViewMonthStart = (TextView) findViewById(R.id.textViewMonthStart);
		textViewYearStart = (TextView) findViewById(R.id.textViewYearStart);
		textViewDayFinish = (TextView) findViewById(R.id.textViewDayFinish);
		textViewMonthFinish = (TextView) findViewById(R.id.textViewMonthFinish);
		textViewYearFinish = (TextView) findViewById(R.id.textViewYearFinish);
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

		setInitCalendar();
		showDialogOnButtonClick();
	}	

	public void showDialogOnButtonClick(){
		buttonStartDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				showDialog(DILOG_ID_START);
			}
		});
		buttonFinishDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				showDialog(DILOG_ID_FINISH);
			}
		});
	}

	private void setInitCalendar(){
		final Calendar calendar = Calendar.getInstance();
		yearStart = calendar.get(Calendar.YEAR);
		monthStart = calendar.get(Calendar.MONTH);
		dayStart = calendar.get(Calendar.DAY_OF_MONTH);
		textViewDayStart.setText("" + dayStart);
		textViewMonthStart.setText(("" + new DateFormatSymbols().getMonths()[monthStart]).toUpperCase());
		textViewYearStart.setText("" + yearStart);

		yearFinish = calendar.get(Calendar.YEAR);
		monthFinish = calendar.get(Calendar.MONTH);
		dayFinish = calendar.get(Calendar.DAY_OF_MONTH);
		textViewDayFinish.setText("" + dayFinish);
		textViewMonthFinish.setText(("" + new DateFormatSymbols().getMonths()[monthFinish]).toUpperCase());
		textViewYearFinish.setText("" + yearFinish);
	}

	@Override
	protected Dialog onCreateDialog(int id){
		if(id == DILOG_ID_START){
			return new DatePickerDialog(this, dpickerListnerStart, yearStart, monthStart, dayStart);
		}
		if(id == DILOG_ID_FINISH){
			return new DatePickerDialog(this, dpickerListnerFinish, yearFinish, monthFinish, dayFinish);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener dpickerListnerStart = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
			yearStart = i;
			monthStart = i1;
			dayStart = i2;

			textViewDayStart.setText("" + dayStart);
			textViewMonthStart.setText(("" + new DateFormatSymbols().getMonths()[monthStart]).toUpperCase());
			textViewYearStart.setText("" + yearStart);
		}
	};

	private DatePickerDialog.OnDateSetListener dpickerListnerFinish
			= new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
			yearFinish = i;
			monthFinish = i1;
			dayFinish = i2;

			textViewDayFinish.setText("" + dayFinish);
			textViewMonthFinish.setText(("" + new DateFormatSymbols().getMonths()[monthFinish]).toUpperCase());
			textViewYearFinish.setText("" + yearFinish);
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
}
