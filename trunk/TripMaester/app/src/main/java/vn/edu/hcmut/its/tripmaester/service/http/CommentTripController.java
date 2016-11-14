package vn.edu.hcmut.its.tripmaester.service.http;

import com.google.gson.Gson;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import vn.edu.hcmut.its.tripmaester.TMApp;
import vn.edu.hcmut.its.tripmaester.controller.BaseController;
import vn.edu.hcmut.its.tripmaester.controller.IRequest;
import vn.edu.hcmut.its.tripmaester.controller.request.RequestTripComment;
import vn.edu.hcmut.its.tripmaester.service.response.BaseHttpResponse;

/**
 * Created by thuanle on 12/17/15.
 * Change the result to Model, not JSONObject
 */
public class CommentTripController extends BaseController<RequestTripComment, BaseHttpResponse> {
    public static final String FORM_CONTENT = "content";
    static final String URL_SAVE_COMMENT_TRIP = HttpConstants.HOST_NAME + "/ITS/rest/comment/SaveCommentOnTrip";
    private static final Class[] REQUESTS = {RequestTripComment.class};

    @Override
    public Class[] getHandledRequests() {
        return REQUESTS;
    }

    @Override
    public void handleRequestInBackground(final IRequest request) {
        final RequestTripComment req = (RequestTripComment) request;
        Ion.with(TMApp.getInstance())
                .load(CommentTripController.URL_SAVE_COMMENT_TRIP)
                .setBodyParameter(HttpConstants.FORM_COMMON_TOKEN_ID, HttpControllerCenter.getInstance().getLoginToken())
                .setBodyParameter(HttpConstants.FORM_COMMON_TRIP_ID, req.getTripId())
                .setBodyParameter(FORM_CONTENT, req.getContent())
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e == null) {
                            try {
                                Gson gson = new Gson();
                                CommentTripResponse response = gson.fromJson(result, CommentTripResponse.class);
                                deliverResult(req, response);
                            } catch (Exception ee) {
                                deliverException(req, ee);
                            }
                        } else {
                            deliverException(req, e);
                        }
                    }
                });
    }
}
