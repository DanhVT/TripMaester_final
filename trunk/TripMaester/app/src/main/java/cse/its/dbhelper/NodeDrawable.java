package cse.its.dbhelper;

import java.util.ArrayList;

/**
 * @author SinhHuynh
 * @Tag Node ( traffic data type)
 */
public class NodeDrawable {
	private long id;
	private long id_seg;
	public double lat;
	public double lon;
	private double speed;
	private String density;

	public NodeDrawable() {
		lat = 0.0;
		lon = 0.0;
		speed = 0;
		density = "";
	}

	public NodeDrawable(long id, double lon, double lat) {
		this.id = id;
		this.lat = lat;
		this.lon = lon;

	}

	public NodeDrawable(long id, long idseg, double lon, double lat) {
		this.id = id;
		this.id_seg = idseg;
		this.lat = lat;
		this.lon = lon;

	}

	public static void add(ArrayList<NodeDrawable> listNode, double longi,
			double lat) {
		NodeDrawable node = new NodeDrawable(0, longi, lat);
		listNode.add(node);
	}

	public String toString() {
		String s;
		s = "~" + lon + "," + lat + "  ";
		return s;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setIdSeg(long idseg) {
		this.id_seg = idseg;
	}

	public long getId() {
		return id;
	}

	public long getIdSeg() {
		return id_seg;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public String getDensity() {
		return density;
	}

	public void setDensity(String density) {
		this.density = density;
	}

	// ### distance between 2 nodes
	public static double getDistance(NodeDrawable dep, NodeDrawable des) {
		double distance;
		distance = 110000 * Math.sqrt((dep.getLat() - des.getLat())
				* (dep.getLat() - des.getLat()) + (dep.getLon() - des.getLon())
				* (dep.getLon() - des.getLon()));
		return distance;
	}

}
