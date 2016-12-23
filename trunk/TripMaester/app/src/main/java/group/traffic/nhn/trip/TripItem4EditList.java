package group.traffic.nhn.trip;

public class TripItem4EditList {
	private String mTripId;
	private String mTripName;
	private String mDateTime;
	private String mStartDate;
	private String mFinishDate;
	private String mStartPlace;
	private String mFinishPlace;
	private String mEmotion;

	public TripItem4EditList(){	}

//	public TripItem4EditList(String name, String createddatre, String startdate,
//			String finishdate, String startplace, String finishplace) {
//		this.mTripName = name;
//		this.mCreatedDate = createddatre;
//		this.mStartDate = startdate;
//		this.mFinishDate = finishdate;
//		this.mStartPlace = startplace;
//		this.mFinishPlace = finishplace;
//	}

	public String getmTripId() {
		return mTripId;
	}
	public void setmTripId(String mTripId) {
		this.mTripId = mTripId;
	}
	public String getmTripName() {
		return mTripName;
	}
	public void setmTripName(String mTripName) {
		this.mTripName = mTripName;
	}
	public String getmStartDate() {
		return mStartDate;
	}
	public void setmStartDate(String mStartDate) {
		this.mStartDate = mStartDate;
	}
	public String getmFinishDate() {
		return mFinishDate;
	}
	public void setmFinishDate(String mFinishDate) {
		this.mFinishDate = mFinishDate;
	}
	public String getmStartPlace() {
		return mStartPlace;
	}
	public void setmStartPlace(String mStartPlace) {
		this.mStartPlace = mStartPlace;
	}
	public String getmFinishPlace() {
		return mFinishPlace;
	}
	public void setmFinishPlace(String mFinishPlace) {
		this.mFinishPlace = mFinishPlace;
	}
	public String getmDateTime() {
		return mDateTime;
	}
	public void setmDateTime(String mDateTime) {
		this.mDateTime = mDateTime;
	}
	public String getmEmotion() {
		return mEmotion;
	}
	public void setmEmotion(String mEmotion) {
		this.mEmotion = mEmotion;
	}
}
