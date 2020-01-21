package com.example.recipeapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.recipeapp.adapters.MealAdapter;
import com.example.recipeapp.models.Category;
import com.example.recipeapp.models.Meal;
import com.example.recipeapp.utilities.JsonToModelUtils;
import com.example.recipeapp.utilities.NetworkUtilities;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class FetchMealTask extends AsyncTask<Void, Void, ArrayList<Meal>> {

    private MealAdapter mMealAdapter;
    private Context mContext;
    private Category mRelatedCategory;

    public FetchMealTask(MealAdapter mealAdapter, Context context, Category relatedCategory) {
        mMealAdapter = mealAdapter;
        mContext = context;
        mRelatedCategory = relatedCategory;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // TODO progressbar
    }

    @Override
    protected ArrayList<Meal> doInBackground(Void... voids) {
        String relatedCategoryTitle = mRelatedCategory.getTitle();
        URL mealsRequestUrl = NetworkUtilities.buildMealURL(relatedCategoryTitle);

        try {
            String jsonResponse = NetworkUtilities
                    .getUrlResponse(mealsRequestUrl);
            ArrayList<Meal> meals = JsonToModelUtils
                    .getMealModelsFromJson(mContext, jsonResponse);
            return meals;

        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Meal> meals) {
        super.onPostExecute(meals);

        if (meals != null) {
            mMealAdapter.setMealData(meals);
        }
        else {
            mMealAdapter.setMealData(new ArrayList<Meal>());
        }
    }
}
