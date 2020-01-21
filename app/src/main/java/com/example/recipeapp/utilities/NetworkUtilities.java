package com.example.recipeapp.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtilities {

    private static final String API_BASE_URL        = "https://www.themealdb.com/api";

    // API return format
    private static final String format              = "json";
    // API version
    private static final String version             = "v1";
    // API key (1 as developer key)
    private static final String key                 = "1";

    // API paths
    private static final String categoryPath        = "categories.php";
    private static final String filterPath          = "filter.php";
    private static final String randomPath          = "random.php";
    private static final String searchPath          = "search.php";
    private static final String lookupPath          = "lookup.php";

    // API queries
    private static final String categoryQuery       = "c";
    private static final String searchQuery         = "s";
    private static final String idQuery             = "i";


    public static URL buildCategoryURL () {
        Uri builtUri = Uri.parse(API_BASE_URL).buildUpon()
                .appendEncodedPath(format)
                .appendEncodedPath(version)
                .appendEncodedPath(key)
                .appendEncodedPath(categoryPath)
                .build();


        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            // TODO catch should indicate the problem to user
            e.printStackTrace();
        }

        //Log.v("URL BUILDER", "Built URI " + url);

        return url;
    }


    public static URL buildMealURL(String relatedCategoryTitle) {
        Uri builtUri = Uri.parse(API_BASE_URL).buildUpon()
                .appendEncodedPath(format)
                .appendEncodedPath(version)
                .appendEncodedPath(key)
                .appendEncodedPath(filterPath)
                .appendQueryParameter(categoryQuery, relatedCategoryTitle)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            // TODO catch should indicate the problem to user
            e.printStackTrace();
        }

        //Log.v("URL BUILDER", "Built URI " + url);

        return url;
    }


    public static URL buildRandomMealURL() {
        //TODO need to check both randoms are not the same
        Uri builtUri = Uri.parse(API_BASE_URL).buildUpon()
                .appendEncodedPath(format)
                .appendEncodedPath(version)
                .appendEncodedPath(key)
                .appendEncodedPath(randomPath)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            // TODO catch should indicate the problem to user
            e.printStackTrace();
        }

        //Log.v("URL BUILDER", "Built URI " + url);

        return url;
    }

    public static URL buildSearchMealByNameURL(String searchQueryValue) {
        Uri builtUri = Uri.parse(API_BASE_URL).buildUpon()
                .appendEncodedPath(format)
                .appendEncodedPath(version)
                .appendEncodedPath(key)
                .appendEncodedPath(searchPath)
                .appendQueryParameter(searchQuery, searchQueryValue)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            // TODO catch should indicate the problem to user
            e.printStackTrace();
        }

        return url;
    }


    public static URL buildRecipeURL(String mealId) {

        Uri builtUri = Uri.parse(API_BASE_URL).buildUpon()
                .appendEncodedPath(format)
                .appendEncodedPath(version)
                .appendEncodedPath(key)
                .appendEncodedPath(lookupPath)
                .appendQueryParameter(idQuery, mealId)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            // TODO catch should indicate the problem to user
            e.printStackTrace();
        }
        return url;

    }


    public static String getUrlResponse(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream is = urlConnection.getInputStream();

            Scanner sc = new Scanner(is);
            sc.useDelimiter("\\A");

            boolean input = sc.hasNext();
            if (input) {
                return sc.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
