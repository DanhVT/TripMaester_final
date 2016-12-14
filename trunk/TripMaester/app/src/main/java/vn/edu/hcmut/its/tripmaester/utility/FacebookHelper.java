package vn.edu.hcmut.its.tripmaester.utility;

import android.support.annotation.NonNull;

import java.net.MalformedURLException;

/**
 * Created by thuanle on 12/23/15.
 */
public class FacebookHelper {

    @NonNull
    public static String getFacebookProfileImage(String id) throws MalformedURLException {
        String GRAPH_FB_URL = "https://graph.facebook.com/";
        return GRAPH_FB_URL + id + "/picture";
    }
}
