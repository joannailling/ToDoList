package com.example.joanna.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by joanna on 8/26/15.
 */
public class ToDoItemDatabase extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "TodoItemDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String TABLE_TODOITEMS = "ToDoItems";

    // ToDoItem Table Columns
    private static final String KEY_TODOITEMS_ID = "id";
    private static final String KEY_TODOITEMS_ITEM = "item";
    private static final String KEY_TODOITEMS_DUEDATE = "dueDate";
    private static final String KEY_TODOITEMS_PRIORITY = "priority";

    private static ToDoItemDatabase sInstance;
    private static final String TAG = "ToDoItemDB";

    public ToDoItemDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // called when the database connection is being configured
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    //Database and tables are created here
    // Called when the database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL for creating the tables
        String CREATE_TODOITEMS_TABLE = "CREATE TABLE " + TABLE_TODOITEMS +
                "(" +
                    KEY_TODOITEMS_ID + " INTEGER PRIMARY KEY, " +
                    KEY_TODOITEMS_ITEM + " TEXT, " +
                    KEY_TODOITEMS_DUEDATE + " TEXT, " +
                    KEY_TODOITEMS_PRIORITY + " NUM" +
                ")";
        db.execSQL(CREATE_TODOITEMS_TABLE);
    }

    // This method is called when database is upgraded like
    // modifying the table structure, adding constraints to database, etc
    // This method will be called only if a DB already exists with the same name but
    // different database version
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop tables and recreate
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOITEMS);
            onCreate(db);
        }
    }

    // Make database a singleton instance to avoid memory leaks and unnecessary
    // re-allocations. See http://bit.ly6LRzfx
    // Ensures that only one TodoItemDatabase will ever exist at any given time
    public static synchronized ToDoItemDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ToDoItemDatabase(context.getApplicationContext());
        }
        return sInstance;
    }

    // Insert or Update ToDoItem to the database
    public long addOrUpdateToDoItem(ToDoItem tdi) {
        // create and/or open the db for writing
        SQLiteDatabase db = getWritableDatabase();
        long toDoItemID = tdi.getId();

        //wrap insert in a transaction to help w performance and ensure db consistency
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TODOITEMS_ITEM, tdi.getItem());
            values.put(KEY_TODOITEMS_DUEDATE, tdi.getDueDate());
            values.put(KEY_TODOITEMS_PRIORITY, tdi.getPriority());

            // First try to update the toDoItem in case the item key ID already
            // exists in the database.
            int rows = db.update(TABLE_TODOITEMS, values, KEY_TODOITEMS_ID + "= ?",
                    new String[]{String.valueOf(tdi.getId())});

            // check if update was successful
            if (rows == 1) {
                // get the primary key (id) of the toDoItem we just updated
                String toDoItemSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_TODOITEMS_ID, TABLE_TODOITEMS, KEY_TODOITEMS_ID);
                Cursor cursor = db.rawQuery(toDoItemSelectQuery,
                        new String[]{String.valueOf(tdi.getId())});
                try {
                    if (cursor.moveToFirst()) {
                        toDoItemID = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // toDoItem with this item text did not already exist, so insert new
                toDoItemID = db.insertOrThrow(TABLE_TODOITEMS, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
        return toDoItemID;
    }

    // Query all posts
    public ArrayList<ToDoItem> getAllToDoItems() {

        //  SELECT * FROM TODOITEMS
        String TODOITEMS_SELECT_QUERY = String.format("SELECT * FROM %s",
                TABLE_TODOITEMS);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(TODOITEMS_SELECT_QUERY, null);
        ArrayList<ToDoItem> toDoItemList = new ArrayList<ToDoItem>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    ToDoItem newToDoItem = new ToDoItem();

                    newToDoItem.setItem(cursor.getString(cursor.getColumnIndex(KEY_TODOITEMS_ITEM)));
                    newToDoItem.setDueDate(cursor.getString(cursor.getColumnIndex(KEY_TODOITEMS_DUEDATE)));
                    newToDoItem.setPriority(cursor.getInt(cursor.getColumnIndex(KEY_TODOITEMS_PRIORITY)));

                    toDoItemList.add(newToDoItem);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return toDoItemList;
    }

    public void deleteAllToDoItems(){
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_TODOITEMS, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
        }
    }

 /**
    public void deleteToDoItem(ToDoItem tdi) {
        // create and/or open the db for writing
        SQLiteDatabase db = getWritableDatabase();
        long toDoItemID = -1;

        // get the primary key (id) of the toDoItem we are trying to delete
        String toDoItemSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                KEY_TODOITEMS_ID, TABLE_TODOITEMS, KEY_TODOITEMS_ITEM);
        Cursor cursor = db.rawQuery(toDoItemSelectQuery,
                new String[]{String.valueOf(tdi.getItem())});
        try {
            if (cursor.moveToFirst()) {
                toDoItemID = cursor.getInt(0);
                db.setTransactionSuccessful();
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        String deleteWhereClause = String.format("%s = %s", KEY_TODOITEMS_ID, toDoItemID);

        //wrap insert in a transaction to help w performance and ensure db consistency
        db.beginTransaction();
        try {
            db.delete(TABLE_TODOITEMS, deleteWhereClause, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete a post from the database");
        } finally {
            db.endTransaction();
        }
    } **/

    public void deleteToDoItem(String itemString) {
        // create and/or open the db for writing
        SQLiteDatabase db = getWritableDatabase();
        long toDoItemID = -1;

        // get the primary key (id) of the toDoItem we are trying to delete
        String toDoItemSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                KEY_TODOITEMS_ID, TABLE_TODOITEMS, KEY_TODOITEMS_ITEM);
        Cursor cursor = db.rawQuery(toDoItemSelectQuery,
                new String[]{String.valueOf(itemString)});
        try {
            if (cursor.moveToFirst()) {
                toDoItemID = cursor.getInt(0);

                String deleteWhereClause = String.format("%s = %s", KEY_TODOITEMS_ID,
                        toDoItemID);

                //wrap insert in a transaction to help w performance and ensure db consistency
                db.beginTransaction();
                try {
                    db.delete(TABLE_TODOITEMS, deleteWhereClause, null);
                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    Log.d(TAG, "Error while trying to delete a post from the database");
                } finally {
                    db.endTransaction();
                }
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }
}
