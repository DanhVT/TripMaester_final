package group.traffice.nhn.common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.LangUtils;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import android.location.Location;
import android.os.Looper;
import android.util.Log;

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
