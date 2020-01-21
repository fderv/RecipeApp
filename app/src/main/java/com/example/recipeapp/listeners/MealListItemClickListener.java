package com.example.recipeapp.listeners;

import com.example.recipeapp.models.Meal;

public interface MealListItemClickListener {
    void onMealItemClick(Meal clickedMeal);
    void onMealItemLongClick(String mealId);
}
