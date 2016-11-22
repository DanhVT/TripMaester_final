package vn.edu.hcmut.its.tripmaester.service.http;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.RequestBody;


/**
 * Created by danh-vo on 04/11/2016.
 */

public class RequestBuilder {
    //Login request body
    public static RequestBody LoginBody(String username, String password) {
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

//    //Upload request body
//    public static MultipartBody uploadRequestBody(String title, String MIME, String type, File file) {
//
//        MediaType MEDIA_TYPE = MediaType.parse(MIME);
//        String format = MIME.split("/")[1];
//        return new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("action", "upload")
//                .addFormDataPart("type", type)
//                .addFormDataPart("device", BasicHelper.getBrand()+", "+BasicHelper.getModelName())
//                .addFormDataPart("name", "Danhvt" ) //e.g. title.png --> imageFormat = png
//                .addFormDataPart("file", title + "." + format, RequestBody.create(MEDIA_TYPE, file))
//                .build();
//    }
}
