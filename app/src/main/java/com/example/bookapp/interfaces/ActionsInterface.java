package com.example.bookapp.interfaces;

import com.example.bookapp.models.Recipe;

public interface ActionsInterface {
    void shareRecipe(Recipe recipe);
    void saveRecipe(Recipe recipe);
    void deleteSaveRecipe(Recipe recipe);
    void expandRecipe(Recipe recipe);
}
