package cse.its.dbhelper;

import org.json.JSONObject;

public class WarningDrawable {
	public WarningDrawable(JSONObject obj){
		try{
			this.lat = Double.parseDouble(obj.getString("latitude"));
			this.log = Double.parseDouble(obj.getString("longitude"));
			
			this.time = obj.getString("time");
			this.timepost = obj.getString("timePost");
			this.type = obj.getString("type");
			
		}catch(Exception ex){
			
		}
	}
	public WarningDrawable(double lat2, double lon) {
		this.lat = lat2;
		this.log = lon;
	}
	private double lat;
	private double log;
	private String timepost;
	private String time;
	private String type;
	
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLog() {
		return log;
	}
	public void setLog(double log) {
		this.log = log;
	}
	public String getTimepost() {
		return timepost;
	}
	public void setTimepost(String timepost) {
		this.timepost = timepost;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
