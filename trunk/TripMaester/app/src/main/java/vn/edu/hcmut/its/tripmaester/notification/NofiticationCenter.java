package vn.edu.hcmut.its.tripmaester.notification;


import de.greenrobot.event.EventBus;

/**
 * Created by thuanle on 12/17/15.
 */
public class NofiticationCenter {
    private static NofiticationCenter _INSTANCE;

    public static NofiticationCenter getInstance() {
        if (_INSTANCE == null) {
            synchronized (NofiticationCenter.class) {
                if (_INSTANCE == null) {
                    _INSTANCE = new NofiticationCenter();
                }
            }
        }
        return _INSTANCE;
    }

    private NofiticationCenter(){
        EventBus mEventBus = new EventBus();
    }
}
