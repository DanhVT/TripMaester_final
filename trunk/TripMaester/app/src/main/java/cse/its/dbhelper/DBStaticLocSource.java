package cse.its.dbhelper;

import java.io.IOException;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author SinhHuynh
 * @Tag This class helps to query data (Street, way   ID--> name)
 */
public class DBStaticLocSource {
	private SQLiteDatabase database;
	private DBStaticLocHelper dbHelper;

	private String[] SegColumns = { DBStaticLocHelper.ID, DBStaticLocHelper.TYPE,
			DBStaticLocHelper.OBJECTID, DBStaticLocHelper.NAME };

	// Constructor
	public DBStaticLocSource(Context context) {
		dbHelper = new DBStaticLocHelper(context);
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

	public Cursor getStreet() {
		return database.query(DBStaticLocHelper.TABLE, SegColumns, null,
				null, null, null, null);
	}

	public int getCount() {
		return database.query(DBStaticLocHelper.TABLE, SegColumns, null, null,
				null, null, null).getCount();
	}

	public int removeRow(long id) {
		return database.delete(DBStaticLocHelper.TABLE, DBStaticLocHelper.ID + " ="
				+ id, null);
	}

	public Cursor getObjectName(Long streetID) {
		return database.query(DBStaticLocHelper.TABLE, SegColumns,
				DBStaticLocHelper.OBJECTID + " =" + streetID, null, null, null, null);
	}

	public Cursor getObjectName(String type, Long ObjectId) {
		return database.query(DBStaticLocHelper.TABLE, SegColumns,
				DBStaticLocHelper.OBJECTID + " = " + ObjectId + " AND "
						+ DBStaticLocHelper.TYPE + " = " + "'" + type + "'", null, null, null,
				null);
	}

}
