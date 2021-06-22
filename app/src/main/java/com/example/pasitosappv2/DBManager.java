package com.example.pasitosappv2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {

    private static final String NAME_DB = "pasosdb", NAME_GPS_TABLE = "pasos";
    private static final Integer VERSION = 1;
    public DBManager(Context context) {
        super(context, NAME_DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(id INTEGER PRIMARY KEY AUTOINCREMENT, " + "fecha DATE, bateria INTEGER, latitud DECIMAL(10,7), longitud DECIMAL(10,7))", NAME_GPS_TABLE));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(id INTEGER PRIMARY KEY AUTOINCREMENT, " + "fecha DATE, bateria INTEGER, latitud DECIMAL(10,7), longitud DECIMAL(10,7))", NAME_GPS_TABLE));
        onCreate(db);
    }
}
