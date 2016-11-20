package vn.edu.hcmut.its.tripmaester.controller.manager;

import com.facebook.AccessToken;

import group.traffic.nhn.user.User;

/**
 * Created by thuanle on 12/25/15.
 */
public class LoginManager {
    public User mUser = new User();
    private static LoginManager _INSTANCE;
    private AccessToken mUserToken;

    private LoginManager() {
    }

    public static LoginManager getInstance() {
        if (_INSTANCE == null) {
            synchronized (LoginManager.class) {
                if (_INSTANCE == null) {
                    _INSTANCE = new LoginManager();
                }
            }
        }
        return _INSTANCE;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public User getUser() {
        return mUser;
    }

    public AccessToken getUserToken() {
        return mUserToken;
    }

    public boolean isLogin() {
        return mUserToken != null;
    }

    public void setUserToken(AccessToken userToken) {
        mUserToken = userToken;
    }
}
