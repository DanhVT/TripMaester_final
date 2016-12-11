package group.traffice.nhn.common;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utilities {


	public static String readStringFromInputStream(InputStream inputStream) {
		StringBuilder total = new StringBuilder();
		try {
			BufferedReader r = new BufferedReader(new InputStreamReader(
					inputStream));

			String line;
			while ((line = r.readLine()) != null) {
				total.append(line);
			}
		} catch (Exception ex) {
			Log.i("read_Input_Stream", ex.getMessage());
		}

		return total.toString();
	}

}
