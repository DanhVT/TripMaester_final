package vn.edu.hcmut.its.tripmaester.utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by thuanle on 10/15/15.
 */
public class GraphicUtils {

    // convert image to string base64
    public static String convertImage2Base64(String path) {
        Bitmap mBitmap = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream mByteArrayOutputStream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, mByteArrayOutputStream);
        byte[] mByteArray = mByteArrayOutputStream.toByteArray();
        return Base64.encodeToString(mByteArray, Base64.DEFAULT);
    }

    public static String convertVideo2Base64(String path){
        File tempFile = new File(path);
        String encodedString = null;

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(tempFile);
        } catch (Exception e) {
            // TODO: handle exception
        }

        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes = output.toByteArray();
        encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
        Log.i("StringVideo", encodedString);
        return encodedString;
    }
}
