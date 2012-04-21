package com.lion.engine.example.storage;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

import com.lion.engine.util.Util;

public class ScoreProvider extends ContentProvider implements BaseColumns {
	
	public static final Uri CONTENT_URI = 
		Uri.parse("content://com.lion.engine.example.storage.scoreprovider");
	
	/**
     * 玩家名字
     * <P>Type: TEXT</P>
     */
	public static final String NAME = "name";
	
	/**
	 * 分数
	 * <P>Type: INTEGER</P>
	 */
	public static final String SCORE = "score";
	
	/**
	 * 游戏的时间
	 * <P>Type: INTEGER(long from System.curentTimeMillis())</P>
	 */
	public static final String TIME = "time";
	
	 /**
     * The default sort order for this table
     */
    public static final String DEFAULT_SORT_ORDER = "SCORE DESC";
	
	private static final String DATABASE_NAME = "score.db";
	private static final String SCORE_TABLE_NAME = "score";
    private static final int DATABASE_VERSION = 1;
	
    // 创建数据库和数据表
	private static class ScoreDatabaseHelper extends SQLiteOpenHelper {

		public ScoreDatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			Util.DEBUG("create a db");
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			
			Util.DEBUG("create a db table!");
			
			db.execSQL("CREATE TABLE " + SCORE_TABLE_NAME + " ("
	                + ScoreProvider._ID + " INTEGER PRIMARY KEY,"
	                + ScoreProvider.NAME + " TEXT,"
	                + ScoreProvider.SCORE + " INTEGER,"
	                + ScoreProvider.TIME + " INTEGER"
	                + ");");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Util.DEBUG("Upgrading database from version " + oldVersion + " to "
	                + newVersion + ", which will destroy all old data");
	        db.execSQL("DROP TABLE IF EXISTS notes");
	        onCreate(db);
		}

	}
	
	private ScoreDatabaseHelper sdbHelper;
	
	@Override
	public boolean onCreate() {
		sdbHelper = new ScoreDatabaseHelper(getContext());
		
		Util.DEBUG("create a provider!");
		
		return true;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		Util.DEBUG("query!");
		
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(SCORE_TABLE_NAME);
        
        // If no sort order is specified use the default
        String orderBy;
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = DEFAULT_SORT_ORDER;
        } else {
            orderBy = sortOrder;
        }
        
        // Get the database and run the query
        SQLiteDatabase db = sdbHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);

        
//        Cursor c = db.query(SCORE_TABLE_NAME, projection, selection, selectionArgs, null, null, orderBy);
        // Tell the cursor what uri to watch, so it knows when its source data changes
        c.setNotificationUri(getContext().getContentResolver(), uri);
        
        
        return c;
		
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		
		Util.DEBUG("insert a score!");
		
		ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }
        
        Long now = Long.valueOf(System.currentTimeMillis());
        
        // 确保所有的域都有值
        if (values.containsKey(TIME) == false) {
            values.put(TIME, now);
        }
        if (values.containsKey(NAME) == false) {
        	values.put(NAME, "无名氏");
        }
        if (values.containsKey(SCORE) == false) {
        	values.put(SCORE, 0);
        }
        
        SQLiteDatabase db = sdbHelper.getWritableDatabase();
        long rowId = db.insert(SCORE_TABLE_NAME, "no value", values);
        if (rowId > 0) {
            Uri scoreUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(scoreUri, null);
            
            
            return scoreUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
        
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
