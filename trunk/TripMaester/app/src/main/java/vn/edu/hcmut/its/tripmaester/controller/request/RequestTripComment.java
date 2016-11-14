package vn.edu.hcmut.its.tripmaester.controller.request;

import vn.edu.hcmut.its.tripmaester.controller.ICallback;
import vn.edu.hcmut.its.tripmaester.service.response.BaseHttpResponse;

/**
 * Created by thuanle on 12/17/15.
 */
public class RequestTripComment extends BaseHttpTripRequest<ICallback<BaseHttpResponse>> {
    private String mContent;

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public static class Builder {
        private String mTripId = null;
        private String mContent = null;
        private ICallback<BaseHttpResponse> mCallback;
        private Object mTag;

        private RequestTripComment build() {
            RequestTripComment request = new RequestTripComment();
            request.setTag(mTag);
            request.setTripId(mTripId);
            request.setContent(mContent);
            request.setCallback(mCallback);
            return request;
        }

        public void setCallback(ICallback<BaseHttpResponse> callback) {
            mCallback = callback;
            build().post();
        }

        public Builder setContent(String content) {
            mContent = content;
            return this;
        }

        public Builder setTag(Object tag) {
            mTag = tag;
            return this;
        }

        public Builder setTripId(String tripId) {
            mTripId = tripId;
            return this;
        }
    }
}
