package com.example.m_hike;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "m-hike.db";
    private static final int DATABASE_VERSION = 1;

    // Define the table Hiking
    private static final String TABLE_NAME_Hiking = "hiking";
    private static final String COLUMN_ID_Hiking = "id";
    private static final String COLUMN_NAME_Hiking = "name";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_PARKING_AVAILABLE = "parkingAvailable";
    private static final String COLUMN_LENGTH_OF_HIKE = "lengthOfHike";
    private static final String COLUMN_DIFFICULT_LEVEL = "difficultLevel";
    private static final String COLUMN_DESCRIPTION = "description";

    // Define the table Observation
    private static final String TABLE_NAME_Observation = "observation";
    private static final String COLUMN_ID_Observation = "id";
    private static final String COLUMN_NAME_Observation = "name";
    private static final String COLUMN_Time = "time";
    private static final String COLUMN_Comment = "comment";
    private static final String COLUMN_HikingId = "hikingId";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the table with the specified columns
        String createTableHikingSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_Hiking + " (" +
                COLUMN_ID_Hiking + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME_Hiking + " TEXT, " +
                COLUMN_LOCATION + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_PARKING_AVAILABLE + " TEXT, " +
                COLUMN_LENGTH_OF_HIKE + " TEXT, " +
                COLUMN_DIFFICULT_LEVEL + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT );";

        String createTableObservationSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_Observation + " (" +
                COLUMN_ID_Observation + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME_Observation + " TEXT, " +
                COLUMN_Time + " TEXT, " +
                COLUMN_Comment + " TEXT, " +
                COLUMN_HikingId + " INTEGER);";

        db.execSQL(createTableHikingSQL);
        db.execSQL(createTableObservationSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database schema upgrades here if needed
    }

    // Insert Hiking a new record into the table
    public long insertHikingRecord(String name, String location, String date, String parkingAvailable, String lengthOfHike, String difficultLevel, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_Hiking, name);
        values.put(COLUMN_LOCATION, location);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_PARKING_AVAILABLE, parkingAvailable);
        values.put(COLUMN_LENGTH_OF_HIKE, lengthOfHike);
        values.put(COLUMN_DIFFICULT_LEVEL, difficultLevel);
        values.put(COLUMN_DESCRIPTION, description);
        long newRowId = db.insert(TABLE_NAME_Hiking, null, values);
        db.close();
        return newRowId;
    }

    // Query Hiking all records from the table
    public Cursor getAllHikingRecords(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME_Hiking, null, null, null, null, null, null);
    }

    // Update Hiking a record in the table
    public int updateHikingRecord(long id, String name, String location, String date, String parkingAvailable, String lengthOfHike, String difficultLevel, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_Hiking, name);
        values.put(COLUMN_LOCATION, location);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_PARKING_AVAILABLE, parkingAvailable);
        values.put(COLUMN_LENGTH_OF_HIKE, lengthOfHike);
        values.put(COLUMN_DIFFICULT_LEVEL, difficultLevel);
        values.put(COLUMN_DESCRIPTION, description);
        int rowsUpdated = db.update(TABLE_NAME_Hiking, values, COLUMN_ID_Hiking + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsUpdated;
    }

    @SuppressLint("Range")
    public HikeData getHikingRecordById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_ID_Hiking + "=?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(TABLE_NAME_Hiking, null, selection, selectionArgs, null, null, null);

        HikeData hikingData = null;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int Id = cursor.getColumnIndex(COLUMN_ID_Hiking);
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_Hiking));
                String location = cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION));
                String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                String parkingAvailable = cursor.getString(cursor.getColumnIndex(COLUMN_PARKING_AVAILABLE));
                String lengthOfHike = cursor.getString(cursor.getColumnIndex(COLUMN_LENGTH_OF_HIKE));
                String difficultLevel = cursor.getString(cursor.getColumnIndex(COLUMN_DIFFICULT_LEVEL));
                String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));

                // Tạo một đối tượng HikeData cho bản ghi này
                hikingData = new HikeData(
                        cursor.getInt(Id),
                        name,
                        location,
                        date,
                        parkingAvailable,
                        lengthOfHike,
                        difficultLevel,
                        description
                );
            }
            cursor.close();
        }
        db.close();
        return hikingData;
    }



    // Delete Hiking a record from the table
    public int deleteHikingRecord(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_NAME_Hiking, COLUMN_ID_Hiking + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted;
    }

    // Insert Observation a new record into the table
    public long insertObservationRecord(String name, String time, String comment, int hikingId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_Observation, name);
        values.put(COLUMN_Time, time);
        values.put(COLUMN_Comment, comment);
        values.put(COLUMN_HikingId, hikingId);
        long newRowId = db.insert(TABLE_NAME_Observation, null, values);
        db.close();
        return newRowId;
    }

    // Query Observation all records from the table
    public Cursor getAllObservationRecords(int hikingId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_HikingId + "=?";
        String[] selectionArgs = {String.valueOf(hikingId)};
        return db.query(TABLE_NAME_Observation, null, selection, selectionArgs, null, null, null);
    }

    // Update Observation a record in the table
    public int updateObservationRecord(int id, String name, String time, String comment, int hikingId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_Observation, name);
        values.put(COLUMN_Time, time);
        values.put(COLUMN_Comment, comment);
        values.put(COLUMN_HikingId, hikingId);
        int rowsUpdated = db.update(TABLE_NAME_Observation, values, COLUMN_ID_Observation + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsUpdated;
    }



    // Delete Observation a record from the table
    public int deleteObservationRecord(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_NAME_Observation, COLUMN_ID_Observation + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted;
    }

    // Get an ObservationData record by its ID
    @SuppressLint("Range")
    public ObservationData getObservationRecordById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                COLUMN_ID_Observation,
                COLUMN_NAME_Observation,
                COLUMN_Time,
                COLUMN_Comment,
                COLUMN_HikingId
        };
        String selection = COLUMN_ID_Observation + "=?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(TABLE_NAME_Observation, projection, selection, selectionArgs, null, null, null);

        ObservationData observationData = null;

        if (cursor != null && cursor.moveToFirst()) {
            int obsId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_Observation));
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_Observation));
            String time = cursor.getString(cursor.getColumnIndex(COLUMN_Time));
            String comment = cursor.getString(cursor.getColumnIndex(COLUMN_Comment));
            int hikingId = cursor.getInt(cursor.getColumnIndex(COLUMN_HikingId));

            observationData = new ObservationData(obsId, name, time, comment, hikingId);
            cursor.close();
        }

        db.close();
        return observationData;
    }
}

