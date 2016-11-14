package vn.edu.hcmut.its.tripmaester.controller.request;

import vn.edu.hcmut.its.tripmaester.controller.ICallback;

/**
 * Created by thuanle on 12/17/15.
 */
public abstract class BaseHttpTripRequest<T extends ICallback> extends BaseHttpRequest<T> {
    private String mTripId;

    public String getTripId() {
        return mTripId;
    }

    public void setTripId(String tripId) {
        mTripId = tripId;
    }
}
