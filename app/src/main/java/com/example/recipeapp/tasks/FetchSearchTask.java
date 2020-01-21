package com.example.recipeapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.recipeapp.adapters.MealAdapter;
import com.example.recipeapp.models.Meal;
import com.example.recipeapp.utilities.JsonToModelUtils;
import com.example.recipeapp.utilities.NetworkUtilities;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class FetchSearchTask extends AsyncTask<Void, Void, ArrayList<Meal>> {

    private MealAdapter mMealAdapter;
    private Context mContext;
    private String searchString;

    public FetchSearchTask(MealAdapter mealAdapter, Context context, String searchString) {
        this.mMealAdapter = mealAdapter;
        this.mContext = context;
        this.searchString = searchString;
    }

    @Override
    protected ArrayList<Meal> doInBackground(Void... voids) {
        URL mealsRequestUrl = NetworkUtilities.buildSearchMealByNameURL(searchString);

        try {
            String jsonResponse = NetworkUtilities
                    .getUrlResponse(mealsRequestUrl);
            ArrayList<Meal> meals = JsonToModelUtils
                    .getMealModelsFromJson(mContext, jsonResponse);

            if (meals == null)
                return new ArrayList<>();
            else
                return meals;

        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Meal> meals) {
        super.onPostExecute(meals);

        if(meals != null)
            mMealAdapter.setMealData(meals);
    }
}
