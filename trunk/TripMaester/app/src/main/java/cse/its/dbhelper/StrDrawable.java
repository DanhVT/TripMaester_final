package cse.its.dbhelper;

/**
 * @author SinhHuynh
 * @Tag Street (traffic data type)
 */
public class StrDrawable {
	int streetID;
	String streetName;
	String streetType;
	
	public StrDrawable(int _strID, String _strType, String _strName){
		this.streetID = _strID;
		this.streetName = _strName;
		this.streetType = _strType;
	}
}
