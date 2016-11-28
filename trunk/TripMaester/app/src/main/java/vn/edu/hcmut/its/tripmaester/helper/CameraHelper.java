package vn.edu.hcmut.its.tripmaester.helper;

import android.os.Environment;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by danh-vo on 28/11/2016.
 */

public class CameraHelper {
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
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
                String randome = UUID.randomUUID().toString().replaceAll("-", "");
                String imageFileName = "Image_" + randome;
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
                String randome = UUID.randomUUID().toString().replaceAll("-", "");
                String imageFileName = "Video_" + randome + "_";
                tempFile = File.createTempFile(imageFileName,".mp4",root);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempFile;
    }
}
