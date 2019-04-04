package com.helpa.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "appname";
    private static final String TABLE_USER = "user";
    private static final String KEY_ID = "id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME = "name";

    Context mContext;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // super(context, Environment.getExternalStorageDirectory() + File.separator + DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    /*
        Method for Create Table in Data base
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_OFFLINE_DATA = " CREATE TABLE " + TABLE_USER + "(" +
                KEY_ID + " TEXT PRIMARY KEY, " +
                KEY_NAME + " TEXT, " +
                KEY_EMAIL + " TEXT " + ")";

        db.execSQL(CREATE_OFFLINE_DATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_USER);
        onCreate(db);
    }

}