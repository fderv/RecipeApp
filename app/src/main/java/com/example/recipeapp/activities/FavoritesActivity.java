package com.example.recipeapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.example.recipeapp.R;
import com.example.recipeapp.adapters.FavAdapter;
import com.example.recipeapp.database.DBContract.RecipeEntry;
import com.example.recipeapp.listeners.MealListItemClickListener;
import com.example.recipeapp.models.Meal;

public class FavoritesActivity extends AppCompatActivity implements MealListItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    public static final int      RECIPE_LOADER = 0;
    public static final String[] RECIPE_PROJECTION = {
            RecipeEntry.COLUMN_RECIPE_ID,
            RecipeEntry.COLUMN_TITLE,
            RecipeEntry.COLUMN_THUMB,
            RecipeEntry.COLUMN_INSTRUCTIONS,
            RecipeEntry.COLUMN_AREA,
            RecipeEntry.COLUMN_YOUTUBE,
            RecipeEntry.COLUMN_INGREDIENTS,
            RecipeEntry.COLUMN_MEASURES,
            RecipeEntry.COLUMN_ING_COUNT
    };


    RecyclerView favListRV;
    FavAdapter fAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        getLoaderManager().initLoader(RECIPE_LOADER, null, this);

        favListRV = findViewById(R.id.rv_favorites);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        favListRV.setLayoutManager(layoutManager);
        favListRV.setHasFixedSize(true);
        fAdapter = new FavAdapter(this, this);
        favListRV.setAdapter(fAdapter);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri recipeUri = RecipeEntry.CONTENT_URI;

        return new CursorLoader(this,
                recipeUri,
                RECIPE_PROJECTION,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        fAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        fAdapter.swapCursor(null);
    }

    @Override
    public void onMealItemClick(Meal clickedMeal) {
        Intent recipeActivityIntent = new Intent(FavoritesActivity.this, MealActivity.class);
        recipeActivityIntent.putExtra("MEAL", clickedMeal.getId());
        startActivity(recipeActivityIntent);
    }

    @Override
    public void onMealItemLongClick(String mealId) {
        String[] idOfToBeRemoved = new String[]{mealId};
        getContentResolver().delete(RecipeEntry.CONTENT_URI, RecipeEntry.COLUMN_RECIPE_ID + "=?", idOfToBeRemoved);
        getLoaderManager().restartLoader(RECIPE_LOADER, null, this);
    }
}