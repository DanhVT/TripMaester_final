package vn.edu.hcmut.its.tripmaester.service.http;

import java.io.File;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import vn.edu.hcmut.its.tripmaester.controller.manager.LoginManager;
import vn.edu.hcmut.its.tripmaester.helper.BasicHelper;

/**
 * Created by AnTD on 11/18/2016.
 */

public class RequestBuilder {
    //Login request body
    public static okhttp3.RequestBody LoginBody(String username, String password) {
        return new FormBody.Builder()
                .add("action", "login")
                .add("format", "json")
                .add("email", username)
                .add("password", password)
                .build();
    }

    public static HttpUrl buildURL() {
        return new HttpUrl.Builder()
                .scheme("https") //http
                .host("www.somehostname.com")
                .addPathSegment("pathSegment")//adds "/pathSegment" at the end of hostname
                .addQueryParameter("param1", "value1") //add query parameters to the URL
                .addEncodedQueryParameter("encodedName", "encodedValue")//add encoded query parameters to the URL
                .build();
    }

    // Picture of the day RSS feed
    public static HttpUrl pictureOfTheDay() {
        return new HttpUrl.Builder()
                .scheme("https")
                .host("commons.wikimedia.org")
                .addPathSegment("w")
                .addPathSegment("api.php")
                .addQueryParameter("action", "featuredfeed")
                .addQueryParameter("format", "xml")
                .addQueryParameter("feed", "potd")
                .build();
    }

    //Upload request body
    public static MultipartBody uploadRequestBody(String filePath, String MIME, String POINT_ID,  File file) {

        MediaType MEDIA_TYPE = MediaType.parse(MIME);
        String format = MIME.split("/")[1];
        return new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("filename", filePath)
                .addFormDataPart("pointId", POINT_ID)
                .addFormDataPart("tokenId", LoginManager.getInstance().getUser().getTokenId())
                .addFormDataPart("dataImage", filePath + "." + format, RequestBody.create(MEDIA_TYPE, file))
                .build();
    }
}
