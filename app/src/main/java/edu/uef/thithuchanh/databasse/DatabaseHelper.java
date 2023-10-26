package edu.uef.thithuchanh.databasse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "food_db";
    private static final String TABLE_SELECTED_FOODS = "selected_foods";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SELECTED_FOODS_TABLE = "CREATE TABLE " + TABLE_SELECTED_FOODS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT)";
        db.execSQL(CREATE_SELECTED_FOODS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SELECTED_FOODS);
        onCreate(db);
    }

    public void addSelectedFood(String foodName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, foodName);
        db.insert(TABLE_SELECTED_FOODS, null, values);
        db.close();
    }

    public List<String> getAllSelectedFoods() {
        List<String> selectedFoodsList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SELECTED_FOODS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int columnIndex = cursor.getColumnIndex(COLUMN_NAME); // Get the column index

        while (cursor.moveToNext()) {
            String foodName = cursor.getString(columnIndex);
            selectedFoodsList.add(foodName);
        }

        cursor.close();
        db.close();
        return selectedFoodsList;
    }

    public void clearSelectedFoods() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SELECTED_FOODS, null, null);
        db.close();
    }
}