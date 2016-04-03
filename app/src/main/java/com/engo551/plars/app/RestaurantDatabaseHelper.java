package com.engo551.plars.app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class RestaurantDatabaseHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "restaurants.db";
    private static final int DATABASE_VERSION = 1;

    public static final String RESTAURANTS_TABLE = "restaurants";
    public static final int ID_COL          = 0;
    public static final int NAME_COL        = 1;
    public static final int RATING_COL      = 2;
    public static final int PICTURE_COL     = 3;
    public static final int ADDRESS_COL     = 4;
    public static final int CITY_COL        = 5;
    public static final int POSTCODE_COL    = 6;
    public static final int LONGITUDE_COL   = 7;
    public static final int LATITUDE_COL    = 8;
    public static final int TYPE_COL        = 9;

    public RestaurantDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * @return          A string array of comma-delimited strings representing each
     * row in the `restaurants` table.
     */
   public String[] toStringArray() {
       SQLiteDatabase db = this.getReadableDatabase();
       Cursor cursor = db.rawQuery("SELECT * FROM restaurants", new String[] { });

       int count = cursor.getCount();
       String[] markerList = new String[count];
       try {
           for (int i = 0; cursor.moveToNext(); i++) {
               String marker = "";
               for (int j = 0; j < cursor.getColumnCount(); j++) {
                   marker += cursor.getString(j) + ",";
               }
               markerList[i] = marker;
           }
       }
       finally {
           cursor.close();
       }

       return markerList;
    }
}