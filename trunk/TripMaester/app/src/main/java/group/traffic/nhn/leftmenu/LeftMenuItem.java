package group.traffic.nhn.leftmenu;

public class LeftMenuItem {
	private String title;
	private int icon;
	private String count = "0";
	// boolean to set visiblity of the counter
	private boolean isCounterVisible = false;
	
	public LeftMenuItem(){
		
	}
	public LeftMenuItem(String title, int icon) {
		this.title = title;
		this.icon = icon;
	}

	public LeftMenuItem(String title, int icon, boolean isCounterVisible, String count){
		this.title = title;
		this.icon = icon;
		this.setCounterVisible(isCounterVisible);
		this.setCount(count);
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}
	public boolean isCounterVisible() {
		return isCounterVisible;
	}
	public void setCounterVisible(boolean isCounterVisible) {
		this.isCounterVisible = isCounterVisible;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
}
