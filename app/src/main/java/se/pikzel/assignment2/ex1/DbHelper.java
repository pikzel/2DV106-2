package se.pikzel.assignment2.ex1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

    public static final String VISITS_TABLE_NAME = "visits";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_YEAR = "year";

    private static final String DATABASE_NAME = "visits.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table " + VISITS_TABLE_NAME
            + " (" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_YEAR + " integer, "
            + COLUMN_COUNTRY + " text not null);";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DbHelper.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + VISITS_TABLE_NAME);
        onCreate(db);
    }

}
