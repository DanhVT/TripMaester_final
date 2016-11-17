package vn.edu.hcmut.its.tripmaester.controller.request;

import vn.edu.hcmut.its.tripmaester.controller.ControllerCenter;
import vn.edu.hcmut.its.tripmaester.controller.ICallback;
import vn.edu.hcmut.its.tripmaester.controller.IRequest;

/**
 * Created by thuanle on 12/17/15.
 */
public abstract class BaseRequest<T extends ICallback> implements IRequest<T> {
    private Object mTag;
    private T mCallback;

    @Override
    public T getCallback() {
        return mCallback;
    }

    @Override
    public Object getTag() {
        return mTag;
    }

    @Override
    public void post() {
        ControllerCenter.getInstance().doRequest(this);
    }

    @Override
    public void setCallback(T callback) {
        mCallback = callback;
    }

    @Override
    public void setTag(Object tag) {
        mTag = tag;
    }
}
