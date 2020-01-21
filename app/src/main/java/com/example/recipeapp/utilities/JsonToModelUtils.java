package com.example.recipeapp.utilities;

import android.content.Context;

import com.example.recipeapp.models.Category;
import com.example.recipeapp.models.Meal;
import com.example.recipeapp.models.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonToModelUtils {

    public static ArrayList<Category> getCategoryModelsFromJson(Context context, String jsonString)
            throws JSONException {

        final String CAT_MAIN = "categories";

        final String CAT_ID = "idCategory";
        final String CAT_TITLE = "strCategory";
        final String CAT_THUMB = "strCategoryThumb";
        final String CAT_DESC = "strCategoryDescription";

        ArrayList<Category> categories = new ArrayList<>();

        JSONObject categoryJson = new JSONObject(jsonString);

        //TODO there will be http error check

        JSONArray categoryArray = categoryJson.getJSONArray(CAT_MAIN);

        String id;
        String title;
        String thumbnail;
        String description;

        for (int i = 0; i < categoryArray.length(); i++) {

            JSONObject cat = categoryArray.getJSONObject(i);

            id = cat.getString(CAT_ID);
            title = cat.getString(CAT_TITLE);
            thumbnail = cat.getString(CAT_THUMB);
            description = cat.getString(CAT_DESC);

            categories.add(new Category(id, title, thumbnail, description));
        }

        return categories;
    }



    public static ArrayList<Meal> getMealModelsFromJson(Context mContext, String jsonString)
            throws JSONException {
        final String MEAL_MAIN = "meals";

        ArrayList<Meal> meals = new ArrayList<>();

        JSONObject mealJson = new JSONObject(jsonString);

        //TODO there will be http error check


        JSONArray mealArray = mealJson.getJSONArray(MEAL_MAIN);

        if (mealArray != null && mealArray.length() > 0) {
            for (int i = 0; i < mealArray.length(); i++) {

                meals.add(getMealModelFromJson(mContext, jsonString, i));
            }
        }

        return meals;
    }

    public static Meal getMealModelFromJson(Context mContext, String jsonString, int index)
            throws JSONException {

        final String MEAL_MAIN = "meals";

        final String MEAL_ID = "idMeal";
        final String MEAL_TITLE = "strMeal";
        final String MEAL_THUMB = "strMealThumb";

        JSONObject mealJson = new JSONObject(jsonString);

        //TODO there will be http error check


        JSONArray mealArray = mealJson.getJSONArray(MEAL_MAIN);

        String id;
        String title;
        String thumbnail;

        JSONObject meal = mealArray.getJSONObject(index);

        id = meal.getString(MEAL_ID);
        title = meal.getString(MEAL_TITLE);
        thumbnail = meal.getString(MEAL_THUMB);

        return new Meal(id, title, thumbnail);
    }

    public static Recipe getRecipeModelFromJson(Context context, String jsonString)
            throws JSONException {

        final String MEAL_MAIN = "meals";

        final String MEAL_ID = "idMeal";
        final String MEAL_TITLE = "strMeal";
        final String MEAL_THUMB = "strMealThumb";

        final String MEAL_AREA = "strArea";
        final String MEAL_INS = "strInstructions";
        final String MEAL_YOUTUBE = "strYoutube";
        final String MEAL_INGR = "strIngredient";
        final String MEAL_MEASURE = "strMeasure";

        JSONObject mealJson = new JSONObject(jsonString);

        //TODO there will be http error check

        JSONArray mealArray = mealJson.getJSONArray(MEAL_MAIN);

        int counter = 1;
        String _ingredient;
        String _measure;

        String id;
        String title;
        String thumbnail;

        String area;
        String instructions;
        String youtube;

        JSONObject meal = mealArray.getJSONObject(0);

        id = meal.getString(MEAL_ID);
        title = meal.getString(MEAL_TITLE);
        thumbnail = meal.getString(MEAL_THUMB);

        area = meal.getString(MEAL_AREA);
        instructions = meal.getString(MEAL_INS);
        youtube = meal.getString(MEAL_YOUTUBE);



        ArrayList<String> ingList = new ArrayList<>();
        ArrayList<String> measureList = new ArrayList<>();
        _ingredient = MEAL_INGR + counter;
        _measure = MEAL_MEASURE + counter;

        while (meal.getString(_ingredient) != null && meal.getString(_ingredient).length() > 0 && counter < 21) {
            ingList.add(meal.getString(_ingredient));
            measureList.add(meal.getString(_measure));


            _ingredient = MEAL_INGR + counter;
            _measure = MEAL_MEASURE + counter;
            counter++;
        }

        String[] ingredients = ingList.toArray(new String[0]);
        String[] measures = measureList.toArray(new String[0]);

        return new Recipe(id, title, thumbnail, instructions, area, youtube, ingredients, measures);

    }
}
