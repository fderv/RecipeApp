package com.example.recipeapp.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import androidx.annotation.Nullable;

import com.example.recipeapp.models.Category;
import com.example.recipeapp.models.Meal;
import com.example.recipeapp.utilities.JsonToModelUtils;
import com.example.recipeapp.utilities.NetworkUtilities;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MealService extends IntentService {

    private Context mContext;
    private Category mRelatedCategory;

    public MealService() {
        super("MealService");
        mContext = this;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ResultReceiver resultReceiver = intent.getParcelableExtra("receiver");

        Bundle dataReceived = intent.getExtras();
        mRelatedCategory = dataReceived.getParcelable("RC");

        String relatedCategoryTitle = mRelatedCategory.getTitle();

        URL mealsRequestUrl = NetworkUtilities.buildMealURL(relatedCategoryTitle);

        try {
            String jsonResponse = NetworkUtilities
                    .getUrlResponse(mealsRequestUrl);
            ArrayList<Meal> meals = JsonToModelUtils
                    .getMealModelsFromJson(mContext, jsonResponse);

            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("meals", meals);
            resultReceiver.send(12, bundle);

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
}
