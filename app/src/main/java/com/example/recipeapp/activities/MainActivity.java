package com.example.recipeapp.activities;

import android.app.AlarmManager;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Loader;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.example.recipeapp.adapters.CategoryAdapter;
import com.example.recipeapp.adapters.ViewPagerAdapter;
import com.example.recipeapp.listeners.CategoryListItemClickListener;
import com.example.recipeapp.R;
import com.example.recipeapp.listeners.MealListItemClickListener;
import com.example.recipeapp.models.Category;
import com.example.recipeapp.models.Meal;
import com.example.recipeapp.receivers.AlarmReceiver;
import com.example.recipeapp.sync.CategoryService;

import com.example.recipeapp.database.DBContract.CategoryEntry;
import com.example.recipeapp.tasks.FetchRandomMealTask;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements CategoryListItemClickListener,
        MealListItemClickListener ,
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final int      CATEGORY_LOADER = 0;
    public static final String[] MAIN_CATEGORY_PROJECTION = {
            CategoryEntry.COLUMN_CATEGORY_ID,
            CategoryEntry.COLUMN_TITLE,
            CategoryEntry.COLUMN_THUMB,
            CategoryEntry.COLUMN_DESCRIPTION,
    };


    private CategoryAdapter mAdapter;
    private RecyclerView mCategoriesRV;

    private Button mSearchButton;
    private Button mFavoriteButton;

    private ViewPager mRandomsVP;
    private LinearLayout mSliderDots;
    private int mDotsCount = 2;
    private ImageView[] mDotsImages;

/*
    BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            ConnectivityManager connMgr =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            boolean isWifiConn = false;
            boolean isMobileConn = false;


            for (Network network : connMgr.getAllNetworks()) {
                NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    isWifiConn |= networkInfo.isConnected();
                }
                if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    isMobileConn |= networkInfo.isConnected();
                }
            }

            if (isMobileConn || isWifiConn) {
                Toast.makeText(getApplicationContext(), "Internet connected", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Internet connection lost, may cause some functions to not work properly", Toast.LENGTH_LONG).show();
            }
        }
    };
*/

    ConnectivityReceiver connectivityReceiver;
    IntentFilter mConnectivityIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getLoaderManager().initLoader(CATEGORY_LOADER, null, this);
        updateCategories();

        Toolbar mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        mSearchButton = findViewById(R.id.search);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        mFavoriteButton = findViewById(R.id.favorites);
        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
                startActivity(intent);
            }
        });


        mConnectivityIntentFilter = new IntentFilter();
        mConnectivityIntentFilter.addAction(Intent.ACTION_MANAGE_NETWORK_USAGE);
        connectivityReceiver = new ConnectivityReceiver();


        initViewPager();

        initRecyclerView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        startAlarm();
        registerReceiver(connectivityReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(connectivityReceiver);
    }

    private void initViewPager() {
        mRandomsVP = findViewById(R.id.vp_randoms);
        mSliderDots = findViewById(R.id.slider_dots);

        ViewPagerAdapter vpa = new ViewPagerAdapter(this, new Meal[0], this);

        FetchRandomMealTask task = new FetchRandomMealTask(this, vpa);
        task.execute();
/*
        ResultReceiver myResultReceiver = new MyResultReceiver(null);
        Intent intent = new Intent(this, RandomMealService.class);
        intent.putExtra("receiver", myResultReceiver);
        startService(intent);
*/

        mRandomsVP.setAdapter(vpa);

        mDotsImages = new ImageView[mDotsCount];

        for (int i = 0; i < mDotsCount; i++) {
            mDotsImages[i] = new ImageView(this);
            mDotsImages[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(8,0,8,0);

            mSliderDots.addView(mDotsImages[i], params);
        }
        mDotsImages[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        mRandomsVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mDotsCount; i++) {
                    mDotsImages[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }
                mDotsImages[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initRecyclerView() {

        mCategoriesRV = findViewById(R.id.rv_categories);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mCategoriesRV.setLayoutManager(layoutManager);
        //mCategoriesRV.setHasFixedSize(true);
        mAdapter = new CategoryAdapter(this, this);
        mCategoriesRV.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {

            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCategoryItemClick(Category clickedCategory) {
        Log.v("CLICKED", "CATEGORY");
        Intent mealsListActivityIntent = new Intent(MainActivity.this, MealsListActivity.class);
        mealsListActivityIntent.putExtra("CAT", clickedCategory);

        startActivity(mealsListActivityIntent);
    }

    @Override
    public void onMealItemClick(Meal clickedMeal) {
        Intent recipeActivityIntent = new Intent(MainActivity.this, MealActivity.class);
        recipeActivityIntent.putExtra("MEAL", clickedMeal.getId());
        startActivity(recipeActivityIntent);

    }

    @Override
    public void onMealItemLongClick(String mealId) {

    }


    /*
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri categoryUri = CategoryEntry.buildCategoryUri(id);

        return new CursorLoader(this,
                categoryUri,
                MAIN_CATEGORY_PROJECTION,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }*/

    private void updateCategories() {

        Intent intent = new Intent(this, CategoryService.class);
        startService(intent);

//        FetchCategoryTask task = new FetchCategoryTask(this.getApplicationContext());
//        task.execute();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        Uri categoryUri = CategoryEntry.CONTENT_URI;

        return new CursorLoader(this,
                categoryUri,
                MAIN_CATEGORY_PROJECTION,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    private void startAlarm() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int prefferedHour = Integer.parseInt(prefs.getString("notTime", "20"));

        alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, prefferedHour);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

        alarmMgr.setRepeating(AlarmManager.RTC, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
        //alarmMgr.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), alarmIntent);


    }


    public class ConnectivityReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            ConnectivityManager connMgr =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            boolean isWifiConn = false;
            boolean isMobileConn = false;
            for (Network network : connMgr.getAllNetworks()) {

                NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
                if (networkInfo == null) {
                    isMobileConn = false;
                    isWifiConn = false;
                }
                else {
                    if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        isWifiConn |= networkInfo.isConnected();
                    }
                    if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        isMobileConn |= networkInfo.isConnected();
                    }
                }
            }

            if (isMobileConn || isWifiConn) {
                Toast.makeText(getApplicationContext(), "Internet connected", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Internet connection lost, may cause some functions to not work properly", Toast.LENGTH_LONG).show();
            }
        }
    }
}