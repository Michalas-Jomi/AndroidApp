package me.jomi.androidapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static me.jomi.androidapp.FeedReaderContract.FeedEntry.SQL_CREATE_ENTRIES;
import static me.jomi.androidapp.FeedReaderContract.FeedEntry.SQL_DELETE_ENTRIES;

public class DatabaseManager extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "testy.db";

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void updateCoins(int value){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("punkty", getCoin() + value);
        db.update("Gracz", contentValues, "ID=" + 0, null);
    }

    public int getCoin(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * FROM Gracz WHERE ID = ?", new String[] {"0"});
        int punkty = 0;
        if(cursor != null && cursor.moveToFirst()){
            punkty = cursor.getInt(cursor.getColumnIndex("punkty"));
            cursor.close();
        }
        return punkty;
    }
}
//https://stackoverflow.com/questions/5987863/android-sqlite-update-statement