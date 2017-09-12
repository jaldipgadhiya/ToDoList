package com.android.acadgild.todolist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.acadgild.todolist.utils.Constants;


/**
 * Created by Jal on 28-07-2017.
 * This class is to create all tables inside DB.
 */

public class TablesClass extends SQLiteOpenHelper {
    /**
     * Write all create table statements here in this class on oncreate method
     * If any changes in table structure go for onUpgrade method
     */

    Context context;

    public TablesClass(Context context, String DatabaseName, String nullColumnHack, int databaseVersion) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String table1 = "CREATE TABLE IF NOT EXISTS " + Constants.TO_DO_RECORD + " ("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Constants.KEY_TITLE + " TEXT, "
                + Constants.KEY_DESCRIPTION + " TEXT, "
                + Constants.KEY_DATE + " TEXT, "
                + Constants.KEY_IMAGE + " TEXT, "
                + Constants.KEY_STATUS + " INTEGER) ";
            db.execSQL(table1);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        context.deleteDatabase(Constants.DATABASE_NAME);
        onCreate(db);
    }
}