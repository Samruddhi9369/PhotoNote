package edu.csulb.android.photonote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotesDatabaseHelper extends SQLiteOpenHelper {
    public static final String ID_COLUMN = "_id";
    public static final String CAPTION_COLUMN = "name";
    public static final String FILE_PATH_COLUMN = "filepath";
    public static final String CREATED_AT_COLUMN = "created_at";
    public static final String DATABASE_TABLE = "NotesReader";
    public static final int DATABASE_VERSION = 1;

    public NotesDatabaseHelper(Context context) {
        super(context, DATABASE_TABLE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String DATABASE_CREATE = "CREATE TABLE " +
                DATABASE_TABLE + "("
                + ID_COLUMN + " INTEGER PRIMARY KEY,"
                + CAPTION_COLUMN + " TEXT,"
                + FILE_PATH_COLUMN + " TEXT,"
                + CREATED_AT_COLUMN + " TEXT)";
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }

    /**
     * Inserting new note into table
     * */
    public void insertImage(String caption, String filepath){
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

        ContentValues values = new ContentValues();
        values.put(CAPTION_COLUMN, caption);
        values.put(FILE_PATH_COLUMN, filepath);
        values.put(CREATED_AT_COLUMN,dateFormat.format(new Date()));
        // Inserting Row
        db.insert(DATABASE_TABLE, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Deleting image from table
     * */
    public void deleteImage(String filepath){
        SQLiteDatabase db = this.getWritableDatabase();
        // Deleting Row
        db.delete(DATABASE_TABLE, FILE_PATH_COLUMN + "='" + filepath+"'", null);
        db.close(); // Closing database connection
    }

    /**
     * Getting all notes
     * returns list of notes
     * */
    public List<PhotoNote> getAllNotes(){
        List<PhotoNote> notes = new ArrayList<PhotoNote>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + DATABASE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PhotoNote objPhotoNote = null;
                objPhotoNote = new PhotoNote(cursor.getString(1),cursor.getString(2),cursor.getString(3));
                notes.add(objPhotoNote);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        return notes;
    }
}
