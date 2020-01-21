package com.example.recipeapp.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.recipeapp.adapters.RecipeLayoutAdapter;
import com.example.recipeapp.database.DBContract.RecipeEntry;
import com.example.recipeapp.models.Recipe;
import com.example.recipeapp.utilities.JsonToModelUtils;
import com.example.recipeapp.utilities.NetworkUtilities;
import com.example.recipeapp.utilities.OtherUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class FetchRecipeTask extends AsyncTask<Void, Void, Recipe> {


    private String mealId;
    private Context mContext;
    private RecipeLayoutAdapter mAdapter;
    private boolean addToDBAfter = false;

    public FetchRecipeTask(String mealId, Context context, RecipeLayoutAdapter adapter, boolean addToDb) {
        this.mealId = mealId;
        mContext = context;
        mAdapter = adapter;
        addToDBAfter = addToDb;
    }

    @Override
    protected Recipe doInBackground(Void... voids) {


        URL recipeRequestURL = NetworkUtilities.buildRecipeURL(mealId);

        try {
            String jsonResponse = NetworkUtilities
                    .getUrlResponse(recipeRequestURL);
            Recipe recipe = JsonToModelUtils
                    .getRecipeModelFromJson(mContext, jsonResponse);
            return recipe;

        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Recipe recipe) {
        super.onPostExecute(recipe);
        mAdapter.populateViews(recipe);
        if (addToDBAfter) {

            String[] projection = new String[] {RecipeEntry._ID};
            String selection = RecipeEntry.COLUMN_RECIPE_ID + "=?";
            String[] selectionArgs = new String[]{recipe.getId()};

            Cursor cursor = mContext.getContentResolver().query(RecipeEntry.CONTENT_URI, projection, selection, selectionArgs, null);
            if (cursor.moveToFirst()) {
                Toast.makeText(mContext, "Recipe already exist", Toast.LENGTH_SHORT).show();
            }
            else {
                ContentValues recipeValues = new ContentValues();
                recipeValues.put(RecipeEntry.COLUMN_RECIPE_ID, recipe.getId());
                recipeValues.put(RecipeEntry.COLUMN_TITLE, recipe.getTitle());
                recipeValues.put(RecipeEntry.COLUMN_THUMB, recipe.getThumbnail());
                recipeValues.put(RecipeEntry.COLUMN_INSTRUCTIONS, recipe.getInstructions());
                recipeValues.put(RecipeEntry.COLUMN_AREA, recipe.getArea());
                recipeValues.put(RecipeEntry.COLUMN_YOUTUBE, recipe.getYoutubeLink());
                recipeValues.put(RecipeEntry.COLUMN_INGREDIENTS, OtherUtils.convertArrayToString(recipe.getIngredients()));
                recipeValues.put(RecipeEntry.COLUMN_MEASURES, OtherUtils.convertArrayToString(recipe.getMeasures()));
                recipeValues.put(RecipeEntry.COLUMN_ING_COUNT, recipe.getIngredientCount());

                mContext.getContentResolver().insert(RecipeEntry.CONTENT_URI, recipeValues);
            }


        }
    }

    public void addRecipe(Recipe recipe) {
        ContentValues recipeValues = new ContentValues();
        mContext.getContentResolver().insert(RecipeEntry.CONTENT_URI, recipeValues);
    }
}
