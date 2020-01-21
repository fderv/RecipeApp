package com.example.recipeapp.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class DBContract {

    public static final String CONTENT_AUTHORITY = "com.example.recipeapp";
    public static final Uri BASE_CONTENT_URI  = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_RECIPE    = "recipe";
    public static final String PATH_CATEGORY  = "category";
    public static final String PATH_HISTORY      = "history";

    public static final class RecipeEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_RECIPE)
                .build();

        public static final String CONTENT_TYPE      = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECIPE;

        public static final String TABLE_NAME           = "recipe";

        public static final String COLUMN_RECIPE_ID     = "id";
        public static final String COLUMN_TITLE         = "title";
        public static final String COLUMN_THUMB         = "thumb";
        public static final String COLUMN_INSTRUCTIONS  = "instructions";
        public static final String COLUMN_AREA          = "area";
        public static final String COLUMN_YOUTUBE       = "youtubeLink";
        public static final String COLUMN_INGREDIENTS   = "ingredients";
        public static final String COLUMN_MEASURES      = "measures";
        public static final String COLUMN_ING_COUNT     = "ingCount";

        public static Uri buildRecipeUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class CategoryEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_CATEGORY)
                .build();

        public static final String CONTENT_TYPE      = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORY;

        public static final String TABLE_NAME           = "category";

        public static final String COLUMN_CATEGORY_ID   = "id";
        public static final String COLUMN_TITLE         = "title";
        public static final String COLUMN_THUMB         = "thumb";
        public static final String COLUMN_DESCRIPTION   = "description";

        public static Uri buildCategoryUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class HistoryEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_HISTORY)
                .build();

        public static final String CONTENT_TYPE      = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HISTORY;

        public static final String TABLE_NAME           = "history";

        public static final String COLUMN_SEARCH        = "search";

        public static Uri buildHistoryUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}