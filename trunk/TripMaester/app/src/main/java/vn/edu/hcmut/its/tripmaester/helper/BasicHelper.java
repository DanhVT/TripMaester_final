package vn.edu.hcmut.its.tripmaester.helper;

import android.os.Environment;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by AnTD on 11/18/2016.
 */

public class BasicHelper {
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
    public static String getBrand() {
        String brand;
        try {
            brand = android.os.Build.BRAND;
        } catch (Exception exception) {
            //Log.e(DEBUG_TAG, exception);
            exception.printStackTrace();
            brand = "Unknown";
        }

        return brand;
    }
    public static String getModelName() {
        String modelName;
        try {
            modelName = android.os.Build.MODEL;
        } catch (Exception exception) {
            //Log.e(DEBUG_TAG, exception);
            exception.printStackTrace();
            modelName = "Unknown";
        }

        return modelName;
    }
    public static File createImageFile() throws IOException {
        boolean externalStorageAvailable = false;
        boolean externalStorageWriteable = false;
        File root = null;
        File tempFile = null;
        try {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                externalStorageAvailable = externalStorageWriteable = true;
            } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                externalStorageAvailable = true;
                externalStorageWriteable = false;
            } else {
                externalStorageAvailable = externalStorageWriteable = false;
            }

            if (externalStorageAvailable && externalStorageWriteable) {
                root = new File(Environment.getExternalStorageDirectory(), "mani");
                if (!root.exists()) {
                    root.mkdirs();
                }
                // Create an image file name
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "Image_" + timeStamp + "_";
                tempFile = File.createTempFile(imageFileName,".jpg",root);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempFile;
    }

    public static File createVideoFile() throws IOException {
        boolean externalStorageAvailable = false;
        boolean externalStorageWriteable = false;
        File root = null;
        File tempFile = null;
        try {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                externalStorageAvailable = externalStorageWriteable = true;
            } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                externalStorageAvailable = true;
                externalStorageWriteable = false;
            } else {
                externalStorageAvailable = externalStorageWriteable = false;
            }

            if (externalStorageAvailable && externalStorageWriteable) {
                root = new File(Environment.getExternalStorageDirectory(), "mani");
                if (!root.exists()) {
                    root.mkdirs();
                }
                // Create an image file name
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());
                String imageFileName = "Video_" + timeStamp + "_";
                tempFile = File.createTempFile(imageFileName,".mp4",root);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempFile;
    }
    public  static String getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd_HHmmss");
        return sdf.format(cal.getTime());
    }
}
