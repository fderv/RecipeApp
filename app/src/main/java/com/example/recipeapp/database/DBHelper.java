package com.example.recipeapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.recipeapp.database.DBContract.CategoryEntry;
import com.example.recipeapp.database.DBContract.RecipeEntry;
import com.example.recipeapp.database.DBContract.HistoryEntry;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME    = "meal.db";
    private static final int DATABASE_VERSION   = 2;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_RECIPE_TABLE = "CREATE TABLE " + RecipeEntry.TABLE_NAME + " (" +
                RecipeEntry._ID + " INTEGER PRIMARY KEY," +
                RecipeEntry.COLUMN_RECIPE_ID + " TEXT NOT NULL," +
                RecipeEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                RecipeEntry.COLUMN_THUMB + " TEXT NOT NULL, " +
                RecipeEntry.COLUMN_INSTRUCTIONS + " TEXT NOT NULL, " +
                RecipeEntry.COLUMN_AREA + " TEXT NOT NULL, " +
                RecipeEntry.COLUMN_YOUTUBE + " TEXT NOT NULL, " +
                RecipeEntry.COLUMN_INGREDIENTS + " TEXT NOT NULL, " +
                RecipeEntry.COLUMN_MEASURES + " TEXT NOT NULL, " +
                RecipeEntry.COLUMN_ING_COUNT + " INTEGER NOT NULL " +
                " );";

        final String SQL_CREATE_CATEGORY_TABLE = "CREATE TABLE " + CategoryEntry.TABLE_NAME + " (" +
                CategoryEntry._ID + " INTEGER PRIMARY KEY," +
                CategoryEntry.COLUMN_CATEGORY_ID + " TEXT NOT NULL," +
                CategoryEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                CategoryEntry.COLUMN_THUMB + " TEXT NOT NULL, " +
                CategoryEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL " +
                " );";

        final String SQL_CREATE_HISTORY_TABLE = "CREATE TABLE " + HistoryEntry.TABLE_NAME + " (" +
                HistoryEntry._ID + " INTEGER PRIMARY KEY," +
                HistoryEntry.COLUMN_SEARCH + " TEXT NOT NULL " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_CATEGORY_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_RECIPE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_HISTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CategoryEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HistoryEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}