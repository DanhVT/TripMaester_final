package cse.its.dbhelper;

import java.io.IOException;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author SinhHuynh
 * @Tag This class helps to query data(node, segment, street  ID--> coordinate)
 */
public class DBTrafficSource {
	private SQLiteDatabase database;
	private DBTrafficHelper dbHelper;
	
	private String[] SegColumns = { DBTrafficHelper.SEGMENT_ID,
		      DBTrafficHelper.LATE,DBTrafficHelper.LONE,
		      DBTrafficHelper.LATS,DBTrafficHelper.LONS,
		      DBTrafficHelper.SEGMENT_STREET_ID};
	//Constructor	
	public DBTrafficSource(Context context) {
	    dbHelper = new DBTrafficHelper(context);
	    try {
	    	dbHelper.createDataBase();
	    	this.open();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	public void close() {
	    dbHelper.close();
	  }
	public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	}

	public Cursor getSeg(String segID){
		Cursor cursor = database.query(DBTrafficHelper.SEGMENT,
		        SegColumns, DBTrafficHelper.SEGMENT_ID + " IN" + segID, null, null, null, null);
		return cursor;
	}
	public Cursor getSegbyStreetID(Long streetID){
//		database = dbHelper.getReadableDatabase();
		Cursor cursor = database.query(DBTrafficHelper.SEGMENT,
		        SegColumns, DBTrafficHelper.SEGMENT_STREET_ID + " =" + streetID, null, null, null, null);

		return cursor;
	}
	
	public Cursor getSegmentbyID( String ID){
		Cursor cursor = database.query(DBTrafficHelper.SEGMENT,
		        SegColumns, DBTrafficHelper.SEGMENT_ID + " =" + ID, null, null, null, null);
		return cursor;
	}
	
}
