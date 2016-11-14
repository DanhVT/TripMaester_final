package group.traffic.nhn.user;

import android.app.Activity;

import vn.edu.hcmut.its.tripmaester.R;

/**
 * Model sao lại chứa Activity?
 */
@Deprecated
public class FriendItem {
    private String content;
    private int icon;
    private String invitemessage = "";
    private String _fb_id;
    private String friendName = "";
    // boolean to set visiblity of the counter
    private boolean isInviteVisible = false;
    private boolean isChecked;

    public FriendItem() {

    }

    public FriendItem(String title, int icon) {
        this.setContent(title);
        this.icon = icon;
    }

    public FriendItem(String title, int icon, boolean visible) {
        this.setContent(title);
        this.icon = icon;
        this.isInviteVisible = visible;
    }

    public FriendItem(Activity act, String title, int icon, boolean visible) {
        this.setContent(title);
        this.icon = icon;
        this.isInviteVisible = visible;
        if (visible && null != act) {
            invitemessage = act.getResources().getString(R.string.msg_invite_install);
        }
    }

    public FriendItem(Activity act, String title, String fb_id, boolean visible, String friendName) {
        this.setContent(title);
        this._fb_id = fb_id;
        this.isInviteVisible = visible;
        this.setFriendName(friendName);
        if (visible && null != act) {
            invitemessage = act.getResources().getString(R.string.msg_invite_install);
        }
    }

    public String getContent() {
        return content;
    }

    public String getFBId() {
        return this._fb_id;
    }

    public String getFriendName() {
        return friendName;
    }

    public int getIcon() {
        return icon;
    }

    public String getInvitemessage() {
        return invitemessage;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public boolean isInviteVisible() {
        return isInviteVisible;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setInviteVisible(boolean isInviteVisible) {
        this.isInviteVisible = isInviteVisible;
    }

    public void setInvitemessage(String invitemessage) {
        this.invitemessage = invitemessage;
    }
}

