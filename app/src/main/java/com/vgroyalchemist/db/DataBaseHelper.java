package com.vgroyalchemist.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class DataBaseHelper extends SQLiteOpenHelper {

	// The Android's default system path of your application database.
	private String DB_PATH;
	private String DB_NAME;
	private SQLiteDatabase myDataBase;
	private final Context myContext;
	private static final int DB_VERSION = 4;

	/**
	 * Constructor Takes and keeps a reference of the passed context in
	 * order to access to the application assets and resources.
	 * 
	 * @param context
	 *            is Application context
	 * @param dbName
	 *            is Database Name
	 * @param packageName
	 *            is name of Application package
	 */
	public DataBaseHelper(Context context, String dbName, String packageName) {
		super(context, dbName, null, DB_VERSION);
		DB_NAME = dbName;
		DB_PATH = "/data/data/" + packageName + "/databases/";
		this.myContext = context;
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() {
		boolean dbExist = checkDataBase();
		if (dbExist) {
			// do nothing - database already exist
		} else {
			// By calling this method and empty database will be created
			// into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			this.getWritableDatabase();

			try {
				copyDataBase();
			} catch (FileNotFoundException e) {
				throw new Error("File Not Found");
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		db.disableWriteAheadLogging();
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
            File file = new File(myPath);
            if (file.exists() && !file.isDirectory()){

				checkDB = SQLiteDatabase.openDatabase(myPath, null,
						SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);
            }
        } catch (Exception e) {
			// database does't exist yet.
            e.printStackTrace();
		}
        
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null;
	}

	/**
	 * Copies your database from your local assets-folder to the just
	 * created empty database in the system folder, from where it can be
	 * accessed and handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {
		// Open your local db as the input stream
		String ipDB = DB_NAME;
		InputStream myInput = myContext.getAssets().open(ipDB);
		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;
		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);
		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[2048];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public SQLiteDatabase openDataBase() throws SQLException {
		// Open the database
		String myPath = DB_PATH + DB_NAME;
		myDataBase = getWritableDatabase();
		/*myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);*/
		return myDataBase;
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
		myContext.deleteDatabase(DB_NAME);
		/*SharedPreferences sharedPreferences = myContext.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_MYID, 0);
		if(sharedPreferences != null){
			sharedPreferences.edit().clear().commit();
		}*/
		try{
			createDataBase();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * update particular record in the database.
	 * @param table
	 * @param whereClause
	 * @param whereArgs
	 */
	public void onDelete(String table, String whereClause, String[] whereArgs) {
		try {
			myDataBase.delete(table, whereClause, whereArgs);
		} catch (Exception e) {
		}
	}

	public void checkAndModify(){
		boolean dbExist = checkDataBase();
		if (dbExist) {
			getWritableDatabase();
		} else {
			this.getWritableDatabase();
			try {
				copyDataBase();
			} catch (FileNotFoundException e) {
				throw new Error("File Not Found");
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}
	// Add your public helper methods to access and get content from the
	// database.
	// You could return cursors by doing "return myDataBase.query(....)" so
	// it'd
	// be easy
	// to you to create adapters for your views.
}
