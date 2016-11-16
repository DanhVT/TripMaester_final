package cse.its.dbhelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author SinhHuynh
 * @Tag This class helps to create data (HCMC street,way) tables from assets
 *      folder
 */
public class DBStaticLocHelper extends SQLiteOpenHelper {
	public final static String DB_NAME = "leanstreet.sqlite";

	/* Segment table */
	public static final String ID = "id";
	public static final String TYPE = "type";
	public static final String OBJECTID = "objectid";
	public static final String NAME = "name";
	public static final String TABLE = "street";

	private SQLiteDatabase myDataBase;
	private final Context context;

	// Constructor
	public DBStaticLocHelper(Context context) {
		super(context, DB_NAME, null, 1);
		this.context = context;
	}

	public void createDataBase() throws IOException {
		// If database does not exist, then copy it from the assets
		if (!checkDataBase()) {
			this.getReadableDatabase();
			this.close();
			try {
				// Copy the database from assets
				copyDataBase();
				// Log.e(TAG, "createDatabase database created");
			} catch (IOException mIOException) {
				
				Log.e("copy data", mIOException.getMessage());
			}
		}
	}

	private boolean checkDataBase() {
		File dbFile = context.getDatabasePath(DB_NAME);// new File("/data/data/" + context.getPackageName()
				//+ "/databases/" + DB_NAME);
		// Log.v("dbFile", dbFile + "   "+ dbFile.exists());
		return dbFile.exists();
	}

	// Copy the database from assets
	private void copyDataBase() throws IOException {
		InputStream mInput = context.getAssets().open(DB_NAME);
	
//		String outFileName = "/data/data/" + context.getPackageName()
//				+ "/databases/" + DB_NAME;
		OutputStream mOutput = new FileOutputStream(context.getDatabasePath(DB_NAME));
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
		String mPath = context.getDatabasePath(DB_NAME).getAbsolutePath();// "/data/data/" + context.getPackageName() + "/databases/"
		//		+ DB_NAME;
		// Log.v("mPath", mPath);
		myDataBase = SQLiteDatabase.openDatabase(mPath, null,
				SQLiteDatabase.CREATE_IF_NECESSARY);
		// mDataBase = SQLiteDatabase.openDatabase(mPath,
		// null,SQLiteDatabase.NO_LOCALIZED_COLLATORS);
		return myDataBase != null;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

}