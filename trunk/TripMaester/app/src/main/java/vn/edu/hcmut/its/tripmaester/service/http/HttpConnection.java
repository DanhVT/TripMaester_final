package vn.edu.hcmut.its.tripmaester.service.http;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by danh-vo on 06/12/2016.
 */

public class HttpConnection {
    private final static int TIMEOUT_CONNECTION = 3000; //ms
    private final static int TIMEOUT_SOCKET = 10000; //ms

    private static OkHttpClient client;
    private InputStream stream;
    private String mUserAgent;
    private Response response;

    private static OkHttpClient getOkHttpClient() {
        if (client == null) {
            OkHttpClient.Builder b = new OkHttpClient.Builder();
            b.connectTimeout(TIMEOUT_CONNECTION, TimeUnit.MILLISECONDS);
            b.readTimeout(TIMEOUT_SOCKET, TimeUnit.MILLISECONDS);

            client = b.build();
            /*
            client.setConnectTimeout(TIMEOUT_CONNECTION, TimeUnit.MILLISECONDS);
            client.setReadTimeout(TIMEOUT_SOCKET, TimeUnit.MILLISECONDS);
            */
        }
        return client;
    }

    public InputStream getStream() {
        if (response == null)
            return null;
        stream = response.body().byteStream();
        return stream;
    }

    public void doGet(String url) {
        try {
            Request.Builder request = new Request.Builder().addHeader("User-Agent", "OkHttp Headers.java").addHeader("Accept", "application/json; q=0.5")
                    .addHeader("Accept", "application/vnd.github.v3+json").url(url);
            if (mUserAgent != null)
                request.addHeader("User-Agent", mUserAgent);
            response = getOkHttpClient().newCall(request.build()).execute();
            Integer status = response.code();
            if (status != 200) {
                Log.e("HTTP CONECTION", "Invalid response from server: " + status.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getContentAsString(){
        try {
            if (response == null)
                return null;
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        if (stream != null) {
            try {
                stream.close();
                stream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
