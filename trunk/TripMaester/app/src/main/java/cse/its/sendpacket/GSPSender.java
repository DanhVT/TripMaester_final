package cse.its.sendpacket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.provider.Settings.Secure;


/**
 * @author SinhHuynh
 * @Tag This class helps to send user's GPS data to ITS server
 */
public class GSPSender extends AsyncTask<Location, Void, Void> {
	String androidID;
	Context context;
	String currentTime;
	String currentDate;

	public GSPSender(Context context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		androidID = Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		currentDate = dateFormat.format(new Date());
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
		currentTime = timeFormat.format(new Date());

	}

	@Override
	protected Void doInBackground(Location... location) {
		DatagramSocket clientSocket;
//		byte[] receiveData = new byte[1024];
		String message = androidID + "," + location[0].getLatitude() + ","
				+ location[0].getLongitude() + ","
				+ (location[0].getSpeed() * 3.6) + ",0.0,0,3,1," + currentDate
				+ " " + currentTime;
		try {
			InetAddress IPAddress = java.net.InetAddress
					.getByName("traffic.hcmut.edu.vn");
//			Log.wtf("GPSSender", IPAddress + " " + message);
			clientSocket = new DatagramSocket();
			DatagramPacket sendPacket = new DatagramPacket(message.getBytes(),
					message.getBytes().length, IPAddress, 180);
//			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length); 
			clientSocket.send(sendPacket);

			clientSocket.close();
		} catch (SocketException | UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
