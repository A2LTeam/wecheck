/**
 * 
 */
package com.forfun.wecheck.database;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.forfun.wecheck.cst.SystemCst.Language;

/**
 * Helper class for database
 * 
 * @author Nick
 * 
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "wecheck.db";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_SCRIPT_CREATE_TABLE = "createTable.sql";
	private static final String DATABASE_SCRIPT_CREATE_VIEW_EN = "createViewEn.sql";
	private static final String DATABASE_SCRIPT_CREATE_VIEW_TC = "createViewTc.sql";
	private static final String DATABASE_SCRIPT_CREATE_VIEW_SC = "createViewSc.sql";
	private static final String DATABASE_SCRIPT_INSERT_CATEGORY = "insert-category.sql";
	private static final String DATABASE_SCRIPT_INSERT_SUB_CATEGORY = "insert-sub-category.sql";
	private static final String DATABASE_SCRIPT_INSERT_SHOP = "insert-shop.sql";
	private static final String DATABASE_SCRIPT_INSERT_ITEM = "insert-item.sql";

	// Variables
	private Context _mContext;

	public DatabaseOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		_mContext = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase arg0) {

		executeScript(arg0, DATABASE_SCRIPT_CREATE_TABLE, DATABASE_VERSION);
		executeScript(arg0, DATABASE_SCRIPT_CREATE_VIEW_TC, DATABASE_VERSION);
		executeScript(arg0, DATABASE_SCRIPT_INSERT_CATEGORY, DATABASE_VERSION);
		executeScript(arg0, DATABASE_SCRIPT_INSERT_SUB_CATEGORY, DATABASE_VERSION);
		executeScript(arg0, DATABASE_SCRIPT_INSERT_SHOP, DATABASE_VERSION);
		executeScript(arg0, DATABASE_SCRIPT_INSERT_ITEM, DATABASE_VERSION);
	}

	private void executeScript(SQLiteDatabase arg0, String scriptName, int dbVersion) {
		Log.i(this.getClass().getSimpleName(), "Execute script : " + scriptName);
		String script = readScriptFromAssets(scriptName);
		String[] querys = script.split(";");

		arg0.beginTransaction();

		try {
			for (int i = 0; i < querys.length; i++) {
//				Log.d(this.getClass().getSimpleName(), "Running SQL : " + querys[i]);
				arg0.execSQL(querys[i]);
			}

			arg0.setTransactionSuccessful();
		} finally {
			arg0.endTransaction();
		}

	}

	// --------------------------------------------------------------------------------------------------
	// Custom functions
	// --------------------------------------------------------------------------------------------------
	private String readScriptFromAssets(String filename) {
		AssetManager assetManager = _mContext.getAssets();
		InputStream inputStream = null;
		BufferedReader bufferedReader = null;
		StringBuilder stringBuilder = new StringBuilder();

		try {
			// Read the data from the file
			inputStream = assetManager.open(filename);
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));

			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			stringBuilder.setLength(0); // Error in reading data, clearing the
										// builder
		} finally {
			// Close the stream
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return stringBuilder.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
	 * .SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// Drop older table if existed
		// arg0.execSQL("DROP TABLE IF EXISTS " + ?);

		// Create tables again
		// onCreate(arg0);
	}

	public void switchLanguage(Language language) {

		DatabaseOpenHelper dbHelper = new DatabaseOpenHelper(_mContext);

		if (Language.ENGLISH == language) {
			executeScript(dbHelper.getWritableDatabase(), DATABASE_SCRIPT_CREATE_VIEW_EN, DATABASE_VERSION);
		} else if (Language.SIMPLIFIED_CHINESE == language) {
			executeScript(dbHelper.getWritableDatabase(), DATABASE_SCRIPT_CREATE_VIEW_SC, DATABASE_VERSION);
		} else {
			executeScript(dbHelper.getWritableDatabase(), DATABASE_SCRIPT_CREATE_VIEW_TC, DATABASE_VERSION);
		}
	}
}
