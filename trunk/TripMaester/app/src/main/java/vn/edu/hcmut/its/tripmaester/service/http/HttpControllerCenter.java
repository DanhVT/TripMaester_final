package vn.edu.hcmut.its.tripmaester.service.http;

import vn.edu.hcmut.its.tripmaester.controller.ControllerCenter;
import vn.edu.hcmut.its.tripmaester.controller.manager.LoginManager;

/**
 * Created by thuanle on 12/18/15.
 */
public class HttpControllerCenter {

    private static HttpControllerCenter _INSTANCE;

    public static HttpControllerCenter getInstance() {
        if (_INSTANCE == null) {
            synchronized (HttpControllerCenter.class) {
                if (_INSTANCE == null) {
                    _INSTANCE = new HttpControllerCenter();
                }
            }
        }
        return _INSTANCE;
    }


    @Deprecated
    public String getLoginToken() {
        // TODO: 12/18/15
        return LoginManager.getInstance().getUser().getTokenId();
    }

    public void registerControllers() {
        ControllerCenter cc = ControllerCenter.getInstance();

        cc.registerController(new CommentTripController());
    }
}
