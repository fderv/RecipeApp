package com.example.recipeapp.sync;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.example.recipeapp.database.DBContract;
import com.example.recipeapp.models.Category;
import com.example.recipeapp.utilities.JsonToModelUtils;
import com.example.recipeapp.utilities.NetworkUtilities;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class CategoryService extends IntentService {

    private Context mContext;
    private ContentResolver mResolver;

    public CategoryService() {
        super("CategoryService");
        mContext = this;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mResolver = this.getContentResolver();

        URL mealCategoriesRequestUrl = NetworkUtilities.buildCategoryURL();

        try {
            String jsonResponse = NetworkUtilities
                    .getUrlResponse(mealCategoriesRequestUrl);
            ArrayList<Category> categories = JsonToModelUtils
                    .getCategoryModelsFromJson(mContext, jsonResponse);

            addCategory(categories);

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public void addCategory(ArrayList<Category> categories) {
        ContentValues categoryValues[] = new ContentValues[categories.size()];

        for (int i = 0; i < categories.size(); i++) {
            categoryValues[i] = new ContentValues();
            categoryValues[i].put(DBContract.CategoryEntry.COLUMN_CATEGORY_ID, categories.get(i).getId());
            categoryValues[i].put(DBContract.CategoryEntry.COLUMN_TITLE, categories.get(i).getTitle());
            categoryValues[i].put(DBContract.CategoryEntry.COLUMN_THUMB, categories.get(i).getThumbnail());
            categoryValues[i].put(DBContract.CategoryEntry.COLUMN_DESCRIPTION, categories.get(i).getDescription());
        }

        for (int i = 0; i < categories.size(); i++) {
            mResolver.insert(DBContract.CategoryEntry.CONTENT_URI, categoryValues[i]);
        }
    }
}
