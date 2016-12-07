package cse.its.dbhelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * @author SinhHuynh
 * @Tag This class helps create data (node, segment, street) tables from assets
 *      folder
 */
public class DBTrafficHelper extends SQLiteOpenHelper {

	public final static String DB_NAME = "gpsTraffic.db";
	// Phiên bản
	private static final int DATABASE_VERSION = 1;

	/* Segment table */
	public static final String SEGMENT = "staticData";
	public static final String SEGMENT_ID = "segment_id";
	public static final String SEGMENT_STREET_ID = "street_id";
	public static final String LATE = "node_end_lat";
	public static final String LONE = "node_end_lon";
	public static final String LATS = "node_start_lat";
	public static final String LONS = "node_start_lon";

	private SQLiteDatabase myDataBase;
	private final Context context;

	// Constructor
	public DBTrafficHelper(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
		this.context = context;
		SQLiteDatabase db = this.getWritableDatabase();
	}

	public void createDataBase() throws IOException {
		// If database does not exist, then copy it from the assets

		if (!checkDataBase()) {
			this.getReadableDatabase();
			this.close();        //DANH_24/11
			try {
				// Copy the database from assets
				copyDataBase();
				// Log.e(TAG, "createDatabase database created");
			} catch (IOException mIOException) {
				throw new Error("ErrorCopyingDataBase");
			}
		}
		else{
			openDataBase();
		}
	}

	private boolean checkDataBase() {
		File dbFile = context.getDatabasePath(DB_NAME);// new File(
														// "/data/data/" +
														// context.getPackageName()
		// + "/databases/" + DB_NAME);
		// Log.v("dbFile", dbFile + "   "+ dbFile.exists());

		return dbFile.exists();
	}

	// Copy the database from assets
	private void copyDataBase() throws IOException {
		InputStream mInput = context.getAssets().open(DB_NAME);
		// String outFileName = "/data/data/" + context.getPackageName()
		// + "/databases/" + DB_NAME;
		OutputStream mOutput = new FileOutputStream(
				context.getDatabasePath(DB_NAME));
		byte[] mBuffer = new byte[1024];
		int mLength;
		while ((mLength = mInput.read(mBuffer)) > 0) {
			mOutput.write(mBuffer, 0, mLength);
		}
		mOutput.flush();
		mOutput.close();
		mInput.close();
	}

	public boolean openDataBase() throws SQLException {
		String mPath = context.getDatabasePath(DB_NAME).getAbsolutePath();// "/data/data/"
																			// +
																			// context.getPackageName()
		// + "/databases/" + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(mPath, null,
				SQLiteDatabase.CREATE_IF_NECESSARY);
		// mDataBase = SQLiteDatabase.openDatabase(mPath,
		// null,SQLiteDatabase.NO_LOCALIZED_COLLATORS);
		return myDataBase != null;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		String drop_segment_table = String.format("DROP TABLE IF EXISTS "+ SEGMENT);
		db.execSQL(drop_segment_table);


		db.execSQL("DROP TABLE IF EXISTS " + SEGMENT);



	}

	@Override
	public void onCreate(SQLiteDatabase db) {

//		openDataBase();
		String create_segment_table = "CREATE TABLE "+ SEGMENT+" ( "+ SEGMENT_ID +" STRING PRIMARY KEY, "+ SEGMENT_STREET_ID+" LONG, "+ LATE+" DOUBLE,"+ LONE +" DOUBLE, "+ LATS+ " DOUBLE, "+ LONS+ " DOUBLE)";
		db.execSQL(create_segment_table);

	}

}