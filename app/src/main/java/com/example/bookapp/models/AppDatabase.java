package com.example.bookapp.models;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.bookapp.interfaces.RecipeDao;

@Database(entities = {Recipe.class}, version = 1,exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {
    public abstract RecipeDao recipeDao();
    //define more Dao bellow
}