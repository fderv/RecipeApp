package com.example.recipeapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Recipe implements Parcelable {

    private String id;
    private String title;
    private String thumbnail;

    private String instructions;
    private String area;
    private String youtubeLink;
    private String[] ingredients;
    private String[] measures;
    private int ingredientCount;

    public Recipe(String id, String title, String thumbnail, String instructions, String area, String youtubeLink, String[] ingredients, String[] measures) {
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
        this.instructions = instructions;
        this.area = area;
        this.youtubeLink = youtubeLink;
        this.ingredients = ingredients;
        this.measures = measures;
        this.ingredientCount = ingredients.length;
    }

    protected Recipe(Parcel in) {
        id = in.readString();
        title = in.readString();
        thumbnail = in.readString();
        instructions = in.readString();
        area = in.readString();
        youtubeLink = in.readString();
        ingredients = in.createStringArray();
        measures = in.createStringArray();
        ingredientCount = in.readInt();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public String[] getMeasures() {
        return measures;
    }

    public String getArea() {
        return area;
    }

    public int getIngredientCount() {
        return ingredientCount;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "title=" + getTitle() +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(thumbnail);
        parcel.writeString(instructions);
        parcel.writeString(area);
        parcel.writeString(youtubeLink);
        parcel.writeStringArray(ingredients);
        parcel.writeStringArray(measures);
        parcel.writeInt(ingredientCount);
    }
}
