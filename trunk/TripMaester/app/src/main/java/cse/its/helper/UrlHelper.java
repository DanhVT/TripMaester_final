package cse.its.helper;

import group.traffice.nhn.common.StaticVariable;

import java.net.URLDecoder;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import cse.its.dbhelper.NodeDrawable;
import cse.its.dbhelper.WarningDrawable;
import cse.its.parser.RouteParser;

/**
 * @author SinhHuynh To build URLs which was used to get traffic data from TIS
 *         or GOOGLE server
 */
public class UrlHelper {
	

	String url;
	NodeDrawable start;
	NodeDrawable destination;

	public UrlHelper(NodeDrawable start, NodeDrawable destination) {
		this.start = start;
		this.destination = destination;
	}

	/**
	 * To Create URL which was used to get traffic data
	 * 
	 * @param routingMode
	 *            : the algorithm was used to find the path
	 * @param transportation
	 *            : the type of transport (car, motor, bus, taxi,...)
	 * @param apiMode
	 *            : the service was used (TIS or google)
	 * @return the full path request URL
	 */
	public String getUrl(int routingMode, int transportation, int apiMode) {

		// this is TIS service
		if (apiMode == RouteParser.ITS_API_MODE) {
			switch (TypeOfTransport.getTransportType(transportation)) {
			case CAR: {
				url = Constant.ITS + "car/";
				break;
			}
			case MOTOR: {
				url = Constant.ITS + "motor/";
				break;
			}
			case BUS: {
				url = Constant.ITS + "bus/";
				break;
			}
			case TRUCK: {
				url = Constant.ITS + "truck/";
				break;
			}
			default: {
				url = Constant.ITS + "motor/";
				break;
			}
			}


			switch (routingMode) {
			case ModeHelper.DISTANCE_MODE: {
				url += Constant.DISTANCE;
				break;
			}
			case ModeHelper.MULTI_POINT_PATH:{
				//http://traffic.hcmut.edu.vn/ITS/rest/motor/multiple_points/testuser/
				url += "multiple_points/testuser/";
				break;
			}
			case ModeHelper.REAL_TIME_MODE: {
				url += Constant.REAL_TIME;
				break;
			}
			case ModeHelper.PREFERENT_REAL_TIME:
				url +=Constant.PREFERENT_USER + "testuser/";
				break;
			default: {
				url += Constant.DISTANCE;
			}
			
//			case ModeHelper.STATIC_TIME_MODE: {
//				url += STATIC_TIME;
//				break;
//			}
//			case ModeHelper.PROFILE_TIME_MODE: {
//				url += Constant.PROFILE_TIME;
//			break;
//			}
			
			}	

			
			addNodeToUrl();
		} else {
			url = Constant.GOOGLE_ROUTING_API + "origin=" + start.getLat() + ","
					+ start.getLon() + "&waypoints=" + destination.getLat()
					+ "," + destination.getLon();
		}
		return url;
	}

	/**
	 * include data to url
	 */
	public void addNodeToUrl() {
		if(StaticVariable.MULTIPLE_POINT){
			url += createParameterForMultiplePointPath();
		}else{
			url += start.getLat() + "/" + start.getLon() + "/" + start.getId() + "/";
			url += destination.getLat() + "/" + destination.getLon() + "/" + destination.getId() + "/";
		}
	}
	
	/**
	 * create parameters for multiple point request
	 * @return
	 */
	private String createParameterForMultiplePointPath(){
		JSONArray paraArray = new JSONArray();
		String result ="";
		try{
			int size = StaticVariable.LIST_MULTIPLE_POINT.size();
			if(StaticVariable.NAVIGATE){
				JSONObject obj = new JSONObject();
				
				obj.put("id", String.valueOf(StaticVariable.START_NODE.getId()));
				obj.put("lat", StaticVariable.START_NODE.getLat());
				obj.put("lng", StaticVariable.START_NODE.getLon());
				paraArray.put(obj); //put to array paramemter
			}
			for(int i = 0; i < size; i++){
				JSONObject obj = new JSONObject();
				NodeDrawable node = StaticVariable.LIST_MULTIPLE_POINT.get(i);
				obj.put("lat", node.getLat());
				obj.put("lng", node.getLon());
				obj.put("id", String.valueOf(node.getId()));
				paraArray.put(obj); //put to array paramemter
			}
			
			result = URLEncoder.encode(paraArray.toString()); 
		}
		catch(Exception e){
			Log.e("create parameters", e.getMessage());
		}
		
		return result;
	}
	
	public String getUrlWarning(int warningtype){
		return Constant.ROOT + "rest/policeWarning";
	}
	
}
