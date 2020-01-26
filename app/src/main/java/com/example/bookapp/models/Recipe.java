package com.example.bookapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.HashMap;
@Entity(tableName = "recipes")
public class Recipe implements Parcelable {

    @PrimaryKey
    public int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "image_url")
    private String imageUrl;
    @ColumnInfo(name = "health_points")
    private String healthPoints;
    @ColumnInfo(name = "cooking_time")
    private String cookingTime;
    @ColumnInfo(name = "number_of_people")
    private String numberOfPeople;
  @Ignore
    private HashMap<String,Boolean>features;
    @ColumnInfo(name = "dish_type")
    private String dishType;
    @Ignore
    private ArrayList<String> ingredients;
    @Ignore
    private ArrayList<String> instructions;

    //empty constructor required by room
    public Recipe(){

    }
    public Recipe(int id,String name, String imageUrl, String healthPoints, String cookingTime, String numberOfPeople, HashMap<String, Boolean> features, String dishType,
    ArrayList<String>ingredients,ArrayList<String>instructions) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.healthPoints = healthPoints;
        this.cookingTime = cookingTime;
        this.numberOfPeople = numberOfPeople;
        this.features = features;
        this.dishType = dishType;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    protected Recipe(Parcel in) {
        id= in .readInt();
        name = in.readString();
        imageUrl = in.readString();
        healthPoints = in.readString();
        cookingTime = in.readString();
        numberOfPeople = in.readString();
        features = in.readHashMap(Boolean.class.getClassLoader());
        dishType = in.readString();
        in.readStringList(ingredients);
        in.readStringList(instructions);
    }
    @Ignore
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
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(imageUrl);
        dest.writeString(healthPoints);
        dest.writeString(cookingTime);
        dest.writeString(numberOfPeople);
        dest.writeMap(features);
        dest.writeString(dishType);
        dest.writeStringList(ingredients);
        dest.writeStringList(instructions);
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getHealthPoints() {
        return healthPoints;
    }

    public String getCookingTime() {
        return cookingTime;
    }

    public String getNumberOfPeople() {
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

    public ArrayList<String> getInstructions() {
        return instructions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setHealthPoints(String healthPoints) {
        this.healthPoints = healthPoints;
    }

    public void setCookingTime(String cookingTime) {
        this.cookingTime = cookingTime;
    }

    public void setNumberOfPeople(String numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public void setFeatures(HashMap<String, Boolean> features) {
        this.features = features;
    }

    public void setDishType(String dishType) {
        this.dishType = dishType;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void setInstructions(ArrayList<String> instructions) {
        this.instructions = instructions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}


