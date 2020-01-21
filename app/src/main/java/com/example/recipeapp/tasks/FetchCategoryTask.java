package com.example.recipeapp.tasks;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.example.recipeapp.models.Category;
import com.example.recipeapp.utilities.JsonToModelUtils;
import com.example.recipeapp.utilities.NetworkUtilities;

import com.example.recipeapp.database.DBContract.CategoryEntry;

import org.json.JSONException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class FetchCategoryTask extends AsyncTask<Void, Void, Void> {

    private Context mContext;
    private ContentResolver mResolver;

    public FetchCategoryTask(Context context) {
        mContext = context;
        mResolver = context.getContentResolver();
    }

    public void addCategory(ArrayList<Category> categories) {
        ContentValues categoryValues[] = new ContentValues[categories.size()];

        for (int i = 0; i < categories.size(); i++) {
            categoryValues[i] = new ContentValues();
            categoryValues[i].put(CategoryEntry.COLUMN_CATEGORY_ID, categories.get(i).getId());
            categoryValues[i].put(CategoryEntry.COLUMN_TITLE, categories.get(i).getTitle());
            categoryValues[i].put(CategoryEntry.COLUMN_THUMB, categories.get(i).getThumbnail());
            categoryValues[i].put(CategoryEntry.COLUMN_DESCRIPTION, categories.get(i).getDescription());
        }

        for (int i = 0; i < categories.size(); i++) {
            mResolver.insert(CategoryEntry.CONTENT_URI, categoryValues[i]);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //TODO progressbar
    }

    @Override
    protected Void doInBackground(Void... voids) {
        URL mealCategoriesRequestUrl = NetworkUtilities.buildCategoryURL();

        try {
            String jsonResponse = NetworkUtilities
                    .getUrlResponse(mealCategoriesRequestUrl);
            ArrayList<Category> categories = JsonToModelUtils
                    .getCategoryModelsFromJson(mContext, jsonResponse);

            addCategory(categories);

        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }
}
