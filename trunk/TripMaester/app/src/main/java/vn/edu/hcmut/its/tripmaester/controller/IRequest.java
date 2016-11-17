package vn.edu.hcmut.its.tripmaester.controller;

/**
 * Created by thuanle on 12/17/15.
 */
public interface IRequest<C extends ICallback> {
    C getCallback();

    Object getTag();

    void post();

    void setCallback(C callback);

    void setTag(Object tag);
}
