package csci498.gnanda.lunchList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RestaurantHelper extends SQLiteOpenHelper {
	
	private static final String PHONE2 = "phone";
	private static final String FEED2 = "feed";
	private static final String NOTES2 = "notes";
	private static final String TYPE2 = "type";
	private static final String ADDRESS2 = "address";
	private static final String NAME2 = "name";
	private static final String ADD_COLUMN_LON = "ALTER TABLE restaurants ADD COLUMN lon REAL";
	private static final String ADD_COLUMN_LAT = "ALTER TABLE restaurants ADD COLUMN lat REAL";
	private static final String ADD_COLUMN_FEED = "ALTER TABLE restaurants ADD COLUMN feed TEXT";
	private static final String ADD_COLUMN_PHONE = "ALTER TABLE restaurants ADD COLUMN phone TEXT";
	private static final String DATABASE_NAME = "lunchlist.db";
	private static final String CREATE_TABLE_SQL = "CREATE TABLE restaurants (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, address TEXT, type TEXT, notes TEXT, feed TEXT, lat REAL, lon REAL, phone TEXT);";
	private static final String SELECT_BY_ID_SQL = "SELECT _id, name, address, type, notes, feed, lat, lon, phone FROM restaurants WHERE _ID=?";
	private static final String SELECT_ALL_SQL = "SELECT _id, name, address, type, notes, feed, lat, phone lon FROM restaurants ORDER BY ";
	public static final int SCHEMA_VERSION = 4;
	
	public RestaurantHelper(Context context) {
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion < 2) {
			db.execSQL(ADD_COLUMN_FEED);
		}
		
		if (oldVersion < 3) {
			db.execSQL(ADD_COLUMN_LAT);
			db.execSQL(ADD_COLUMN_LON);
		}		
		
		if (oldVersion < 4) {
			db.execSQL(ADD_COLUMN_PHONE);
		}
	}
	
	public Cursor getById(String id) {
		String[] args = {id};
		return getReadableDatabase().rawQuery(SELECT_BY_ID_SQL, args);
	}
	
	public void update(String id, String name, String address, String type, String notes, String feed, String phone) {
		ContentValues cv = new ContentValues();
		String[] args = {id};
		cv.put(NAME2, name);
		cv.put(ADDRESS2, address);
		cv.put(TYPE2, type);
		cv.put(NOTES2, notes);
		cv.put(FEED2, feed);
		cv.put(PHONE2, phone);
		
		getWritableDatabase().update("restaurants", cv, "_ID=?", args);
	}
	
	public void insert(String name, String address, String type, String notes, String feed, String phone) {
		ContentValues cv = new ContentValues();
		cv.put(NAME2, name);
		cv.put(ADDRESS2, address);
		cv.put(TYPE2, type);
		cv.put(NOTES2, notes);
		cv.put(FEED2, feed);
		cv.put(PHONE2, phone);
		
		getWritableDatabase().insert("Restaurants", NAME2, cv);
	}
	
	public void updateLocation(String id, double lat, double lon) {
		ContentValues cv = new ContentValues();
		String[] args = {id};
		
		cv.put("lat", lat);
		cv.put("lon", lon);
		
		getWritableDatabase().update("restaurants", cv, "_ID=?", args);
	}

	public Cursor getAll(String orderBy) {
		return getReadableDatabase().rawQuery(SELECT_ALL_SQL + orderBy, null);
	}
	
	public String getName(Cursor c) {
		return c.getString(1);
	}
	
	public String getAddress(Cursor c) {
		return c.getString(2);
	}
	
	public String getType(Cursor c) {
		return c.getString(3);
	}
	
	public String getNotes(Cursor c) {
		return c.getString(4);
	}

	public String getFeed(Cursor c) {
		return c.getString(5);
	}
	
	public double getLatitude(Cursor c) {
		return c.getDouble(6);
	}
	
	public double getLongitude(Cursor c) {
		return c.getDouble(7);
	}
	
	public String getPhone(Cursor c) {
		return c.getString(8);
	}
	
}
