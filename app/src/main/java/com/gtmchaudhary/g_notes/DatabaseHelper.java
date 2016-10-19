package com.gtmchaudhary.g_notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by gtmchaudhary on 9/28/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    // Logcat tag
    private static final String TAG = "MyActivity";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "GNotes";

    // Table name
    private static final String TABLE_NOTES = "notes";

    // Column names
    private static final String KEY_ID = "id";
    private static final String NOTE_TITLE = "note_title";
    private static final String NOTE_CONTENT = "note_content";

    // Create Table Query
    private static final String CREATE_TABLE_NOTES = "CREATE TABLE "
            + TABLE_NOTES + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + NOTE_TITLE
            + " TEXT," + NOTE_CONTENT + " TEXT" + ")";


    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Executing the Create Table Query
        db.execSQL(CREATE_TABLE_NOTES);
        Log.i(TAG,"?..$DB_CREATE");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop table, if already exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        db.execSQL(CREATE_TABLE_NOTES);

        Log.i(TAG,"?..$DB_UPGRADE");
    }


    /*********************************************** Database Operations *****************************************/

    /*
 * Creating a Note
 */
    public long createNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOTE_TITLE, note.getNoteTitle());
        values.put(NOTE_CONTENT, note.getNoteContent());

        // insert row
        long returnTemp = db.insert(TABLE_NOTES, null, values);

        Log.i(TAG,"?..$NOTE_CREATE");
        return returnTemp;
    }

    /*
 * Getting all Notes
 */
    public ArrayList<Note> getAllNotes() {
        ArrayList<Note> notes = new ArrayList<Note>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_NOTES;

        Log.e(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()){
            do{
                Note note = new Note();
                note.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                note.setNoteTitle_db((c.getString(c.getColumnIndex(NOTE_TITLE))));
                note.setNoteContent_db(c.getString(c.getColumnIndex(NOTE_CONTENT)));

                notes.add(note);
            }while(c.moveToNext());
        }

        Log.i(TAG,"?..$GET_ALL_NOTES");
        return notes;
    }

    /*
 * Updating a Note
 */
    public int updateNote(Note oldNote, String updatedTitle, String updatedContent) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOTE_TITLE, updatedTitle);
        values.put(NOTE_CONTENT, updatedContent);

        // updating row
        Log.i(TAG,"?..$NOTE_UPDATE");
        return db.update(TABLE_NOTES,
                values,
                NOTE_TITLE + " = ? AND " + NOTE_CONTENT + " =?" ,
                new String[] { String.valueOf(oldNote.getNoteTitle()), String.valueOf(oldNote.getNoteContent()) });
    }

    /*
 * Deleting a Note
 */
    public void deleteNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES,
                NOTE_TITLE + " = ? AND " + NOTE_CONTENT + " =?" ,
                new String[] {   String.valueOf(note.getNoteTitle()), String.valueOf(note.getNoteContent())  });
        Log.i(TAG,"?..$NOTE_DELTE");
    }

    /*
 * Getting Notes count
 */
    public int getNoteCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NOTES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        Log.i(TAG,"?..$NOTE_COUNT");
        // return count
        return count;
    }


    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
