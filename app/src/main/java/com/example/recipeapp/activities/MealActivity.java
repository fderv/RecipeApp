package com.example.recipeapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipeapp.R;
import com.example.recipeapp.adapters.RecipeLayoutAdapter;
import com.example.recipeapp.models.Recipe;
import com.example.recipeapp.tasks.FetchRecipeTask;

public class MealActivity extends AppCompatActivity {

    Handler handler = new Handler();

    private RecipeLayoutAdapter adapter;
    Button addFavBut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        Intent intentThatStartedThisActivity = getIntent();
        final String stringExtra = intentThatStartedThisActivity.getStringExtra("MEAL");

        adapter = new RecipeLayoutAdapter(this);

        FetchRecipeTask fetcher = new FetchRecipeTask(stringExtra, this, adapter, false);
        fetcher.execute();
        /*
        ResultReceiver myResultReceiver = new MyResultReceiver(null);
        Intent intent = new Intent(getApplicationContext(), RecipeService.class);
        intent.putExtra("MI", stringExtra);
        intent.putExtra("DB", false);
        intent.putExtra("receiver", myResultReceiver);
        startService(intent);
*/
        addFavBut = findViewById(R.id.add_fav_button);
        addFavBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FetchRecipeTask fetcher = new FetchRecipeTask(stringExtra, getApplicationContext(), adapter, true);
                fetcher.execute();
/*
                Intent intent = new Intent(getApplicationContext(), RecipeService.class);
                intent.putExtra("RLA", adapter);
                intent.putExtra("MI", stringExtra);
                intent.putExtra("DB", true);
                startService(intent);*/
            }
        });
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
                    adapter.populateViews((Recipe) resultData.getParcelable("recipe"));
                }
            });
        }

    }
}