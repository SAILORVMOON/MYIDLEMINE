package com.example.myidlemine;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBDatame {

    private static final String DATABASE_NAME = "myData.db";
    private static final int DATABASE_VERSION = 5;
    private static final String TABLE_NAME = "myDataTable";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_LEVEL = "level";
    private static final String COLUMN_MONEY = "money";
    private static final String COLUMN_PROGRESS = "progress";
    private static final String COLUMN_MULTIPLIER = "multiplier";
    private static final String COLUMN_FACTORIES = "factories";


    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_LEVEL = 1;
    private static final int NUM_COLUMN_MONEY = 2;
    private static final int NUM_COLUMN_PROGRESS = 3;
    private static final int NUM_COLUMN_MULTIPLIER= 4;
    private static final int NUM_COLUMN_FACTORIES  = 5;

    private SQLiteDatabase mDataBase;

    public DBDatame(Context context) {
        OpenHelper mOpenHelper = new OpenHelper(context);
        mDataBase = mOpenHelper.getWritableDatabase();
    }

    public long insert(String level, String money, String progress, String multiplier, String factories) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_LEVEL, level);
        cv.put(COLUMN_MONEY, money);
        cv.put(COLUMN_PROGRESS, progress);
        cv.put(COLUMN_MULTIPLIER, multiplier);
        cv.put(COLUMN_FACTORIES, factories);
        return mDataBase.insert(TABLE_NAME, null, cv);
    }

    public int update(Datame md) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_LEVEL, md.getLevel());
        cv.put(COLUMN_MONEY, md.getMoney());
        cv.put(COLUMN_PROGRESS, md.getProgress());
        cv.put(COLUMN_MULTIPLIER, md.getMultiplier());
        cv.put(COLUMN_FACTORIES, md.getFactories());
        return mDataBase.update(TABLE_NAME, cv, COLUMN_ID + " = ?",new String[] { String.valueOf(md.getId())});
    }

    public Datame select(long id) {
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        mCursor.moveToFirst();
        long idd = mCursor.getInt(NUM_COLUMN_ID);
        String level = mCursor.getString(NUM_COLUMN_LEVEL);
        String money = mCursor.getString(NUM_COLUMN_MONEY);
        String progress = mCursor.getString(NUM_COLUMN_PROGRESS);
        String multiplier = mCursor.getString(NUM_COLUMN_MULTIPLIER);
        String factories = mCursor.getString(NUM_COLUMN_FACTORIES);
        return new Datame(idd, level, money, progress, multiplier, factories);
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
                    COLUMN_MONEY + " TEXT, " +
                    COLUMN_PROGRESS + " TEXT,"+
                    COLUMN_MULTIPLIER + " TEXT,"+
                    COLUMN_FACTORIES+" TEXT);";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

}