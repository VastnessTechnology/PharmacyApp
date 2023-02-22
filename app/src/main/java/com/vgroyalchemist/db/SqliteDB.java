package com.vgroyalchemist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.vgroyalchemist.utils.Tags;


public class SqliteDB {

    public SQLiteDatabase database;
    private DataBaseHelper helper;

    public SqliteDB(Context context) {
        this.helper = new DataBaseHelper(context, Tags.DB_NAME, context.getPackageName());
    }

    public void open() throws SQLException {
        this.helper.createDataBase();
        try {
            database = this.helper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }
    }

    public void insert(String[] fields, String[] values, String table) {
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < fields.length; i++) {
            contentValues.put(fields[i], values[i]);
        }
        database.insert(table, null, contentValues);
    }

    public void close() {
        database.close();
        helper.close();
    }
}
