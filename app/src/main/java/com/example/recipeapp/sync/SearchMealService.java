package com.example.recipeapp.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.recipeapp.adapters.MealAdapter;
import com.example.recipeapp.models.Meal;
import com.example.recipeapp.utilities.JsonToModelUtils;
import com.example.recipeapp.utilities.NetworkUtilities;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class SearchMealService extends IntentService {

    private MealAdapter mealAdapter;
    private Context mContext;
    private String searchString;

    public SearchMealService() {
        super("SearchMealService");
        mContext = getApplicationContext();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Bundle dataReceived = intent.getExtras();
        mealAdapter = (MealAdapter) dataReceived.getSerializable("MA");
        searchString = intent.getStringExtra("SS");

        URL mealsRequestUrl = NetworkUtilities.buildSearchMealByNameURL(searchString);

        try {
            String jsonResponse = NetworkUtilities
                    .getUrlResponse(mealsRequestUrl);
            ArrayList<Meal> meals = JsonToModelUtils
                    .getMealModelsFromJson(mContext, jsonResponse);

            if (meals == null)
                return;
            else
                mealAdapter.setMealData(meals);

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
}
