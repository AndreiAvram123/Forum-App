package com.example.bookapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

public class Recipe implements Parcelable {

    private String name;
    private String imageUrl;
    private int healthPoints;
    private int cookingTime;
    private int numberOfPeople;
    private HashMap<String,Boolean>features;
    private String dishType;
    private ArrayList<String> ingredients;

    public Recipe(String name, String imageUrl, int healthPoints, int cookingTime, int numberOfPeople, HashMap<String, Boolean> features, String dishType,
    ArrayList<String>ingredients) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.healthPoints = healthPoints;
        this.cookingTime = cookingTime;
        this.numberOfPeople = numberOfPeople;
        this.features = features;
        this.dishType = dishType;
        this.ingredients = ingredients;
    }

    protected Recipe(Parcel in) {
        name = in.readString();
        imageUrl = in.readString();
        healthPoints = in.readInt();
        cookingTime = in.readInt();
        numberOfPeople = in.readInt();
        features = in.readHashMap(Boolean.class.getClassLoader());
        dishType = in.readString();
        in.readStringList(ingredients);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(imageUrl);
        dest.writeInt(healthPoints);
        dest.writeInt(cookingTime);
        dest.writeInt(numberOfPeople);
        dest.writeMap(features);
        dest.writeString(dishType);
        dest.writeStringList(ingredients);
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public HashMap<String, Boolean> getFeatures() {
        return features;
    }

    public String getDishType() {
        return dishType;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }
}


