package com.example.bookapp.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.bookapp.models.Recipe;
@Dao
public interface RecipeDao {

    @Insert
    void insertRecipe(Recipe recipe);


}
