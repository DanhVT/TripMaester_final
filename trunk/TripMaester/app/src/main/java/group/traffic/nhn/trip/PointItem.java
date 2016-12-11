/**
 * 
 */
package group.traffic.nhn.trip;

/**
 * @author Thanh
 *
 */

@SuppressWarnings("serial")
public class PointItem{
	//TODO
	int x_lat;//lat 1E6
	int y_long;//long 1E6
	double z;
	double fromZ;
	String pointDescription;
	String order;
	
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public int getX_Lat() {
		return x_lat;
	}
	public void setX_Lat(int x) {
		this.x_lat = x;
	}
	public int getY_Long() {
		return y_long;
	}
	public void setY_Long(int y) {
		this.y_long = y;
	}
	public double getZ() {
		return z;
	}
	public void setZ(float z) {
		this.z = z;
	}
	public double getFromZ() {
		return fromZ;
	}
	public void setFromZ(float fromZ) {
		this.fromZ = fromZ;
	}
	public String getPointDescription() {
		return pointDescription;
	}
	public void setPointDescription(String pointDescription) {
		this.pointDescription = pointDescription;
	}
}
