package group.traffic.nhn.trip;

public class TripItem4EditList {
	private String mTripName;
	private String mCreatedDate;
	private String mStartDate;
	private String mFinishDate;
	private String mEndDate;
	private String mStartPlace;
	private String mFinishPlace;
	public TripItem4EditList(String name, String createddatre, String startdate,
			String finishdate, String startplace, String finishplace) {
		this.mTripName = name;
		this.mCreatedDate = createddatre;
		this.mStartDate = startdate;
		this.mFinishDate = finishdate;
		this.mStartPlace = startplace;
		this.mFinishPlace = finishplace;
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
	public String getmEndDate() {
		return mEndDate;
	}
	public void setmEndDate(String mEndDate) {
		this.mEndDate = mEndDate;
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
	public String getmCreatedDate() {
		return mCreatedDate;
	}
	public void setmCreatedDate(String mCreatedDate) {
		this.mCreatedDate = mCreatedDate;
	}
}
