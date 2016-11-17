package vn.edu.hcmut.its.tripmaester.controller;

import vn.edu.hcmut.its.tripmaester.controller.request.RequestTripComment;

/**
 * Created by thuanle on 12/18/15.
 */
public class RequestBuilder {
    public static RequestTripComment.Builder requestCommentTrip() {
        return new RequestTripComment.Builder();
    }
}
