package vn.edu.hcmut.its.tripmaester.utility;

import android.support.annotation.NonNull;

import java.net.MalformedURLException;

/**
 * Created by thuanle on 12/23/15.
 */
public class FacebookHelper {
    private static String GRAPH_FB_URL = "https://graph.facebook.com/";

    @NonNull
    public static String getFacebookProfileImage(String id) throws MalformedURLException {
        return GRAPH_FB_URL + id + "/picture";
    }
}
