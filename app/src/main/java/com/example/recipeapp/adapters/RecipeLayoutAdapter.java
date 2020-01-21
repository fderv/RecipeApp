package com.example.recipeapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.recipeapp.R;
import com.example.recipeapp.models.Recipe;

import java.io.Serializable;

public class RecipeLayoutAdapter implements Serializable {



    private TextView titleTV;
    private TextView youtubeTV;
    private TextView ingredientsTV;
    private TextView instructionsTV;
    private ImageView mealImage;

    Context context;

    public RecipeLayoutAdapter(Context context) {
        this.context = context;

        titleTV = ((Activity)context).findViewById(R.id.tv_recipe_title);
        youtubeTV = ((Activity)context).findViewById(R.id.tv_recipe_youtube);
        ingredientsTV = ((Activity)context).findViewById(R.id.tv_recipe_ingredients);
        instructionsTV = ((Activity)context).findViewById(R.id.tv_recipe_instructions);
        mealImage = ((Activity)context).findViewById(R.id.iv_recipe_image);
    }

    public void populateViews(Recipe recipe) {
        if (recipe == null) {
            ((Activity) context).finish();
            return;
        }

        String title = recipe.getTitle();
        String youtube = recipe.getYoutubeLink();
        String[] ingredients = recipe.getIngredients();
        String[] measures = recipe.getMeasures();
        String instructions = recipe.getInstructions();
        String imageUrl = recipe.getThumbnail();
        int ingredientCount = recipe.getIngredientCount();

        String ingredientsField = "";

        for (int i = 0; i < ingredientCount; i++) {
            ingredientsField = ingredientsField + ingredients[i] + " - " + measures[i] + "\n";
        }

        SpannableString ingredientsListString = new SpannableString(ingredientsField);
        ingredientsListString.setSpan(new BulletSpan(), 10, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        titleTV.setText(title);
        youtubeTV.setText(youtube);
        ingredientsTV.setText(ingredientsListString);
        instructionsTV.setText(instructions);
        Glide.with(context).load(imageUrl).into(mealImage);

    }
}