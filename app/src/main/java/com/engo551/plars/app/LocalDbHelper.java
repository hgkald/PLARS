package com.engo551.plars.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
/**
 * Created by Xueyang Zou on 2016-03-21.
 */
public class LocalDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Pref.db";
    public static final String TABLE_NAME = "ClickRecord";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "ResType";

    public LocalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ClickRecord (ID INTEGER PRIMARY KEY AUTOINCREMENT, ResType TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS ClickRecord");
        onCreate(db);
    }

    public boolean insertRecord (String type){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        /*first argument is which column you want to insert data, second is the value itself*/
        contentValues.put(COL_2, type);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }
}
