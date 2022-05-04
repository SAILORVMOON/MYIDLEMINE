package com.example.myidlemine;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBShopWorker {
    private static final String DATABASE_NAME = "shopWorker.db";
    private static final int DATABASE_VERSION = 6;
    private static final String TABLE_NAME = "shopWorkerTable";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_LEVEL = "level";
    private static final String COLUMN_PRICE = "price";


    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_LEVEL = 1;
    private static final int NUM_COLUMN_PRICE = 2;

    private SQLiteDatabase mDataBase;

    public DBShopWorker(Context context) {
        DBShopWorker.OpenHelper mOpenHelper = new DBShopWorker.OpenHelper(context);
        mDataBase = mOpenHelper.getWritableDatabase();
    }

    public long insert(String level, String price) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_LEVEL, level);
        cv.put(COLUMN_PRICE, price);
        return mDataBase.insert(TABLE_NAME, null, cv);
    }

    public int update(ShopWorker md) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_LEVEL, md.getLevel());
        cv.put(COLUMN_PRICE, md.getPrice());
        return mDataBase.update(TABLE_NAME, cv, COLUMN_ID + " = ?",new String[] { String.valueOf(md.getId())});
    }

    public ShopWorker select(long id) {
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        mCursor.moveToFirst();
        long idd = mCursor.getInt(NUM_COLUMN_ID);
        String level = mCursor.getString(NUM_COLUMN_LEVEL);
        String money = mCursor.getString(NUM_COLUMN_PRICE);
        return new ShopWorker(idd, level, money);
    }

    private class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LEVEL+ " TEXT, " +
                    COLUMN_PRICE + " TEXT);";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

}

