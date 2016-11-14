package group.traffice.nhn.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * This class helps to write location to file
 * @author Vo tinh
 *
 */
public class LogFile {
	private static final String TAG = "FOLLOW TRIP LOG";
	
	private Context mContext;
	private static File mLocationFile;

	
	public LogFile(Context context) {
		this.mContext = context;
		// create a File object for the parent directory
		File navigateDirectory = new File(Environment.getExternalStorageDirectory().getPath() + Constants.FOLDER_FOLLOW_TRIP);
		
		// have the object build the directory structure, if needed.
		if(!navigateDirectory.exists())
			navigateDirectory.mkdirs();
		
		// create a File object for the output file
		mLocationFile = new File(navigateDirectory, Constants.FILE_FOLLOW_TRIP);
		try {
			if (!mLocationFile.exists())
				mLocationFile.createNewFile();
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	public static void writeToFile(String data) {
		try {

			FileOutputStream fOut = new FileOutputStream(mLocationFile, true);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fOut);
			outputStreamWriter.write(data);
			outputStreamWriter.append("\n");
			outputStreamWriter.close();
		} catch (IOException e) {
			Log.e(TAG, "File write failed: " + e.toString());
		}

	}
}
