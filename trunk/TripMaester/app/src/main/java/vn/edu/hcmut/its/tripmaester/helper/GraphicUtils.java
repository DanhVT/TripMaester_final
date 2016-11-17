package vn.edu.hcmut.its.tripmaester.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

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
}
