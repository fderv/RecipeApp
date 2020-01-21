package com.example.recipeapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import com.example.recipeapp.R;
import com.example.recipeapp.adapters.MealAdapter;
import com.example.recipeapp.listeners.MealListItemClickListener;
import com.example.recipeapp.models.Category;
import com.example.recipeapp.models.Meal;
import com.example.recipeapp.sync.MealService;

import java.util.ArrayList;

public class MealsListActivity extends AppCompatActivity implements MealListItemClickListener {

    Handler handler = new Handler();

    private RecyclerView mMealsRV;
    private MealAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals_list);


        Toolbar mainToolbar = findViewById(R.id.meals_list_toolbar);
        setSupportActionBar(mainToolbar);

        Intent intentThatStartedThisActivity = getIntent();
        Bundle dataReceived = intentThatStartedThisActivity.getExtras();

        if (dataReceived != null) {
            Category relatedCategory = dataReceived.getParcelable("CAT");
            initRecyclerView(relatedCategory);
        }
        else {

            //TODO error check
        }
    }

    private void initRecyclerView(Category relatedCategory) {
        mMealsRV = findViewById(R.id.rv_meals);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mMealsRV.setLayoutManager(layoutManager);
        //mCategoriesRV.setHasFixedSize(true);
        mAdapter = new MealAdapter(new ArrayList<Meal>(), this, this);
        mMealsRV.setAdapter(mAdapter);

//        FetchMealTask task = new FetchMealTask(mAdapter, this, relatedCategory);
//        task.execute();
        ResultReceiver myResultReceiver = new MyResultReceiver(null);
        Intent intent = new Intent(getApplicationContext(), MealService.class);
        intent.putExtra("receiver", myResultReceiver);
        intent.putExtra("RC", relatedCategory);
        startService(intent);

    }

    @Override
    public void onMealItemClick(Meal clickedMeal) {
        //TODO go to recipe page
        Intent recipeActivityIntent = new Intent(MealsListActivity.this, MealActivity.class);
        recipeActivityIntent.putExtra("MEAL", clickedMeal.getId());
        startActivity(recipeActivityIntent);
    }

    @Override
    public void onMealItemLongClick(String mealId) {

    }

    public class MyResultReceiver extends ResultReceiver {

        public MyResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.setMealData(resultData.<Meal>getParcelableArrayList("meals"));
                }
            });
        }

    }
}