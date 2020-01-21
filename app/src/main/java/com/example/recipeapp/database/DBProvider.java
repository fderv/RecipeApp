package com.example.recipeapp.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.recipeapp.database.DBContract.CategoryEntry;
import com.example.recipeapp.database.DBContract.RecipeEntry;
import com.example.recipeapp.database.DBContract.HistoryEntry;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DBProvider extends ContentProvider {

    public static final int RECIPE      = 100;
    public static final int CATEGORY    = 200;
    public static final int HISTORY     = 300;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private              DBHelper   mHelper;

    public static UriMatcher buildUriMatcher(){

        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority   = DBContract.CONTENT_AUTHORITY;

        matcher.addURI(authority,  DBContract.PATH_RECIPE             , RECIPE);
        matcher.addURI(authority,  DBContract.PATH_CATEGORY              , CATEGORY);
        matcher.addURI(authority,  DBContract.PATH_HISTORY              , HISTORY);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase    db      = mHelper.getReadableDatabase();
        final int               match   = sUriMatcher.match(uri);
        Cursor                  retCursor;

        switch (match) {
            case RECIPE: {
                retCursor = db.query(RecipeEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
            } break;

            case CATEGORY: {
                retCursor = db.query(CategoryEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
            } break;

            case HISTORY: {
                retCursor = db.query(HistoryEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
            } break;

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case RECIPE:    return RecipeEntry.CONTENT_TYPE;
            case CATEGORY:  return CategoryEntry.CONTENT_TYPE;
            case HISTORY:   return HistoryEntry.CONTENT_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db    = mHelper.getWritableDatabase();
        final int            match = sUriMatcher.match(uri);
        Uri                  returnUri;

        switch (match) {
            case RECIPE: {

                long _id = db.insert(RecipeEntry.TABLE_NAME, null, contentValues);

                if(_id > 0){
                    returnUri = RecipeEntry.buildRecipeUri(_id);
                }
                else{
                    throw new android.database.SQLException("Failed to insert row into" + uri);
                }

            } break;

            case CATEGORY: {

                long _id = db.insert(CategoryEntry.TABLE_NAME, null, contentValues);

                if(_id > 0){
                    returnUri = CategoryEntry.buildCategoryUri(_id);
                }
                else{
                    throw new android.database.SQLException("Failed to insert row into" + uri);
                }

            } break;

            case HISTORY: {

                long _id = db.insert(HistoryEntry.TABLE_NAME, null, contentValues);

                if(_id > 0){
                    returnUri = HistoryEntry.buildHistoryUri(_id);
                }
                else{
                    throw new android.database.SQLException("Failed to insert row into" + uri);
                }

            } break;

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        getContext().getContentResolver().notifyChange(uri,null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        if(selection == null)
            selection = "1";

        switch(match){
            case RECIPE:
                rowsDeleted = db.delete(RecipeEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case CATEGORY:
                rowsDeleted = db.delete(CategoryEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case HISTORY:
                rowsDeleted = db.delete(HistoryEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch(match){
            case RECIPE:
                rowsUpdated = db.update(RecipeEntry.TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;
            case CATEGORY:
                rowsUpdated = db.update(CategoryEntry.TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;
            case HISTORY:
                rowsUpdated = db.update(HistoryEntry.TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rowsUpdated != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;
    }
}
