package com.example.recipeapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.recipeapp.adapters.ViewPagerAdapter;
import com.example.recipeapp.models.Meal;
import com.example.recipeapp.utilities.JsonToModelUtils;
import com.example.recipeapp.utilities.NetworkUtilities;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class FetchRandomMealTask extends AsyncTask<Void, Void, Meal[]> {

    private Context mContext;
    private ViewPagerAdapter mVPA;

    public FetchRandomMealTask(Context mContext, ViewPagerAdapter mVPA) {
        this.mContext = mContext;
        this.mVPA = mVPA;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //TODO progressbar
    }

    @Override
    protected Meal[] doInBackground(Void... voids) {
        URL randomMealsRequestUrl = NetworkUtilities.buildRandomMealURL();

        try {
            String jsonResponse1 = NetworkUtilities
                    .getUrlResponse(randomMealsRequestUrl);
            Meal meal1 = JsonToModelUtils
                    .getMealModelFromJson(mContext, jsonResponse1, 0);

            String jsonResponse2 = NetworkUtilities
                    .getUrlResponse(randomMealsRequestUrl);
            Meal meal2 = JsonToModelUtils
                    .getMealModelFromJson(mContext, jsonResponse2, 0);

            return new Meal[]{meal1, meal2};

        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Meal[] meals) {
        super.onPostExecute(meals);

        mVPA.setMealData(meals);
    }
}
