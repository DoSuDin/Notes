package com.example.fna;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBNotes {

    private static final String DATABASE_NAME = "simple2.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "tableNotes";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "Title";
    private static final String COLUMN_TEXT = "Text";
    private static final String COLUMN_TAG = "Tag";

    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_TITLE = 1;
    private static final int NUM_COLUMN_TEXT = 2;
    private static final int NUM_COLUMN_TAG = 3;

    public SQLiteDatabase mDataBase;

    public DBNotes(Context context) {
        OpenHelper mOpenHelper = new OpenHelper(context);
        mDataBase = mOpenHelper.getWritableDatabase();
    }

    public void insert(String title, String text, String tag) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_TEXT, text);
        cv.put(COLUMN_TAG, tag);
        mDataBase.insert(TABLE_NAME, null, cv);
    }

    public int update(Notes md) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_TITLE, md.getTitle());
        cv.put(COLUMN_TEXT, md.getTexte());
        cv.put(COLUMN_TAG, md.getTag());
        return mDataBase.update(TABLE_NAME, cv, COLUMN_ID + " = ?",new String[] { String.valueOf(md.getId())});
    }

    public void deleteAll() {
        mDataBase.delete(TABLE_NAME, null, null);
    }

    public void delete(long id) {
        mDataBase.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public Notes select(long id) {
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        mCursor.moveToFirst();
        String Title = mCursor.getString(NUM_COLUMN_TITLE);
        String Text = mCursor.getString(NUM_COLUMN_TEXT);
        String Tag = mCursor.getString(NUM_COLUMN_TAG);
        return new Notes(id, Title, Text, Tag);
    }

    public ArrayList<Notes> selectAll( String selection,
                                       String[] selectionArgs) {
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

        ArrayList<Notes> arr = new ArrayList<Notes>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                long id = mCursor.getLong(NUM_COLUMN_ID);
                String Title = mCursor.getString(NUM_COLUMN_TITLE);
                String Text = mCursor.getString(NUM_COLUMN_TEXT);
                String Tag = mCursor.getString(NUM_COLUMN_TAG);
                arr.add(new Notes(id, Title, Text, Tag));
            } while (mCursor.moveToNext());
        }
        return arr;
    }

    private class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE+ " TEXT, " +
                    COLUMN_TEXT + " TEXT, " +
                    COLUMN_TAG + " TEXT); ";
            db.execSQL(query);
        }

//        public Cursor fetchRecordsByQuery(String query) {
//
//            return mDataBase.query(true, TABLE_NAME, new String[] { COLUMN_TITLE, COLUMN_TEXT,
//
//                            COLUMN_TAG }, COLUMN_TAG + " LIKE" + "'%" + query + "%'", null,
//
//                    null, null, null, null);
//
//        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
