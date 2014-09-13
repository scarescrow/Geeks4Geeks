package scarecrow.beta.geeks4geeks;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

	// The Android's default system path of your application database.
	@SuppressLint("SdCardPath")
	private static String DB_PATH = "/data/data/scarecrow.beta.geeks4geeks/databases/";

	private static String DB_NAME = "links";
	private static String Table_Algo = "algorithms";
	private static String Table_DS = "data_structures";

	private SQLiteDatabase myDataBase;

	private static final String[] COLUMNS = { "id", "Topic", "Subtopic",
			"Link", "Code" };

	private final Context myContext;

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */
	public DataBaseHelper(Context context) {

		super(context, DB_NAME, null, 1);
		this.myContext = context;
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist) {
			// do nothing - database already exist
		} else {

			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			this.getReadableDatabase();

			try {

				copyDataBase();

			} catch (IOException e) {

				throw new Error("Error copying database");

			}
		}

	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {

			// database does't exist yet.

		}

		if (checkDB != null) {

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {

		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public void openDataBase() throws SQLException {

		// Open the database
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY);
	}

	@Override
	public synchronized void close() {

		if (myDataBase != null)
			myDataBase.close();

		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public String[] search_algo(String topic) {

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor;
		String[] details = { "", "", "", ""};
		topic = topic.replace("'", "&#8217;");
		cursor = db.query(Table_Algo, new String[] { COLUMNS[0], COLUMNS[1],
				COLUMNS[2], COLUMNS[3], COLUMNS[4] }, COLUMNS[1] + " LIKE ?",
				new String[] { "%" + topic + "%" }, null, null, null, null);
		
		if (cursor.moveToNext() && cursor.getCount() > 0) {
			details[0] = cursor.getString(1);
			details[1] = cursor.getString(2);
			details[2] = cursor.getString(3);
			details[3] = cursor.getString(4);
		}

		cursor.close();
		db.close();
		
		return details;
	}

	public String[] search_ds(String topic) {

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor;
		String[] details = { "", "", "", ""};
		topic = topic.replace("'", "&#8217;");
		cursor = db.query(Table_DS, new String[] { COLUMNS[0], COLUMNS[1],
				COLUMNS[2], COLUMNS[3], COLUMNS[4] }, COLUMNS[1] + " LIKE ?",
				new String[] { "%" + topic + "%" }, null, null, null, null);

		if (cursor.moveToNext() && cursor.getCount() > 0) {
			details[0] = cursor.getString(1);
			details[1] = cursor.getString(2);
			details[2] = cursor.getString(3);
			details[3] = cursor.getString(4);
		}

		cursor.close();
		db.close();

		return details;
	}

	public List<String> getAllSubtopics(String table) {
		List<String> subtopics = new ArrayList<String>();

		// Select All Query
		String selectQuery = "SELECT DISTINCT Subtopic FROM " + table;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst() && cursor.getCount() > 0) {
			do {
				subtopics.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}

		// closing connection
		cursor.close();
		db.close();

		// returning lables
		return subtopics;
	}

	public List<String> getAllTopics(String table, String Subtopic) {
		List<String> topics = new ArrayList<String>();

		// Select All Query
		String selectQuery = "SELECT  Topic FROM " + table
				+ " WHERE Subtopic LIKE '%" + Subtopic + "%'";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				topics.add(cursor.getString(0).replace("&#8217;", "'"));
			} while (cursor.moveToNext());
		}

		// closing connection
		cursor.close();
		db.close();

		// returning lables
		return topics;
	}

}
