package group.traffic.nhn.rightmenu;

public class RightMenuItem {
	private String title;
	private int icon;
	private String count = "0";
	// boolean to set visiblity of the counter
	private boolean isCounterVisible = false;
	public RightMenuItem(){
		
	}
	public RightMenuItem(String title, int icon) {
		this.title = title;
		this.icon = icon;
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
