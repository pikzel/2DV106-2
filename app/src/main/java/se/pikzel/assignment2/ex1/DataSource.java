package se.pikzel.assignment2.ex1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DataSource {

    // Database fields
    private SQLiteDatabase database;
    private DbHelper dbHelper;
    private String[] allColumns = {DbHelper.COLUMN_ID,
            DbHelper.COLUMN_YEAR,
            DbHelper.COLUMN_COUNTRY};

    public DataSource(Context context) {
        dbHelper = new DbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Visit createVisit(int year, String country) {
        ContentValues values = new ContentValues();
        values.put(DbHelper.COLUMN_YEAR, year);
        values.put(DbHelper.COLUMN_COUNTRY, country);
        long insertId = database.insert(DbHelper.VISITS_TABLE_NAME, null, values);
        Cursor cursor = database.query(DbHelper.VISITS_TABLE_NAME,
                allColumns, DbHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Visit newVisit = cursorToVisit(cursor);
        cursor.close();
        return newVisit;
    }

    public void deleteVisit(Visit visit) {
        long id = visit.getId();
        database.delete(DbHelper.VISITS_TABLE_NAME, DbHelper.COLUMN_ID
                + " = " + id, null);
    }

    public Visit getVisit(long visitId) {
        String restrict = DbHelper.COLUMN_ID + "=" + visitId;
        Cursor cursor = database.query(true, DbHelper.VISITS_TABLE_NAME, allColumns, restrict,
                null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursorToVisit(cursor);
        }
        // Make sure to close the cursor
        cursor.close();
        return null;
    }

    public boolean updateVisit(long visitId, int year, String country) {
        ContentValues args = new ContentValues();
        args.put(DbHelper.COLUMN_YEAR, year);
        args.put(DbHelper.COLUMN_COUNTRY, country);

        String restrict = DbHelper.COLUMN_ID + "=" + visitId;
        return database.update(DbHelper.VISITS_TABLE_NAME, args, restrict, null) > 0;
    }

    public List<Visit> getAllVisits() {
        List<Visit> visits = new ArrayList<Visit>();

        Cursor cursor = database.query(DbHelper.VISITS_TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Visit visit = cursorToVisit(cursor);
            visits.add(visit);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return visits;
    }

    private Visit cursorToVisit(Cursor cursor) {
        Visit visit = new Visit();
        visit.setId(cursor.getLong(0));
        visit.setYear(cursor.getInt(1));
        visit.setCountry(cursor.getString(2));
        return visit;
    }
}
