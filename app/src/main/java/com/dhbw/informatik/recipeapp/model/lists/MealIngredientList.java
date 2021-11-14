package com.dhbw.informatik.recipeapp.model.lists;

import com.dhbw.informatik.recipeapp.model.MealIngredient;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MealIngredientList {

    @SerializedName("meals")
    private List<MealIngredient> ingredientList;

    public MealIngredientList(List<MealIngredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public List<MealIngredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<MealIngredient> ingredientList) {
        this.ingredientList = ingredientList;
    }
}
