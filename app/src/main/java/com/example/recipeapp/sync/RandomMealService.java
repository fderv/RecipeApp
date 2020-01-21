package com.example.recipeapp.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import androidx.annotation.Nullable;

import com.example.recipeapp.models.Meal;
import com.example.recipeapp.utilities.JsonToModelUtils;
import com.example.recipeapp.utilities.NetworkUtilities;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class RandomMealService extends IntentService {

    private Context mContext;

    public RandomMealService() {
        super("RandomMealService");
        mContext = this;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ResultReceiver resultReceiver = intent.getParcelableExtra("receiver");
        //mVPA = (ViewPagerAdapter) dataReceived.getSerializable("VPA");



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


            Bundle bundle = new Bundle();
            bundle.putParcelableArray("result", new Meal[]{meal1, meal2});
            resultReceiver.send(15, bundle);

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
}
