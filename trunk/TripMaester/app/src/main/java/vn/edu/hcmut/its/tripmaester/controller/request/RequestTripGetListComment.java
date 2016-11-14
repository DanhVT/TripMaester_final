package vn.edu.hcmut.its.tripmaester.controller.request;

import java.util.ArrayList;
import java.util.List;

import group.traffic.nhn.message.MessageItem;
import vn.edu.hcmut.its.tripmaester.controller.ICallback;

/**
 * // TODO: 12/18/15 UNCOMPLETED
 * Created by thuanle on 12/17/15.
 */
public class RequestTripGetListComment extends BaseRequest<ICallback<List<MessageItem>>> {
    private ArrayList<String> mContents;
    private String tripId;

    public ArrayList<String> getContents() {
        return mContents;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getTripId() {
        return tripId;
    }

    public static class Builder {
        private String mTripId = null;
        private ICallback<List<MessageItem>> mCallback;
        private Object mTag;

        private RequestTripGetListComment build() {
            RequestTripGetListComment request = new RequestTripGetListComment();
            request.setTag(mTag);
            request.setTripId(mTripId);
            request.setCallback(mCallback);
            return request;
        }

        public void setCallback(ICallback<List<MessageItem>> callback) {
            mCallback = callback;
            build().post();
        }

        public Builder setTag(Object tag) {
            mTag = tag;
            return this;
        }
    }
}
