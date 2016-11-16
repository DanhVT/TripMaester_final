package group.traffic.nhn.asynctask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings.Secure;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import group.traffice.nhn.common.Constants;
/**
 * This class helps to send current location to server
 * @author Vo tinh
 *
 */
public class LocationSender extends AsyncTask<String, Void, Void> {
	private String TAG ="LOCATION SENDER";
	String mAndroidId;
	Context mContext;
	String currentTime;
	String currentDate;

	public LocationSender(Context context) {
		this.mContext = context;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mAndroidId = Secure.getString(mContext.getContentResolver(),Secure.ANDROID_ID);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		currentDate = dateFormat.format(new Date());
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		currentTime = timeFormat.format(new Date());

	}

	@Override
	protected Void doInBackground(String... mess) {
		DatagramSocket clientSocket;
		String message = mAndroidId + " " + mess[0] + currentDate + " " + currentTime;
		try {
			
			byte[] sendData = new byte[1024];
			sendData = message.getBytes();
			InetAddress IPAddress = InetAddress.getByName(Constants.DOMAIN_NAME);
			clientSocket = new DatagramSocket();
			DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length, IPAddress, Constants.SERVER_PORT);
			clientSocket.send(sendPacket);
			clientSocket.close();
			
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
		
		return null;
	}

}
