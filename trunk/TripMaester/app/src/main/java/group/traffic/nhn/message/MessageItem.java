package group.traffic.nhn.message;

import android.app.Activity;

public class MessageItem {
	private String content;
	private int icon;
	private boolean isNew = false;
	private int number;
	private String date;
	private String fbIdAva;
	
	public MessageItem(){
		
	}
	
	public MessageItem(String title, int icon) {
		this.setContent(title);
		this.icon = icon;
	}

	public MessageItem(String title, int icon, boolean flagnew, int number){
		this.setContent(title);
		this.icon = icon;
		this.setNew(flagnew);
		this.setNumber(number);
	}
	
	public MessageItem(Activity act,String title, int icon, boolean flagnew, int number, String date, String fbId){
		this.setContent(title);
		this.icon = icon;
		this.setNew(flagnew);
		this.setNumber(number);
		this.date =date;
		this.fbIdAva = fbId;
	}
	
	public MessageItem(String title, int icon, boolean flagnew, int number, String fbId,String date){
		String content = "";
		try{
			content = new String(title.getBytes(), "UTF-8");
		}catch(Exception ex){
			content = title;
		}
		
		this.setContent(content);
		this.icon = icon;
		this.setNew(flagnew);
		this.setNumber(number);
		this.fbIdAva = fbId;
		this.date = date;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFbIdAva() {
		return fbIdAva;
	}

	public void setFbIdAva(String fbIdAva) {
		this.fbIdAva = fbIdAva;
	}
}
