package com.example.recipeapp.sync;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.recipeapp.database.DBContract;
import com.example.recipeapp.models.Recipe;
import com.example.recipeapp.utilities.JsonToModelUtils;
import com.example.recipeapp.utilities.NetworkUtilities;
import com.example.recipeapp.utilities.OtherUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class RecipeService extends IntentService {

    private String mealId;
    private Context mContext;
    private boolean addToDBAfter = false;

    public RecipeService() {
        super("RecipeService");
        mContext = this;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ResultReceiver resultReceiver = intent.getParcelableExtra("receiver");

        Log.d("STARTED!", "YEAH!");
        mealId = intent.getStringExtra("MI");
        addToDBAfter = intent.getBooleanExtra("DB", false);

        URL recipeRequestURL = NetworkUtilities.buildRecipeURL(mealId);

        try {
            String jsonResponse = NetworkUtilities
                    .getUrlResponse(recipeRequestURL);
            Recipe recipe = JsonToModelUtils
                    .getRecipeModelFromJson(mContext, jsonResponse);

            Bundle bundle = new Bundle();
            bundle.putParcelable("recipe", recipe);
            resultReceiver.send(15, bundle);

            if (addToDBAfter) {

                String[] projection = new String[] {DBContract.RecipeEntry._ID};
                String selection = DBContract.RecipeEntry.COLUMN_RECIPE_ID + "=?";
                String[] selectionArgs = new String[]{recipe.getId()};

                Cursor cursor = mContext.getContentResolver().query(DBContract.RecipeEntry.CONTENT_URI, projection, selection, selectionArgs, null);
                if (cursor.moveToFirst()) {
                    Toast.makeText(mContext, "Recipe already exist", Toast.LENGTH_SHORT).show();
                }
                else {
                    ContentValues recipeValues = new ContentValues();
                    recipeValues.put(DBContract.RecipeEntry.COLUMN_RECIPE_ID, recipe.getId());
                    recipeValues.put(DBContract.RecipeEntry.COLUMN_TITLE, recipe.getTitle());
                    recipeValues.put(DBContract.RecipeEntry.COLUMN_THUMB, recipe.getThumbnail());
                    recipeValues.put(DBContract.RecipeEntry.COLUMN_INSTRUCTIONS, recipe.getInstructions());
                    recipeValues.put(DBContract.RecipeEntry.COLUMN_AREA, recipe.getArea());
                    recipeValues.put(DBContract.RecipeEntry.COLUMN_YOUTUBE, recipe.getYoutubeLink());
                    recipeValues.put(DBContract.RecipeEntry.COLUMN_INGREDIENTS, OtherUtils.convertArrayToString(recipe.getIngredients()));
                    recipeValues.put(DBContract.RecipeEntry.COLUMN_MEASURES, OtherUtils.convertArrayToString(recipe.getMeasures()));
                    recipeValues.put(DBContract.RecipeEntry.COLUMN_ING_COUNT, recipe.getIngredientCount());

                    mContext.getContentResolver().insert(DBContract.RecipeEntry.CONTENT_URI, recipeValues);
                }


            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
}
