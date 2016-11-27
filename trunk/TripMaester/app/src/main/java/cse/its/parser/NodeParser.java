package cse.its.parser;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import group.traffic.nhn.map.DynamicOverlay;
import okhttp3.OkHttpClient;
import vn.edu.hcmut.its.tripmaester.helper.ApiCall;

/**
 * @author SinhHuynh
 * @Tag GET NODE (coordinate) from a way(street) ID, only used in case that ID does not exist in local db, ITS service
 */
public class NodeParser extends AsyncTask<String, Void, Void> {
	Context context;
	MapView mapView;
	private final OkHttpClient client = new OkHttpClient();
	public NodeParser(Context context, MapView mapView){
		this.context = context;
		this.mapView = mapView;
	}

	@Override
	protected Void doInBackground(String... params) {
		
		Log.i("Url: ", params[0]);
		String url = params[0];
		try {
			String xml = ApiCall.GET(client, url);
			InputStream is = new ByteArrayInputStream(xml.getBytes());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);

			Element element =doc.getDocumentElement();
			element.normalize();

			NodeList nList = doc.getElementsByTagName("node");
			if(nList.getLength() > 0){
				Element element1 = (Element) nList.item(0);
				double lon = Double.parseDouble(element1.getAttribute("lon"));
				double lat = Double.parseDouble(element1.getAttribute("lat"));
				SearchParser.searchLoc = new DynamicOverlay(context, lon, lat, mapView, false);
				mapView.getOverlays().add(SearchParser.searchLoc);
				mapView.getController().animateTo(new GeoPoint(lat, lon));
			}
			else{
				Toast.makeText(context, "Not found node", Toast.LENGTH_LONG).show();
			}

		}catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return null;
	
	}

	@Override
	protected void onPostExecute(Void result) {
		mapView.invalidate();
		super.onPostExecute(result);
	}

}
