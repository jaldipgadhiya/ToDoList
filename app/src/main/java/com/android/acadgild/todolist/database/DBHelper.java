package com.android.acadgild.todolist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.android.acadgild.todolist.model.ToDoData;
import com.android.acadgild.todolist.utils.Constants;

import java.util.ArrayList;

/**
 * Created by Jal on 28-07-2017.
 * This class is for all database related DML operations.
 */

public class DBHelper {
    private SQLiteDatabase db;
    private final Context context;
    private final TablesClass dbHelper;
    public static int no;
    public static DBHelper db_helper = null;

    public static DBHelper getInstance(Context context){
        try{
            if(db_helper == null){
                db_helper = new DBHelper(context);
                db_helper.open();
            }
        }catch(IllegalStateException e){
            //db_helper already open
        }
        return db_helper;
    }

    /*
	 * set context of the class and initialize TableClass Object
	 */

    public DBHelper(Context c) {
        context = c;
        dbHelper = new TablesClass(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    /*
     * close databse.
     */
    public void close() {
        if (db.isOpen()) {
            db.close();
        }
    }

    public boolean dbOpenCheck() {
        try{
            return db.isOpen();
        }catch(Exception e){
            return false;
        }
    }

    /*
     * open com.android.acadgild.todolist.database
     */
    public void open() throws SQLiteException {
        try {
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            Log.v("open Exception", "error==" + e.getMessage());
            db = dbHelper.getReadableDatabase();
        }
    }

    public long insertContentVals(String tableName, ContentValues content){
        long id=0;
        try{
            db.beginTransaction();
            id = db.insert(tableName, null, content);
            db.setTransactionSuccessful();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
        return id;
    }

    public Cursor getTableRecords(String tablename, String[] columns, String where, String orderby){
        Cursor cursor =  db.query(false, tablename, columns,where, null, null, null, orderby, null);
        return cursor;
    }

    /*
	 * Get count of all tables in a table as per the condition
	 */

    public int getFullCount(String table, String where) {
        Cursor cursor = db.query(false, table, null, where, null, null, null, null, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
                no = cursor.getCount();
                cursor.close();
            }
        } finally {
            cursor.close();
        }
        return no;
    }


    public void deleteRecords(String table, String whereClause, String[] whereArgs){
        try {
            db.beginTransaction();
            db.delete(table, whereClause, whereArgs);
            db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
    }

    /*
	 * Get value of any table as per the condition.
	 */

    public String getValue(String table, String column, String where) {
        Cursor result = db.query(false, table, new String[] { column }, where,
                null, null, null, Constants.ID, null);
        String value = "";
        try {
            if (result.moveToFirst()) {
                value = result.getString(0);
            } else {
                return null;
            }
        } finally {
            result.close();
        }
        return value;
    }
	/*
	 * Get Multiple Values from column of any specified table.
	 */

    public String[] getValues(boolean b, String table, String column,
                              String where, String orderBy) {
        ArrayList<String> savedAns = new ArrayList<String>();
        Cursor result = null;
        String[] y;
        try {
            result = db.query(b, table, new String[] { column }, where, null,
                    null, null, orderBy, null);
            if (result.moveToFirst()) {
                do {
                    savedAns.add(result.getString(result.getColumnIndex(column)));
                } while (result.moveToNext());
            } else {
                return null;
            }
            y = savedAns.toArray(new String[result.getCount()]);
        } finally {
            result.close();
        }
        return y;
    }

    public int updateRecords(String table, ContentValues values,
                             String whereClause, String[] whereArgs) {
        int a=0;
        try {
            db.beginTransaction();
             a = db.update(table, values, whereClause, whereArgs);
            db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
        return a;
    }

    //To get all Tododata Details
    public ArrayList<ToDoData> getAllToDos() {
        ArrayList<ToDoData> todos = new ArrayList<ToDoData>();

        // select Tododata query
        String query = "SELECT  * FROM " + Constants.TO_DO_RECORD +" order by key_date";

        // get reference of the Tododata DB
        Cursor cursor = db.rawQuery(query, null);


        // parse all results
        ToDoData todo = null;

        if (cursor.moveToFirst()) {
            do {
                todo = new ToDoData();
                todo.setKeyId(Integer.parseInt(cursor.getString(0).toString()));
                todo.setKeyTitle(cursor.getString(1).toString());
                todo.setKeyDescription(cursor.getString(2).toString());
                todo.setKeyDate(cursor.getString(3).toString());
                todo.setKeyStatus(Integer.parseInt(cursor.getString(5).toString()));
                todo.setKeyPhotoImage(cursor.getBlob(4));
                todos.add(todo);
            } while (cursor.moveToNext());
        }
        return todos;
    }

    //To get all completed Tododata Details
    public ArrayList<ToDoData> getAllCompletedToDos() {
        ArrayList<ToDoData> todos = new ArrayList<ToDoData>();

        // select Tododata query
        String query = "SELECT  * FROM " + Constants.TO_DO_RECORD + " where key_status = 1 order by key_date";

        // get reference of the Tododata DB
        Cursor cursor = db.rawQuery(query, null);


        // parse all results
        ToDoData todo = null;

        if (cursor.moveToFirst()) {
            do {
                todo = new ToDoData();
                todo.setKeyId(Integer.parseInt(cursor.getString(0).toString()));
                todo.setKeyTitle(cursor.getString(1).toString());
                todo.setKeyDescription(cursor.getString(2).toString());
                todo.setKeyDate(cursor.getString(3).toString());
                todo.setKeyStatus(Integer.parseInt(cursor.getString(5).toString()));
                todo.setKeyPhotoImage(cursor.getBlob(4));
                todos.add(todo);
            } while (cursor.moveToNext());
        }
        return todos;
    }



}
