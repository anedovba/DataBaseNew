package com.example.vitaly.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vitaly on 04.02.17.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "users";
    public static final int CURRENT_VERSION = 1;

    public static final String CREATE_TABLE_USERS = "CREATE TABLE "+TABLE_NAME+" (_id int auto increment, name text, password text)";

    public DBHelper(Context context, int version) {

        super(context, "MyDB", null, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
