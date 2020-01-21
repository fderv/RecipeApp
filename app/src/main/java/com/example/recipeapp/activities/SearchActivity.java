package com.example.recipeapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.recipeapp.R;
import com.example.recipeapp.adapters.MealAdapter;
import com.example.recipeapp.database.DBContract.HistoryEntry;
import com.example.recipeapp.listeners.MealListItemClickListener;
import com.example.recipeapp.models.Meal;
import com.example.recipeapp.sync.SearchMealService;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements MealListItemClickListener {

    private MealAdapter mAdapter;
    private RecyclerView mSearchResultsRV;
    private EditText mSearchET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSearchET = findViewById(R.id.et_search);

        initRecyclerView();
    }

    private void initRecyclerView() {

        mSearchResultsRV = findViewById(R.id.rv_search_results);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mSearchResultsRV.setLayoutManager(layoutManager);
        //mSearchResultsRV.setHasFixedSize(true);
        mAdapter = new MealAdapter(new ArrayList<Meal>(), this, this);
        mSearchResultsRV.setAdapter(mAdapter);

        mSearchET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchString = textView.getText().toString();

//                    FetchSearchTask theLittleGuy = new FetchSearchTask(mAdapter, getApplicationContext(), searchString);
//                    theLittleGuy.execute();
                    Intent intent = new Intent(getApplicationContext(), SearchMealService.class);
                    intent.putExtra("MA", mAdapter);
                    intent.putExtra("SS", searchString);
                    startService(intent);

                    ContentValues historyValues = new ContentValues();
                    historyValues.put(HistoryEntry.COLUMN_SEARCH, searchString);
                    getContentResolver().insert(HistoryEntry.CONTENT_URI, historyValues);

                    handled = true;
                }
                return handled;
            }
        });
    }

    @Override
    public void onMealItemClick(Meal clickedMeal) {
        //TODO go to recipe page
        Intent recipeActivityIntent = new Intent(SearchActivity.this, MealActivity.class);
        recipeActivityIntent.putExtra("MEAL", clickedMeal.getId());
        startActivity(recipeActivityIntent);
    }

    @Override
    public void onMealItemLongClick(String mealId) {

    }
}
